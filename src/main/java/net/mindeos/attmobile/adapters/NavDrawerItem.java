// This file is part of AttMobile - http://att.mindeos.net/
//
// AttMobile is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// AttMobile is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with AttMobile. If not, see <http://www.gnu.org/licenses/>.

package net.mindeos.attmobile.adapters;

/**
 * Adapter for the items of the navigation drawer.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class NavDrawerItem {
    private String title;
    private int icon;
    private boolean isVisible = false;
    private boolean isTitle = false;
    private int action;


    /**
     * Instantiates a new item of the navigation drawer.
     *
     * @param title the title of this element
     * @param icon the icon used in this element
     * @param isVisible whether this element is visible or not
     * @param action the action assigned to this element
     */
    public NavDrawerItem(String title, int icon, boolean isVisible, int action) {
        this.title = title;
        this.icon = icon;
        this.isVisible = isVisible;
        this.action = action;
    }

    /**
     * Instantiates a new title item for the navigation drawer.
     *
     * @param title the title
     * @param isTitle the is title
     */
    public NavDrawerItem(String title, boolean isTitle) {
        this.title = title;
        this.isTitle = true;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public int getIcon() {
        return icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon the icon
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }

    /**
     * Returns if the element is visible or not.
     *
     * @return the boolean
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Sets visibility of the item.
     *
     * @param isVisible the is visible
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Returns if this element is a title element or not.
     *
     * @return the boolean
     */
    public boolean isTitle() {
        return isTitle;
    }

    /**
     * Sets a boolean indicating if this item is a title item or not.
     *
     * @param isTitle the is title
     */
    public void setTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }

    /**
     * Gets the action associated to the item.
     *
     * @return the action
     */
    public int getAction() {
        return action;
    }

    /**
     * Sets an action associated to the item.
     *
     * @param action the action associated to the item.
     */
    public void setAction(int action) {
        this.action = action;
    }
}