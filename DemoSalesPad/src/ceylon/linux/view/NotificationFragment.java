package ceylon.linux.view;

import java.util.ArrayList;

import com.example.dimosales.R;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import ceylon.linux.db.Dbworker;

import com.ericharlow.DragNDrop.DragListener;
import com.ericharlow.DragNDrop.DragNDropAdapter;
import com.ericharlow.DragNDrop.DragNDropListView;
import com.ericharlow.DragNDrop.DropListener;
import com.ericharlow.DragNDrop.RemoveListener;

public class NotificationFragment extends ListFragment implements
	OnItemClickListener {
	public OnClickListener show_data = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			for (int i = 0; i < listView.getAdapter().getCount(); i++) {

				MarketingActivities m = (MarketingActivities) listView.getAdapter().getItem(i);
				db.update_set_priority(m.id, i + "");

			}

		}
	};
	ListView listView;
	;
	ArrayList<MarketingActivities> content = new ArrayList<MarketingActivities>();
	Dbworker db;
	Button show;
	private DropListener mDropListener = new DropListener() {
		@Override
		public void onDrop(int from, int to) {
			ListAdapter adapter = getListAdapter();
			if (adapter instanceof DragNDropAdapter) {
				((DragNDropAdapter) adapter).onDrop(from, to);
				getListView().invalidateViews();
			}
		}
	};
	private RemoveListener mRemoveListener = new RemoveListener() {
		@Override
		public void onRemove(int which) {
			ListAdapter adapter = getListAdapter();
			if (adapter instanceof DragNDropAdapter) {
				((DragNDropAdapter) adapter).onRemove(which);
				getListView().invalidateViews();
			}
		}
	};
	private DragListener mDragListener = new DragListener() {

		int backgroundColor = 0xe0103010;
		int defaultBackgroundColor;

		@Override
		public void onDrag(int x, int y, ListView listView) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStartDrag(View itemView) {
			itemView.setVisibility(View.INVISIBLE);
			defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
			itemView.setBackgroundColor(backgroundColor);
			ImageView iv = (ImageView) itemView.findViewById(R.id.ImageView01);
			if (iv != null)
				iv.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onStopDrag(View itemView) {
			itemView.setVisibility(View.VISIBLE);
			itemView.setBackgroundColor(defaultBackgroundColor);
			ImageView iv = (ImageView) itemView.findViewById(R.id.ImageView01);
			if (iv != null)
				iv.setVisibility(View.VISIBLE);
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dragndroplistview, container,
			false);
		show = (Button) rootView.findViewById(R.id.show_button);
		show.setOnClickListener(show_data);
		db = new Dbworker(getActivity());
		get_marketing_activities();
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setListAdapter(new DragNDropAdapter(getActivity(),
			new int[]{R.layout.dragitem}, new int[]{R.id.TextView01,
			R.id.TextView_01, R.id.TextView_02}, content));// new
		// DragNDropAdapter(this,content)
		listView = getListView();
		if (listView instanceof DragNDropListView) {

			((DragNDropListView) listView).setRemoveListener(mRemoveListener);
			((DragNDropListView) listView).setDragListener(mDragListener);
		}
		listView.setOnItemClickListener(this);

	}

	public Cursor get_marketing_activities() {
		Cursor cursor = db.get_marketing_activities();
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				MarketingActivities m = new MarketingActivities();
				m.campaign_date = cursor.getString(2);
				m.campaign_type = cursor.getString(1);
				m.location = cursor.getString(6);
				m.id = cursor.getString(0);

				/*
				 * Log.i("id", " " + cursor.getString(0));
				 * Log.i("campaign_type", " " + cursor.getString(1));
				 * Log.i("campaign_date", " " + cursor.getString(2));
				 * Log.i("objective", " " + cursor.getString(3));
				 * Log.i("material_required_for_ho", " " + cursor.getString(4));
				 * Log.i("other_requirment_for_branch", " " +
				 * cursor.getString(5)); Log.i("location", " " +
				 * cursor.getString(6)); Log.i("invitees", " " +
				 * cursor.getString(7)); Log.i("dimo_employees", " " +
				 * cursor.getString(8)); Log.i("no_of_employees", " " +
				 * cursor.getString(9)); Log.i("quotation", " " +
				 * cursor.getString(10));
				 */

				content.add(m);
				cursor.moveToNext();
			}
			cursor.close();
		}
		db.close();
		return cursor;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	public static class MarketingActivities {
		public String id;
		public String campaign_date;
		public String campaign_type;
		public String location;


	}

}
