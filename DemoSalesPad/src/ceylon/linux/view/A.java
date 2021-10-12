package ceylon.linux.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dimosales.R;

public class A extends Toast {
	public A(Context t, String text) {
		super(t);

		LayoutInflater inflater = (LayoutInflater) t
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View toastRoot = inflater.inflate(R.layout.toast, null);
		this.setView(toastRoot);
		TextView tk = (TextView) toastRoot.findViewById(R.id.textView1);
		tk.setText(text);
		this.setDuration(Toast.LENGTH_LONG);
		// TODO Auto-generated constructor stub
	}

	public static A MyMakeText(Context context, CharSequence text, int duration) {
		return new A(context, text.toString());
	}

	public void show() {
		super.show();
		// Your show override code, including super.show()
	}
}
