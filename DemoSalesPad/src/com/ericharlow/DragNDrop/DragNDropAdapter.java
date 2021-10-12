package com.ericharlow.DragNDrop;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ceylon.linux.view.NotificationFragment.MarketingActivities;

public final class DragNDropAdapter extends BaseAdapter implements
	RemoveListener, DropListener {

	private int[] mIds;
	private int[] mLayouts;
	private LayoutInflater mInflater;
	private ArrayList<MarketingActivities> mContent;

	public DragNDropAdapter(Context context, int[] itemLayouts, int[] itemIDs,
	                        ArrayList<MarketingActivities> content) {
		init(context, itemLayouts, itemIDs, content);
	}

	private void init(Context context, int[] layouts, int[] ids,
	                  ArrayList<MarketingActivities> content) {
		// Cache the LayoutInflate to avoid asking for a new one each time.
		mInflater = LayoutInflater.from(context);
		mIds = ids;
		mLayouts = layouts;
		mContent = content;
	}

	/**
	 * The number of items in the list
	 *
	 * @see android.widget.ListAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return mContent.size();
	}

	/**
	 * Since the data comes from an array, just returning the index is
	 * sufficient to get at the data. If we were using a more complex data
	 * structure, we would return whatever object represents one row in the
	 * list.
	 *
	 * @see android.widget.ListAdapter#getItem(int)
	 */

	/**
	 * Use the array index as a unique id.
	 *
	 * @see android.widget.ListAdapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Make a view to hold each row.
	 *
	 * @see android.widget.ListAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// A ViewHolder keeps references to children views to avoid unneccessary
		// calls
		// to findViewById() on each row.
		ViewHolder holder;

		// When convertView is not null, we can reuse it directly, there is no
		// need
		// to reinflate it. We only inflate a new View when the convertView
		// supplied
		// by ListView is null.
		if (convertView == null) {
			convertView = mInflater.inflate(mLayouts[0], null);

			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(mIds[0]);
			holder.text2 = (TextView) convertView.findViewById(mIds[1]);
			holder.text3 = (TextView) convertView.findViewById(mIds[2]);

			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}

		// Bind the data efficiently with the holder.
		MarketingActivities m = getItem(position);

		holder.text.setText(m.campaign_date);
		holder.text2.setText(m.location);
		holder.text3.setText(m.campaign_type);

		return convertView;
	}

	@Override
	public void onRemove(int which) {
		if (which < 0 || which > mContent.size())
			return;
		mContent.remove(which);
	}

	@Override
	public void onDrop(int from, int to) {
		MarketingActivities temp = mContent.get(from);
		mContent.remove(from);
		mContent.add(to, temp);
	}

	@Override
	public MarketingActivities getItem(int position) {
		// TODO Auto-generated method stub
		return mContent.get(position);
	}

	static class ViewHolder {
		TextView text;
		TextView text2;
		TextView text3;
	}
}