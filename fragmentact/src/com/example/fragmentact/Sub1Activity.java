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
import com.google.android.gms.maps.model.PolylineOptions;
import com.navdrawer.SimpleSideDrawer;
//import com.navdrawer.SimpleSideDrawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

class RouteData{//xmlのデータ保持するクラス
	private List<LatLng> routeline=new ArrayList<LatLng>();
	
	String catecory;
	String timestamp;
	String infomation;
	
	int color=Color.BLUE;
	
	public List<LatLng> getroute(){
		return routeline;	
	}
	void setColor(int color){
		this.color=color;
	}
	public int getColor(){
		return color;
		
	}
	public void addpoint(LatLng latlng){
		routeline.add(latlng);
	}
	
	public void loadxml(String filename) throws SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		File fp = new File(filename);
		
		Document document = documentBuilder.parse(fp);
		//Element root = document.getDocumentElement();
		for(int i=0;i<document.getElementsByTagName("pt").getLength();i++){
			String ptstr=document.getElementsByTagName("pt").item(i).getTextContent();
			String strsplit[]=ptstr.split(",");
			System.out.println("debug"+ptstr);
			addpoint(new LatLng(Double.parseDouble(strsplit[0]),Double.parseDouble(strsplit[1])));
		}
	}
	public void writexml(){
		
	}
	
	
}

class mapcontrol{
	//mapのコントロールを担当する。
	//マップ表示のフラグメントがリセットされても直前の状態を保持できるようにする。
	
	private GoogleMap map;
	private LatLng deflatlng=new LatLng(37.531603, 138.912883);
	private float defZoom=15;
	
	static ArrayList<RouteData> routedata = new ArrayList<RouteData>();
	static int initialize =0;
	
	public void setMap(GoogleMap map) throws SAXException, IOException{
		System.out.println("Debug:"+"setmap");
		this.map = map;
		if(initialize==0){
			System.out.println("Debug:"+"initializde");
			routedata.add(0,new RouteData());
			
			routedata.get(0).loadxml(Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/test_data.xml");
			
			initialize=1;
		}
	}
	public void drawroute(int routeindex){
		System.out.println("test test");
		
		map.addPolyline(new PolylineOptions()
	    .addAll(routedata.get(routeindex).getroute())
	    .width(8)
	    .color(routedata.get(routeindex).getColor()));
	}
	public void onDestroy(){
		 deflatlng=map.getCameraPosition().target;
		 defZoom = map.getCameraPosition().zoom;
	}
	public void CameraUpdate(){
		CameraUpdate cu = 
			CameraUpdateFactory.newLatLngZoom(
					deflatlng, defZoom);
		map.moveCamera(cu);
		drawroute(0);
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
		try {
			mapdate.setMap(map);
		} catch (SAXException e) {
			AlertBox("ERROR","XMLファイルのパースに失敗");
		} catch (IOException e) {
			AlertBox("ERROR","ファイルが存在しない");
		}
		
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
