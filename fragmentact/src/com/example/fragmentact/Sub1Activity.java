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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;




public class Sub1Activity extends FragmentActivity 
	implements OnConnectionFailedListener, LocationListener, ConnectionCallbacks{
	
	public SimpleSideDrawer mSlidingMenu;
	public static final String TAG = "TEST";
	static GoogleMap map;
	static int initialized =0;
	static MapControl mapdata=new MapControl();	
	
	Intent calledintent;//呼び出し元のintentを代入する
	
	private LocationClient mLocationClient = null;
	private static final LocationRequest REQUEST = LocationRequest.create()
	.setInterval(5000) // 5 seconds
	.setFastestInterval(16) // 16ms = 60fps
	.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub1);
					
		Button btn4 = (Button) findViewById(R.id.btn4);
        Button btn5 = (Button) findViewById(R.id.btn5);
        
        calledintent = getIntent();
        String keyword = calledintent.getStringExtra(“D”);
        
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
			mapdata.setMap(map);
		} catch (SAXException e) {
			AlertBox("ERROR","XMLファイルのパースに失敗");
		} catch (IOException e) {
			AlertBox("ERROR","ファイルが存在しない");
		}
		
		mapdata.CameraUpdate();

		if (map != null) {
			map.setMyLocationEnabled(true);
		}
		mLocationClient = new LocationClient(getApplicationContext(), this, this); // ConnectionCallbacks, OnConnectionFailedListener
		if (mLocationClient != null) {
			// Google Play Servicesに接続
			mLocationClient.connect();
		}

	}
	protected void onDestroy(){
		mapdata.onDestroy();
		super.onDestroy();
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
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// 現在地に移動
		CameraPosition cameraPos = new CameraPosition.Builder()
		.target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(15.0f)
		.bearing(0).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
		mapdata.addpoint(new LatLng(location.getLatitude(), location.getLongitude()));
		mapdata.drawroute(0);
	}


	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		mLocationClient.requestLocationUpdates(REQUEST,this); // LocationListener
	}
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
