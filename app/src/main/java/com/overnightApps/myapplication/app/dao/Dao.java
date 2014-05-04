package com.overnightApps.myapplication.app.dao;

import android.util.Log;

import com.overnightApps.myapplication.app.dao.exceptions.DataClassNotFoundException;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by andre on 3/22/14.
 */
public abstract class Dao<DomainClass, DataClass extends ParseObject, QueryClass extends ParseQuery<DataClass>> {
    public abstract DomainClass convertToDomainObject(DataClass parseObject);
    final static public String CREATED_AT_TAG = "createdAt";

    public  DataClass find(DomainClass searchObject) throws DataClassNotFoundException {
        QueryClass parseQuery = getNewUniqueResultQueryInstance(searchObject);
        return findSingleObject(parseQuery);
    }

    public DataClass save(DomainClass domainClass){
        DataClass searchDataClassInstance;
        try {
            searchDataClassInstance = find(domainClass);
        } catch (DataClassNotFoundException dataClassNotFoundException) {
            searchDataClassInstance = getNewDataClassInstance();
        }
        saveDataClassFromDomain(searchDataClassInstance, domainClass);
        searchDataClassInstance.saveInBackground();
        return searchDataClassInstance;
    }

    protected abstract DataClass getNewDataClassInstance();

    /** this is done so we can inject QueryClass dependency at the subclass level with the
     * extract and override technique*/
    protected abstract QueryClass getNewQueryInstance();

    protected abstract void saveDataClassFromDomain(DataClass dataClass, DomainClass domainClass);

    protected abstract QueryClass getNewUniqueResultQueryInstance(DomainClass domainClass);

    DataClass findSingleObject(QueryClass query) throws DataClassNotFoundException {
        ArrayList<DataClass> results = null;
        try {
            results = (ArrayList<DataClass>) query.find();
        } catch (ParseException e) {
            Log.e("Dao-findSingleObject","Error",e);
        }
        Assert.assertTrue("No more than one parse object should be returned.", results.size() <= 1);
        if(results.isEmpty()){
            throw new DataClassNotFoundException("Object not found on back end");
        } else{
            return results.get(0);
        }
    }
}