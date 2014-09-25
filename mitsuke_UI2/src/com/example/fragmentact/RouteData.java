package com.example.fragmentact;

import java.io.File;
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

import com.google.android.gms.maps.model.LatLng;


public class RouteData{
	//xmlのデータ.歩行ルートを保持するクラス
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
	public void writexml() throws ParserConfigurationException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.newDocument();
	    // <<<<< DOMオブジェクトの作成までのおきまりのコード
	    Element root = document.createElement("route");//.createElement("test");
	    document.appendChild(root);
	    
	    Element timestamp = document.createElement("timestamp");
        root.appendChild(timestamp);
        Text textContents = document.createTextNode("255525");
        timestamp.appendChild(textContents);
        
        
	    
	    
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
	    System.out.println( sw.toString());
	}
	
	
}
