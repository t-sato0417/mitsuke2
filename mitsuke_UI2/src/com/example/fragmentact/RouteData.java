package com.example.fragmentact;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.example.sshconnect.Sftp;
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
	
	public void loadxml(String filename,boolean online) throws SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		File fp=null;
		if(online){
			fp = new File(GeneralValue.cachefolder+filename);
			if(!fp.exists()){//キャッシュファイルに存在しない。
				//オンラインから取得
				Sftp sftp =new Sftp();
				sftp.setSetting(GeneralValue.onlinedir+"/"+online,
						GeneralValue.cachefolder+"/"+filename,true);
				sftp.start();
				if(!fp.exists()){
					throw new IOException("ONLINE");
				}
				
			}
		}else{
			fp = new File(GeneralValue.savefolder+filename);
		}

		Document document = documentBuilder.parse(fp);
		//Element root = document.getDocumentElement();
		for(int i=0;i<document.getElementsByTagName("pt").getLength();i++){
			String ptstr=document.getElementsByTagName("pt").item(i).getTextContent();
			String strsplit[]=ptstr.split(",");
			System.out.println("debug"+ptstr);
			addpoint(new LatLng(Double.parseDouble(strsplit[0]),Double.parseDouble(strsplit[1])));
		}
	}
	public String getTimestamp(){
		Calendar now = Calendar.getInstance();
		//2014-09-17 17:26:30
		return String.format("%d-%02d-%02d %02d:%02d:%02d",
				now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DATE),
				now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),now.get(Calendar.SECOND)
				);
	}
	
	public void writexml(String info) throws ParserConfigurationException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.newDocument();
	    // <<<<< DOMオブジェクトの作成までのおきまりのコード
	    Element root = document.createElement("route");//.createElement("test");
	    document.appendChild(root);
	    
	    Element timestamp = document.createElement("timestamp");
        root.appendChild(timestamp);
        Text textContents = document.createTextNode(getTimestamp());
        timestamp.appendChild(textContents);
        
        Element infomation = document.createElement("infomation");
        if(info.length()==0){
        	System.out.println("Debug:info:"+info);
        	textContents = document.createTextNode(getTimestamp()+"に作成されました");
        }else{
        	System.out.println("Debug:infomationなし");
        	textContents = document.createTextNode(info);
        }
        infomation.appendChild(textContents);
        root.appendChild(infomation); 
        
        Element latlng = document.createElement("LatLng");    	
               
        for(int i=0;i<routeline.size();i++){
        	Element pt = document.createElement("pt");
        	
        	textContents = document.createTextNode(routeline.get(i).latitude+","+routeline.get(i).longitude);
        	pt.appendChild(textContents);
        	latlng.appendChild(pt);
        }
        root.appendChild(latlng);
        
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
	    
	    String fname=randmd5();

	    System.out.println("Debug:savexmlfile:"+GeneralValue.savefolder+"/"+fname+".xml");
		FileWriter fw=new FileWriter(GeneralValue.savefolder+"/"+fname+".xml");
		fw.write(sw.toString());	    
	}
	public String randmd5(){
		double hashseed=Math.random();
		MessageDigest md=null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] dat = (""+hashseed).getBytes();;
		md.update(dat);
		String fname="";
		String strdigest="";

		byte []digest = md.digest();
		for (int i = 0; i < digest.length; i++) {
			int d = digest[i];
			if (d < 0) {//byte型では128～255が負値になっているので補正
				d += 256;
			}
			if (d < 16) {//0～15は16進数で1けたになるので、2けたになるよう頭に0を追加
				System.out.print("0");
			}
			strdigest+=Integer.toString(d, 16);
		}
		return strdigest;
	}
	
	
}
