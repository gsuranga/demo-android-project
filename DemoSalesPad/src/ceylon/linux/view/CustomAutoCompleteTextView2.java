package ceylon.linux.view;

import java.util.HashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class CustomAutoCompleteTextView2 extends AutoCompleteTextView {

	public CustomAutoCompleteTextView2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Returns the country name corresponding to the selected item
	 */
	@Override
	protected CharSequence convertSelectionToString(Object selectedItem) {
		/**
		 * Each item in the autocompetetextview suggestion list is a hashmap
		 * object
		 */
		HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
		return hm.get("ID");
	}
}
