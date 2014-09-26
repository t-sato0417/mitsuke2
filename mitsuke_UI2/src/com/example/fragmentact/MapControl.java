package com.example.fragmentact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapControl {
	//mapのコントロールを担当する。
	//Sub1Activityがstaticで保持するためのクラス
	//マップ表示のフラグメントがリセットされても直前の状態を保持できるようにする。
	//routedata.get(0)はGPSロガー用
	
	private GoogleMap map;//
	private LatLng deflatlng=new LatLng(37.531603, 138.912883);
	private float defZoom=16;
	private boolean onGPS=false;
	
	private boolean onEditmode=false;
	private int		beginratio=0; // point / 1000=
	private int		endratio=1000;
	
	static ArrayList<RouteData> routedata = new ArrayList<RouteData>();//ルート(ラインデータ)の保持
	static List<LatLng> selectroute=new ArrayList<LatLng>();
	
	static int initialize =0;//一度のみの初期化処理に利用する（0初回　1初回以降）
	
	public void setBeginpoint(int point){
		this.beginratio=point;
		selectroute.removeAll(selectroute);
		drawrouteall();
	}
	public void setEndpoint(int point){
		this.endratio=point;
		selectroute.removeAll(selectroute);
		drawrouteall();
	}
	public void onEditmode(boolean on){}
	
	public void addpoint(LatLng latlng){
		routedata.get(0).addpoint(latlng);
	}
	public void setGPS(boolean on){
		onGPS=on;
		
	}
	public void changeGPS(Location location){
		if(onGPS){
			
			CameraPosition cameraPos = new CameraPosition.Builder()
			.target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(map.getCameraPosition().zoom)
			.bearing(0).build();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
			
		}
		addpoint(new LatLng(location.getLatitude(), location.getLongitude()));
		drawrouteall();
	}
	int colorlist[]={Color.RED,Color.BLACK,Color.BLUE,Color.CYAN,Color.DKGRAY,Color.GRAY,
			Color.GREEN,Color.LTGRAY,Color.MAGENTA,Color.WHITE};
	public void loadxml(String filename,boolean online) throws SAXException, IOException{
		int i=1;
		try{
			for(i=0;i<10;i++){
				routedata.get(i);
			}
		}catch(IndexOutOfBoundsException e){
			if(i==0){
				System.out.println("Debug:GPS用のルートデータが無い");
			}
			routedata.add(i,new RouteData());
			routedata.get(i).loadxml(filename,online);
			routedata.get(1).setColor(colorlist[(new Random()).nextInt(colorlist.length)]);
			setGPS(false);
			List<LatLng> route= routedata.get(i).getroute();
			CameraUpdate cu = 
					CameraUpdateFactory.newLatLngZoom(
							route.get(route.size()/4), defZoom);
				map.moveCamera(cu);
			
		}
		drawrouteall();
	}
	void writexml(String info) throws ParserConfigurationException, IOException{
		routedata.get(0).writexml(info);
	}
	public void setMap(GoogleMap map) throws SAXException, IOException{
		System.out.println("Debug:"+"setmap");
		this.map = map;
		if(initialize==0){
			System.out.println("Debug:"+"initializde");
			routedata.add(0,new RouteData());
			
			initialize=1;
		}
	}
	public void debug_setGPS(){
		LatLng latlng = routedata.get(0).getroute().get(routedata.get(0).getroute().size()-1);
		addpoint(new LatLng(latlng.latitude+0.00002,latlng.longitude));
	}
	public void drawroute(int routeindex){
		
		map.addPolyline(new PolylineOptions()
	    .addAll(routedata.get(routeindex).getroute())
	    .width(15)
	    .color(routedata.get(routeindex).getColor()));
		if(routeindex==0&&onEditmode){
			int beginpoint=(int) Math.floor((double)routedata.get(routeindex).getroute().size()*((double)beginratio/1000.0));
			int endpoint=(int) Math.floor((double)routedata.get(routeindex).getroute().size()*((double)endratio/1000.0));
			int max=Math.max(beginpoint, endpoint);
			int min=Math.min(beginpoint, endpoint);
			
			for(int i=min;i<max;i++){
				selectroute.add(routedata.get(routeindex).getroute().get(i));
			}
			map.addPolyline(new PolylineOptions()
		    .addAll(selectroute)
		    .width(8)
		    .color(Color.YELLOW));
		}
		
	}
	public void drawrouteall(){
		for(int i=0;i<routedata.size();i++){
			drawroute(i);
			
		}
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
		//drawroute(0);
		//drawroute(1);
	}
}