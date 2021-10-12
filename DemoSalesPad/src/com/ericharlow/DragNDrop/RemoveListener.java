package com.ericharlow.DragNDrop;


public interface RemoveListener {

	/**
	 * Called when an item is to be removed
	 *
	 * @param which - indicates which item to remove.
	 */
	void onRemove(int which);
}
