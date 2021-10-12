
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.example.dimosales.R;

public class A extends Toast {
	public A(Toast tt, Context t) {
		super(t);

		LayoutInflater inflater = (LayoutInflater) t
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Call toast.xml file for toast layout
		View toastRoot = inflater.inflate(R.layout.toast, null);

		this.setView(tt.getView());
		this.setDuration(tt.getDuration());
		// TODO Auto-generated constructor stub
	}

	public static A MyMakeText(Context context, CharSequence text, int duration) {
		return new A(Toast.makeText(context, text, duration), context);
	}

	public void show() {
		super.show();
		// Your show override code, including super.show()
	}
}
