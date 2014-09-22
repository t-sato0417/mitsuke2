package com.example.fragmentact;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import android.graphics.Color;
import android.os.Environment;

import com.google.android.gms.maps.model.LatLng;


public class RouteData{
	//xmlのデータ.歩行ルートを保持するクラス
	private List<LatLng> routeline=new ArrayList<LatLng>();
	
	String category="";
	String timestamp="";
	String infomation="";
	
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
	public int getPoint(){
		return routeline.size();
	}
	public void addpoint(LatLng latlng){
		try{
		if(	(routeline.get(routeline.size()-1).latitude-latlng.latitude>=0.0001)||
			(routeline.get(routeline.size()-1).longitude-latlng.longitude>=0.0001)
				){
			routeline.add(latlng);
		}
		}catch(ArrayIndexOutOfBoundsException e){
			//System.out.println("Debug:Arryout");
			routeline.add(latlng);
		}
		//System.out.println("Debug:numpoint"+routeline.size());
	}
	
	public void loadxml(String filename) throws SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		File fp = new File(Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/"+filename);
		
		Document document = documentBuilder.parse(fp);
		//Element root = document.getDocumentElement();
		for(int i=0;i<document.getElementsByTagName("pt").getLength();i++){
			String ptstr=document.getElementsByTagName("pt").item(i).getTextContent();
			String strsplit[]=ptstr.split(",");
			System.out.println("debug"+ptstr);
			addpoint(new LatLng(Double.parseDouble(strsplit[0]),Double.parseDouble(strsplit[1])));
		}
	}
	public void writexml(String filename) throws ParserConfigurationException, IOException{
		System.out.println("Debug:Writexml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.newDocument();
	    
	    Element root = document.createElement("route");//.createElement("test");
	    document.appendChild(root);
	    
	    Element timestamp = document.createElement("timestamp");
        root.appendChild(timestamp);
        Text textContents1 = document.createTextNode(this.timestamp);
        timestamp.appendChild(textContents1);
        
        Element category = document.createElement("category");
        root.appendChild(category);
        Text textContents2 = document.createTextNode(this.category);
        timestamp.appendChild(textContents2);
        
        Element infomation = document.createElement("infomation");
        root.appendChild(infomation);
        Text textContents3 = document.createTextNode(this.infomation);
        timestamp.appendChild(textContents3);
        
        Element latlng = document.createElement("LatLng");
        root.appendChild(latlng);
        for(int i=0;i<routeline.size();i++){
        	Element pt = document.createElement("pt");
        	latlng.appendChild(pt);
        	Text textContents4 = document.createTextNode(
        			routeline.get(i).latitude +"," + routeline.get(i).longitude);
        	pt.appendChild(textContents4);
        }
        
	    StringWriter sw = new StringWriter();
	    TransformerFactory tfactory = TransformerFactory.newInstance(); 
	    Transformer transformer = null;
		try {
			transformer = tfactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    try {
			transformer.transform(new DOMSource(document), new StreamResult(sw));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	   
	    System.out.println("Debug:FileWriter");
	    //System.out.println("Debug:"+sw.toString());
	    //System.out.println("Dubug:Path:"+Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/"+filename);
	    File fp = new File(Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/"+filename);
	    FileWriter fw =new FileWriter(fp);
	    fw.write(sw.toString());
	    fw.close();
	    //FileReader fr =new FileReader(Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/"+"sample.xml");
	    //System.out.println("Debug:sample.xml:");
	}
	
	
}
