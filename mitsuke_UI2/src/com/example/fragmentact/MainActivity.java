package com.example.fragmentact;

//import com.example.androidmapview.Sub1Activity;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends FragmentActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //フォルダを作成
        File approot=new File(GeneralValue.approot);
        approot.mkdir();
        File xmlfolder = new File(GeneralValue.savefolder);
        xmlfolder.mkdir();
        File cachefolder = new File(GeneralValue.cachefolder);
        cachefolder.mkdir();
        
 
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);
 
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                    Sub1Activity.class );
                startActivity(intent);
            }
        });
 
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                    Sub2Activity.class );
                startActivity(intent);
            }
        });
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		//System.out.println("clicked");
	   // DOWNとUPが取得できるのでログの2重表示防止ためif
	   if (e.getKeyCode() == KeyEvent.KEYCODE_MENU) {
	       //キーコード表示
	       System.out.println("KeyCode:"+ e.getKeyCode());
	   }else 
	   if (e.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	       //キーコード表示
		   System.out.println("KeyCode:"+ e.getKeyCode());
		   //return true;
	   }
	 
	   return super.dispatchKeyEvent(e);
	}
}
