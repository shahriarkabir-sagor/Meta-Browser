package com.dkltd.mb;

import android.os.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import androidx.appcompat.app.*;
import androidx.appcompat.widget.SearchView;
import android.util.*;
import android.transition.*;
import android.graphics.drawable.*;
import android.content.*;

public class MainActivity extends AppCompatActivity 
{
	SearchView sv;
	WebView webView;
	LinearLayout rl;
	String google,bing,yahoo,ddc,sUrl;
	String baseUrl;
	boolean isGoogle,isBing,isYahoo,isDdc;
	ProgressBar pb;
	ImageView refIconView,closeIconView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		//Custom Toolbar
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.custom_toolbar);
		//SearchEngines
		google = "https://www.google.com/search?q=";
		bing = "https://www.bing.com/search?q=";
		yahoo = "https://search.yahoo.com/search?ei=UTF-8&fr=crmas&p=";
		ddc = "https://duckduckgo.com/?q=";
		//setting all boolean false by Deafault
		isGoogle = false;
		isBing = false;
		isYahoo = false;
		isDdc = true;
		//initialize all veiw
		vInit();
		webView.setWebViewClient(new WebViewClient(){
				public void onPageStarted()
				{}
				public void onPageFinished(WebView wview, String url)
				{
					if (rl.getVisibility() == View.VISIBLE && webView.getVisibility() == View.GONE)
					{
						rl.setVisibility(View.GONE);
						webView.setVisibility(View.VISIBLE);
					}
					String newUrl = wview.getUrl();
					sUrl = newUrl;
					sv.setQuery(newUrl, false);
					if(baseUrl != null){
						sv.setQuery(baseUrl,false);
					}
				}
				public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request)
				{
					return false;
				}
			});
		webView.setWebChromeClient(new WebChromeClient(){
				public void onProgressChanged(WebView view, int progress)
				{
					progChangeFunc(view,progress);
				
				}

				private void progChangeFunc(WebView view, int progress)
				{
					if (progress < 100 && pb.getVisibility() == ProgressBar.GONE)
					{
						pb.setVisibility(ProgressBar.VISIBLE);
						refIconView.setVisibility(View.GONE);
						closeIconView.setVisibility(View.VISIBLE);
						
					}

					pb.setProgress(progress);
					if (progress == 100)
					{
						pb.setVisibility(ProgressBar.GONE);
						refIconView.setVisibility(View.VISIBLE);
						closeIconView.setVisibility(View.GONE);
					}
				}
			});
		webView.setDownloadListener(new DownloadListener(){

				@Override
				public void onDownloadStart(String p1, String p2, String p3, String p4, long p5)
				{
					// TODO: Implement this method
				}
			});
		sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
				@Override
				public boolean onQueryTextSubmit(String query)
				{
					//After submission clear focus of keyboard
					sv.clearFocus();
					//check if it is a url or not
					if (Patterns.WEB_URL.matcher(query).matches() ||
						Patterns.IP_ADDRESS.matcher(query).matches() ||
						URLUtil.isValidUrl(query) ||
						Patterns.EMAIL_ADDRESS.matcher(query).matches() ||
						Patterns.TOP_LEVEL_DOMAIN.matcher(query).matches() ||
						query.startsWith("view-source:") ||
						query.startsWith("tcp://") ||
						query.startsWith("localhost:"))
					{
						webView.loadUrl(query);

					}
					else
					{
						
						// entered text will be searched
						if (query.length() >= 1)
						{
							if (isGoogle)
							{
								isBing = false;
								isYahoo = false;
								isDdc = false;
								webView.loadUrl(google + query);
							}
							else if (isBing)
							{
								isGoogle = false;
								isYahoo = false;
								isDdc = false;
								webView.loadUrl(bing + query);
							}
							else if (isYahoo)
							{
								isGoogle = false;
								isBing = false;
								isDdc = false;
								webView.loadUrl(yahoo + query);
							}
							else if (isDdc)
							{
								isGoogle = false;
								isBing = false;
								isYahoo = false;
								webView.loadUrl(ddc + query);
							}
						}
					}
					return false;
				}

				@Override
				public boolean onQueryTextChange(String newText)
				{
					// TODO: Implement this method
					if (newText.length() >= 1)
					{

					}
					return false;
				}
			});





    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater mInflater = getMenuInflater();
		mInflater.inflate(R.menu.menu_layout,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// check which menu option is clicked
		switch(item.getItemId()){
			case R.id.settingsId:
				Toast.makeText(this,"Got id: "+item.getItemId()+" compare: "+R.id.settingsId,Toast.LENGTH_SHORT).show();
				Intent settingIntent = new Intent(MainActivity.this,mPrefereneces.class);
				startActivity(settingIntent);
				break;
			default:
			Toast.makeText(this,"Feature is not available yet",Toast.LENGTH_SHORT).show();
				Toast.makeText(this,"Wait for the next update",Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	

	private void vInit()
	{
		webView = findViewById(R.id.mainWebView);
		WebSettings ws = webView.getSettings();
		ws.setJavaScriptEnabled(true);
		//rl defining
		rl = findViewById(R.id.startPageId);
		//progressbar define
		pb = findViewById(R.id.mainProgressBarId);
		//initialize refreshIconView closeIcon
		refIconView = findViewById(R.id.refreshIconId);
		closeIconView = findViewById(R.id.closeIconId);
		//SearchView
		sv = findViewById(R.id.searchViewId);
	}
	public void onBrFtrClick(View v)
	{
		switch (v.getId())
		{
			case R.id.home_iconId:
				if (webView.getVisibility() == View.VISIBLE && rl.getVisibility() == View.GONE)
				{
					webView.setVisibility(View.GONE);
					rl.setVisibility(View.VISIBLE);
					sv.setQuery("",false);
					sUrl = null;
				}
				break;
			case R.id.backwardIconId:
				if (webView.canGoBack())
				{
					webView.goBack();
				}
				else if (webView.getVisibility() == View.VISIBLE && rl.getVisibility() == View.GONE)
				{
					webView.setVisibility(View.GONE);
					rl.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.forwardIconId:
				if (webView.canGoForward())
				{
					webView.goForward();
				}
				break;
			case R.id.refreshIconId:
				if(webView.getVisibility() == View.VISIBLE && rl.getVisibility() == View.GONE){
					webView.loadUrl(webView.getUrl().toString());
				}
				break;
			case R.id.closeIconId:
				webView.stopLoading();
				break;
			case R.id.inspectIconId:
				if(sUrl!=null){
					Intent i = new Intent(MainActivity.this,SEActivity.class);
					i.putExtra("url",sUrl);
					startActivityForResult(i,1);
				}
				break;
			default:
				Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO: Implement this method
		super.onActivityResult(requestCode, resultCode, data);
		//check result code validity and not empty
		if(requestCode == 1 && data != null){
			//Get code from previous activity
			String unEncodedHtml = data.getStringExtra("code");
			baseUrl = data.getStringExtra("baseurl");
			String historyUrl = baseUrl;
			//check if returned code is not empty
			if(unEncodedHtml.isEmpty()){
				sv.setQuery(baseUrl,true);
			}else{
			//Now load new code to webView
			webView.loadDataWithBaseURL(baseUrl,unEncodedHtml,"text/html","utf-8",historyUrl);
			sv.setQuery(baseUrl,false);
			}
		} 
	}
	
	
	
	@Override
	public void onBackPressed()
	{
		if (webView.canGoBack())
		{
			webView.goBack();
		}
		else
		{
			exitCondition();
		}

	}

	private void exitCondition()
	{
		if (webView.getVisibility() == View.VISIBLE && rl.getVisibility() == View.GONE)
		{
			webView.setVisibility(View.GONE);
			rl.setVisibility(View.VISIBLE);
		}
		else
		{
			super.onBackPressed();
		}

	}

}
