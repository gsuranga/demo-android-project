package ceylon.linux.controller;

import com.example.dimosales.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class CusAlertDialog {

	private Button btn_ok, btn_cancel;
	private TextView textmsg,txt_title;
	private Dialog dialog;
	private Context context;

	public CusAlertDialog(Context context) {

		this.context = context;
		dialog = new Dialog(context);
		 dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
		dialog.setContentView(R.layout.mydialoglayout);
		dialog.setCanceledOnTouchOutside(false);

		 setBtn_ok((Button) dialog.findViewById(R.id.btn_ok));
		 setBtn_cancel((Button) dialog.findViewById(R.id.btn_cancel));
		 textmsg = (TextView) dialog.findViewById(R.id.textmsg);

		 /////////////////////////////////////////////////////////////
	

		
            
		  dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titleview); 
		  View vv=  dialog.getWindow().getDecorView();
		  txt_title = (TextView) vv.findViewById(R.id.title);
		
		// TODO Auto-generated constructor stub
	}

	public void setMessage(CharSequence massage) {
		textmsg.setText(massage);
	}
	
	public void setPositiveButtonName(CharSequence massage)
	{
		btn_ok.setText(massage);
	}
	
	public void setNegitiveButtonName(CharSequence massage)
	{
		btn_cancel.setText(massage);
	}

	public void show() {
		dialog.show();
	}

	public Button getBtn_ok() {
		return btn_ok;
	}

	public void setBtn_ok(Button btn_ok) {
		this.btn_ok = btn_ok;
	}

	public Button getBtn_cancel() {
		return btn_cancel;
	}

	public void setBtn_cancel(Button btn_cancel) {
		this.btn_cancel = btn_cancel;
	}
	
	public void dismiss()
	{
		dialog.dismiss();
	}
	public void setTitle(CharSequence title)
	{
		txt_title.setText(title);
	}
}
