package ceylon.linux.model;

import java.util.ArrayList;

public class NavDrawerItem {

	private String title;
	private int icon;
	private ArrayList<NavDrawerSubItem> navDrawerSubItems = new ArrayList<NavDrawerSubItem>();

	public NavDrawerItem() {
	}

	public NavDrawerItem(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return this.icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	/**
	 * @return the navDrawerSubItems
	 */
	public ArrayList<NavDrawerSubItem> getNavDrawerSubItems() {
		return navDrawerSubItems;
	}

	/**
	 * @param navDrawerSubItems the navDrawerSubItems to set
	 */
	public void setNavDrawerSubItems(ArrayList<NavDrawerSubItem> navDrawerSubItems) {
		this.navDrawerSubItems = navDrawerSubItems;
	}
}
