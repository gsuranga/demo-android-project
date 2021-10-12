package ceylon.linux.view;

import com.example.dimosales.R;
import com.example.dimosales.R.layout;
import com.example.dimosales.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RegDealerWebVw extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_dealer_web_vw);
		this.setTitle("Register Dealer");
        WebView wv;  
        wv = (WebView) findViewById(R.id.webView1);  
        WebSettings webSettings = wv.getSettings();
        wv.getSettings().setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        
        wv.setWebViewClient(new WebViewClient() {
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	          view.loadUrl(url);
        	          return true;
        	           }}); 
        //wv.loadUrl("http://gateway.ceylonlinux.com/dimo_lanka/delar/drawIndexupdatemoredetails?token_delar_name=Akpel%20(private)%20Limited&token_delar_addess=No%20:%20398/1A,%20High%20Level%20Road&token_shop_name=Akpel%20(private)%20Limited&tokendealer_id=191");
        //wv.loadUrl("http://123.231.13.186/dimo_lanka/delar/drawIndexupdatemoredetails?token_delar_name=Akpel%20(private)%20Limited&token_delar_addess=No%20:%20398/1A,%20High%20Level%20Road&token_shop_name=Akpel%20(private)%20Limited&tokendealer_id=191");
        wv.loadUrl("http://123.231.13.186/dimo_lanka/delar/drawIndexupdatemoredetails");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reg_dealer_web_vw, menu);
		return true;
	}

}
