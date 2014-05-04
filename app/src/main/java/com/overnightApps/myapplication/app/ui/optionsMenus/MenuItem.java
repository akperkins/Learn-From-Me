package com.overnightApps.myapplication.app.ui.optionsMenus;

/**
 * An item representing an option on a menu.
 */
public class MenuItem {
    public final Integer id;
    private final String content;

    public MenuItem(int id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuItem menuItem = (MenuItem) o;

        return content.equals(menuItem.content) && id.equals(menuItem.id);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }
}
