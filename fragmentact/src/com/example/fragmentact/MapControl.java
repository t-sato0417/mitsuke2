package com.example.fragmentact;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.graphics.Color;
import android.os.Environment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapControl {
	//map�̃R���g���[����S������B
	//Sub1Activity��static�ŕێ����邽�߂̃N���X
	//�}�b�v�\���̃t���O�����g�����Z�b�g����Ă����O�̏�Ԃ�ێ��ł���悤�ɂ���B
	//routedata.get(0)��GPS���K�[�p
	
	private GoogleMap map;//
	private LatLng deflatlng=new LatLng(37.531603, 138.912883);
	private float defZoom=15;
	
	static ArrayList<RouteData> routedata = new ArrayList<RouteData>();//���[�g(���C���f�[�^)�̕ێ�
	
	static int initialize =0;//��x�݂̂̏����������ɗ��p����i0����@1����ȍ~�j
	
	public void addpoint(LatLng latlng){
		routedata.get(0).addpoint(latlng);
	}
	public void writexml() throws ParserConfigurationException, IOException{
		routedata.get(0).writexml("example.xml");
	}
	public void loadxml(String path) throws SAXException, IOException{
		int i=1;
		try{
			for(i=0;i<10;i++){

				routedata.get(i);

			}
		}catch(IndexOutOfBoundsException e){
			if(i==0){
				System.out.println("Debug:GPS�p�̃��[�g�f�[�^������");
			}
			routedata.add(i,new RouteData());
			routedata.get(i).loadxml(path);
			routedata.get(1).setColor(Color.RED);
		}
	}

	public void setMap(GoogleMap map) throws SAXException, IOException{
		System.out.println("Debug:"+"setmap");
		this.map = map;
		if(initialize==0){
			System.out.println("Debug:"+"initializde");
			routedata.add(0,new RouteData());
			routedata.add(1,new RouteData());
			routedata.get(1).loadxml("test_data.xml");
			routedata.get(1).setColor(Color.RED);
			initialize=1;
		}
	}
	public void drawroute(int routeindex){
		
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
		drawroute(1);
	}
}