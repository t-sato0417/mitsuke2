//package com.example.androidmapview;
package com.example.fragmentact;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.example.fragmentact.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.navdrawer.SimpleSideDrawer;
//import com.navdrawer.SimpleSideDrawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

class mapcontrol{
	//mapのコントロールを担当する。
	//マップ表示のフラグメントがリセットされても直前の状態を保持できるようにする。
	
	private GoogleMap map;
	private LatLng deflatlng=new LatLng(37.531603, 138.912883);
	private float defZoom=15;
	
	private List<LatLng> routeline=new ArrayList();
	
	public void setMap(GoogleMap map){
		this.map = map;
	}
	public void onDestroy(){
		 deflatlng=map.getCameraPosition().target;
		 defZoom = map.getCameraPosition().zoom;
		 
		 //System.out.println("Lat"+deflatlng.latitude+":lng"+deflatlng.longitude);
	}
	public void CameraUpdate(){
		CameraUpdate cu = 
			CameraUpdateFactory.newLatLngZoom(
					deflatlng, defZoom);
		map.moveCamera(cu);
	}
	public void loadxml(String filename) throws SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File fp = new File("/mitsuke2/test_data.xml");
		
		Document document = documentBuilder.parse(fp);
		Element root = document.getDocumentElement();
		
		root.getAttribute("pt");
		
	}
}

public class Sub1Activity extends FragmentActivity{
	
	public SimpleSideDrawer mSlidingMenu;
	public static final String TAG = "TEST";
	static GoogleMap map;
	static int initialized =0;
	static mapcontrol mapdate=new mapcontrol();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("FRAGMENT PRINT POINT");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub1);
					
		Button btn4 = (Button) findViewById(R.id.btn4);
        Button btn5 = (Button) findViewById(R.id.btn5);
        
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Sub1Activity.this, 
                    MainActivity.class );
                startActivity(intent);
                finish(); // アクティビティ終了
            }
        });
        mSlidingMenu = new SimpleSideDrawer(this);
        mSlidingMenu.setLeftBehindContentView(R.layout.side_menu);
        btn5.setOnClickListener(new OnClickListener() {
            @Override 
            public void onClick(View v) {
            	mSlidingMenu.toggleLeftDrawer();
            }
        });
        
		map = ((SupportMapFragment)
				getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		MapsInitializer.initialize(this);
		mapdate.setMap(map);
		
		mapdate.CameraUpdate();
		
	}
	protected void onDestroy(){
		mapdate.onDestroy();
		super.onDestroy();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
		return true;*/
	}
	public void AlertBox(String title,String Message){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // アラートダイアログのタイトルを設定します
        alertDialogBuilder.setTitle(title);
        // アラートダイアログのメッセージを設定します
        alertDialogBuilder.setMessage(Message);
     // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        // アラートダイアログを表示します
        alertDialog.show();
	}
}
