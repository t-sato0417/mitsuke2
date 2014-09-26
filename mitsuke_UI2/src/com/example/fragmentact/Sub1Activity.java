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

import jp.ne.docomo.smt.dev.common.exception.SdkException;
import jp.ne.docomo.smt.dev.common.exception.ServerException;
import jp.ne.docomo.smt.dev.common.http.AuthApiKey;
import jp.ne.docomo.smt.dev.environmentsensor.EnvironmentSensor;
import jp.ne.docomo.smt.dev.environmentsensor.data.EnvironmentObservationData;
import jp.ne.docomo.smt.dev.environmentsensor.data.EnvironmentSensorData;
import jp.ne.docomo.smt.dev.environmentsensor.data.EnvironmentSensorResultData;
import jp.ne.docomo.smt.dev.environmentsensor.param.EnvironmentSensorRequestParam;

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
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




public class Sub1Activity extends FragmentActivity 
	implements OnConnectionFailedListener, LocationListener, ConnectionCallbacks{
	
	public SimpleSideDrawer mSlidingMenu;
	public static final String TAG = "TEST";
	static GoogleMap map;
	static int initialized =0;
	static MapControl mapdata=new MapControl();
	
	static final String APIKEY = "4874496b63633061385766326e2e487a4e456f665a31636131647157632e4d7a386e6838786f77715a4c30";
	
	TextView tempview;
	
	private LocationClient mLocationClient = null;
	private static final LocationRequest REQUEST = LocationRequest.create()
	.setInterval(5000) // 5 seconds
	.setFastestInterval(16) // 16ms = 60fps
	.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub1);
		
		tempview = (TextView)findViewById(R.id.tempview);
		
		Intent parentIntent = getIntent();
		String loadfilename=parentIntent.getStringExtra("FILENAME");
		boolean iflocalfile = parentIntent.getBooleanExtra("LOCALFILE",false);
		
		Button btn4 = (Button) findViewById(R.id.btn4);
        Button btn5 = (Button) findViewById(R.id.btn5);
        
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Sub1Activity.this, 
                    MainActivity.class );
                startActivity(intent);
                //finish(); // アクティビティ終了
            }
        });
        mSlidingMenu = new SimpleSideDrawer(this);
        mSlidingMenu.setLeftBehindContentView(R.layout.side_menu);
        btn5.setOnClickListener(new OnClickListener() {
            @Override 
            public void onClick(View v) {
            	mapdata.setGPS(true);
            	mapdata.changeGPS(mLocationClient.getLastLocation());
            	mSlidingMenu.toggleLeftDrawer();
            	mapdata.setGPS(false);
            	Button bihindbutton = (Button) findViewById(R.id.behind_btn);
            	bihindbutton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	EditText infotext = (EditText)findViewById(R.id.editText1);
                    	
                        try {
                        	SpannableStringBuilder sb = (SpannableStringBuilder)infotext.getText();
							mapdata.writexml(sb.toString());
							Toast.makeText(Sub1Activity.this,"ファイルを保存しました",Toast.LENGTH_LONG).show();
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							AlertBox("ERROR","ファイルの書き込みに失敗");
						}
                    }
                });
            }
        });
        
		map = ((SupportMapFragment)
				getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		MapsInitializer.initialize(this);
		try {
			mapdata.setMap(map);
			if(loadfilename!=null){
				mapdata.loadxml(loadfilename,!iflocalfile);
				
			}else{
				mapdata.setGPS(true);
			}
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
		
		
		// API キーの登録
		AuthApiKey.initializeAuth(APIKEY);	
		
		
		
		
		//Location myLocate = mLocationClient.getLastLocation();
	}
	protected void onDestroy(){
		mapdata.onDestroy();
		super.onDestroy();
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
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
	int count =0;
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// 現在地に移動

		if(count%212==0){
			EnvironmentSensorRequestParam requestParam = new EnvironmentSensorRequestParam();
			SensorAsyncTask task = new SensorAsyncTask(new AlertDialog.Builder(this));
			requestParam.setLat(mLocationClient.getLastLocation().getLatitude());
			requestParam.setLon(mLocationClient.getLastLocation().getLongitude());
			requestParam.setDataType("1013");//1213降水量
			requestParam.setWithData("1213");

			task.execute(requestParam);	
		}
		mapdata.changeGPS(location);
	}


	
	private class SensorAsyncTask extends AsyncTask<EnvironmentSensorRequestParam, Integer, EnvironmentSensorResultData> {
	private AlertDialog.Builder _dlg;
	
	private boolean isSdkException = false;
	private String exceptionMessage = null;
 
    public SensorAsyncTask(AlertDialog.Builder dlg) {
        super();
        _dlg = dlg;
    }
 
	@Override
	protected EnvironmentSensorResultData doInBackground(EnvironmentSensorRequestParam... params) {
		EnvironmentSensorResultData resultData = null;
		EnvironmentSensorRequestParam req_param = params[0];
		try {
			
			// リクエストを実行する
			EnvironmentSensor environment = new EnvironmentSensor();
			resultData = environment.request(req_param);

		
		} catch (SdkException ex) {
			isSdkException = true;
			exceptionMessage = "ErrorCode: " + ex.getErrorCode() + "\nMessage: " + ex.getMessage();
			
		} catch (ServerException ex) {
			exceptionMessage = "ErrorCode: " + ex.getErrorCode() + "\nMessage: " + ex.getMessage();
		}
		
		return resultData;
    }
	
	@Override
	protected void onCancelled() {
	}
	
    @Override
    protected void onPostExecute(EnvironmentSensorResultData resultData) {
		StringBuffer sb = new StringBuffer();
		
    	if(resultData == null){
    		if(isSdkException){
    			_dlg.setTitle("SdkException 発生");
   			
    		}else{
    			_dlg.setTitle("ServerException 発生");
    		}
			_dlg.setMessage(exceptionMessage + " ");
			_dlg.show();

    	}else{
    		
			// 結果表示
			ArrayList<EnvironmentSensorData> sensorList = resultData.getEnvironmentSensorDataList();
			if (sensorList != null) {
				for (EnvironmentSensorData sensorData : sensorList) {
					//sb.append("拠点ID : " + sensorData.getId() +"\n");
					//sb.append("名称 : " + sensorData.getName() +"\n");
					sb.append("都道府県 : " + sensorData.getPrefecture() +"\n");
					sb.append("市区町村 : " + sensorData.getCity() +"\n");
					//sb.append("緯度 : " + sensorData.getLat() +"\n");
					//sb.append("経度 : " + sensorData.getLon() +"\n");
					ArrayList<EnvironmentObservationData> dataList = sensorData.getEnvironmentObservationDataList();
					if (dataList == null) continue;
					for (EnvironmentObservationData data : dataList) {
						//sb.append("　　環境データ種別 : " + data.getDataType() +"\n");
						//sb.append("　　観測日時 : " + data.getObsDateTime() +"\n");
						ArrayList<String> valList = data.getValList();
						for (String val : valList) {
							sb.append("　　気温 : " + val +"度\n");
						}
					}
				}
			}
			tempview.setText(sb.toString());
			//System.out.println("Debug:sb:"+sb.toString());
			//tempview.requestFocus();

			/*// ソフトキーボードを非表示にする
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(temp.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);*/
    	}
    }
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
