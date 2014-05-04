package com.overnightApps.myapplication.app.ui.optionsMenus;

import java.util.List;
import java.util.Map;

/**
 * Created by andre on 3/14/14.
 */
public class MenuOptions {
    /**
     * An array of sample (dummy) items.
     */
    private final List<MenuItem> items;
    /**
     * A map of sample (dummy) items, by ID.
     */
    private final Map<Integer, MenuItem> itemMap;

    public MenuOptions(List<MenuItem> items, Map<Integer, MenuItem> itemMap) {
        this.itemMap = itemMap;
        this.items = items;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public Map<Integer, MenuItem> getItemMap() {
        return itemMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuOptions that = (MenuOptions) o;

        return itemMap.equals(that.itemMap) && items.equals(that.items);

    }

    @Override
    public int hashCode() {
        int result = items.hashCode();
        result = 31 * result + itemMap.hashCode();
        return result;
    }
}
