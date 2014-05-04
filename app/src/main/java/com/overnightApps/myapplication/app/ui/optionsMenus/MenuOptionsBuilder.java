package com.overnightApps.myapplication.app.ui.optionsMenus;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuOptionsBuilder {
    private final List<MenuItem> items;
    private final Map<Integer, MenuItem> itemMap;

    public MenuOptionsBuilder(List<MenuItem> items, Map<Integer, MenuItem> itemMap) {
        this.items = items;
        this.itemMap = itemMap;
    }

    public static MenuOptionsBuilder newInstance() {
        return new MenuOptionsBuilder(new ArrayList<MenuItem>(), new HashMap<Integer, MenuItem>());
    }

    public MenuOptionsBuilder addItem(MenuItem item) {
        Assert.assertFalse(itemMap.containsKey(item.id));

        items.add(item);
        itemMap.put(item.id, item);
        return this;
    }

    public MenuOptions createMenuOptions() {
        return new MenuOptions(items, itemMap);
    }
}