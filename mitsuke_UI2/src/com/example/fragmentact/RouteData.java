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
	//xml�̃f�[�^.���s���[�g��ێ�����N���X
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
		if(routeline.size()>0){
			double latdiff=Math.abs(latlng.latitude-routeline.get(routeline.size()-1).latitude );
			double londiff=Math.abs(latlng.longitude-routeline.get(routeline.size()-1).longitude);
			if(routeline.size()>0){
				if(	!(latdiff<0.000001&&londiff<0.0000001)	)
				{
					routeline.add(latlng);
				}
			}
		}else{
			routeline.add(latlng);
		}
		//routeline.add(latlng);
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
			fp = new File(GeneralValue.cachefolder+"/"+filename);
			if(!fp.exists()){//�L���b�V���t�@�C���ɑ��݂��Ȃ��B
				//�I�����C������擾
				Sftp sftp =new Sftp();
				sftp.setSetting(GeneralValue.onlinedir+"/"+filename,
						GeneralValue.cachefolder+"/"+filename,true);
				sftp.start();
				try {
					sftp.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!fp.exists()){
					throw new IOException("ONLINE");
				}


			}
		}else{
			fp = new File(GeneralValue.savefolder+"/"+filename);
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

	public void writexml(String info,String categorytext) throws ParserConfigurationException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.newDocument();
		// <<<<< DOM�I�u�W�F�N�g�̍쐬�܂ł̂����܂�̃R�[�h
		Element route = document.createElement("route");//.createElement("test");
		document.appendChild(route);
		
		//�J�e�S���^�O�̒ǉ�
		String categorytag="other";
		if((categorytext!=null&&categorytext.length()!=0)){
			categorytag=categorytext;
		}
		
		Element category = document.createElement("category");
		route.appendChild(category);
		Text textContents = document.createTextNode(categorytag);
		category.appendChild(textContents);

		//�^�C���X�^���v�^�O�𐶐�
		Element timestamp = document.createElement("timestamp");
		route.appendChild(timestamp);
		textContents = document.createTextNode(getTimestamp());
		timestamp.appendChild(textContents);
		
		//�C���t�H���[�V�����^�O�𐶐�
		Element infomation = document.createElement("infomation");
		route.appendChild(infomation);
		if(info.length()==0){ //information�ɏ������݂��Ȃ�
			System.out.println("Debug:info:"+info);
			textContents = document.createTextNode(getTimestamp()+"�ɍ쐬����܂���");
		}else{ //information�ɏ������݂�����
			System.out.println("Debug:infomation�Ȃ�");
			textContents = document.createTextNode(info);
		}
		infomation.appendChild(textContents);

		Element latlng = document.createElement("LatLng");    	

		for(int i=0;i<routeline.size();i++){
			Element pt = document.createElement("pt");

			textContents = document.createTextNode(routeline.get(i).latitude+","+routeline.get(i).longitude);
			pt.appendChild(textContents);
			latlng.appendChild(pt);
		}
		route.appendChild(latlng);

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
		fw.close();
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
			if (d < 0) {//byte�^�ł�128�`255�����l�ɂȂ��Ă���̂ŕ␳
				d += 256;
			}
			if (d < 16) {//0�`15��16�i����1�����ɂȂ�̂ŁA2�����ɂȂ�悤����0��ǉ�
				System.out.print("0");
			}
			strdigest+=Integer.toString(d, 16);
		}
		return strdigest;
	}


}
