package ceylon.linux.controller;

import java.util.ArrayList;

import com.example.dimosales.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ceylon.linux.model.NavDrawerItem;
import ceylon.linux.model.NavDrawerSubItem;

public class NavDrawerListAdapter extends BaseExpandableListAdapter {

	private final Context context;
	private final ArrayList<NavDrawerItem> navDrawerItems;
	private int selectedGroup;
	private int selectedChild;

	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getGroupCount() {
		return navDrawerItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return navDrawerItems.get(groupPosition).getNavDrawerSubItems().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return navDrawerItems.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return navDrawerItems.get(groupPosition).getNavDrawerSubItems().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int position, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		NavDrawerItem navDrawerItem = navDrawerItems.get(position);
		txtTitle.setText(navDrawerItem.getTitle());
		return convertView;
	}

	@Override
	public View getChildView(int position, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_sub_item, null);
		}
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
		NavDrawerSubItem navDrawerSubItem = navDrawerItems.get(position).getNavDrawerSubItems().get(childPosition);

		txtTitle.setText(navDrawerSubItem.getTitle());

		if (navDrawerSubItem.getIcon() != 0) {
			imgIcon.setImageResource(navDrawerSubItem.getIcon());
		} else {
			imgIcon.setVisibility(View.INVISIBLE);
		}

		if (navDrawerSubItem.isCounterVisibility()) {
			txtCount.setText(Integer.toString(navDrawerSubItem.getCount()));
		} else {
			txtCount.setVisibility(View.INVISIBLE);
		}
		if (position == selectedGroup && childPosition == selectedChild) {
			convertView.setBackgroundColor(Color.BLUE);
		} else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int i, int i2) {
		return true;
	}

	public void setSelection(int categoryPosition, int childPosition) {
		this.selectedGroup = categoryPosition;
		this.selectedChild = childPosition;
	}
}