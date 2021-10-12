/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : May 28, 2014, 9:04:52 AM
 */
package ceylon.linux.model;

/**
 * NavDrawerSubItem - Description of NavDrawerSubItem
 *
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class NavDrawerSubItem {

	private String title;
	private int icon;
	private int count;
	private boolean counterVisibility;

	public NavDrawerSubItem() {
	}

	public NavDrawerSubItem(String title) {
		this.title = title;
	}

	public NavDrawerSubItem(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	public NavDrawerSubItem(String title, int icon, int count) {
		this.title = title;
		this.icon = icon;
		this.count = count;
	}

	public NavDrawerSubItem(String title, int icon, int count, boolean counterVisibility) {
		this.title = title;
		this.icon = icon;
		this.count = count;
		this.counterVisibility = counterVisibility;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the icon
	 */
	public int getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(int icon) {
		this.icon = icon;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the counterVisibility
	 */
	public boolean isCounterVisibility() {
		return counterVisibility;
	}

	/**
	 * @param counterVisibility the counterVisibility to set
	 */
	public void setCounterVisibility(boolean counterVisibility) {
		this.counterVisibility = counterVisibility;
	}

}
