package com.dkltd.mb;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.*;
import android.os.*;
import org.jsoup.nodes.*;
import org.jsoup.*;
import java.io.*;
import android.view.*;
import android.widget.*;
import android.content.*;

public class SEActivity extends AppCompatActivity 
{
	String gUrl;
	EditText codeEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.se_layout);
		//Custom Toolbar
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.toolbar_two);
		//get url from intent
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			gUrl = bundle.getString("url");
		}
		//initialize code editor
		codeEditor = findViewById(R.id.codeEditorId);
		//Execute parser
		new ParseHtml().execute();
		
    }
	public void onInjectOptionClick(View v1){
		switch(v1.getId()){
			case R.id.backIcon:
				super.onBackPressed();
				break;
			case R.id.injectIconId:
				String code = codeEditor.getText().toString();
				Intent i2 = new Intent(SEActivity.this,MainActivity.class);
				i2.putExtra("code",code);
				i2.putExtra("baseurl",gUrl);
				setResult(1,i2);
				finish();
				break;
			default: 
				break;
		}
	}
	
	
	//ParseHtml for editing
	private class ParseHtml extends AsyncTask<Void, Void, Void>
	{
		String url,code;

		@Override
		protected void onPreExecute()
		{
			// TODO: Implement this method
			super.onPreExecute();
			if(gUrl!=null){
				url = gUrl;
			}
			
			
		}
		
		

		@Override
		protected Void doInBackground(Void[] p1)
		{
			// TODO: Implement this method
			try
			{
				Document doc = Jsoup.connect(url).get();
				code = doc.toString();
			}
			catch (IOException e)
			{}

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			// TODO: Implement this method
			super.onPostExecute(result);
			codeEditor.setText(code);
			
		}

		
		
		
	}
}
