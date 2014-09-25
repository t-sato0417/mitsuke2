package com.example.fragmentact;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

public class Sub3Activity extends FragmentActivity {
	int wc = ViewGroup.LayoutParams.WRAP_CONTENT; 
	int array;
	Node connector;
	NamedNodeMap attributes ;
	Element file;
	Document document;



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub3);
		System.out.println("Debug:ActivitySub3");
		try {
			initButton();
		} catch (SAXException e) {
			AlertBox("ERROR","XMLファイルがおかしい");
		} catch (IOException e) {
			AlertBox("ERROR","XMLファイルがみつかりません");
		} catch (ParserConfigurationException e) {
			AlertBox("ERROR","XMLファイルがおかしい");
		}
	}

	public void localfilebutton() throws SAXException, IOException,
	ParserConfigurationException {
		

	}
	public void initButton() throws SAXException, IOException,
	ParserConfigurationException {

		System.out.println("Debug:initBUtton");
		Intent intent = getIntent();



		//Sub2Activityから値を受け取る
		//CharSequence getstring = intent.getCharSequenceExtra("CATEGORY");
		String category=intent.getStringExtra("CATEGORY");
		if(category.equalsIgnoreCase("local")){
			localfilebutton();
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		File fp = new File(Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/manage_test.xml");
		document = documentBuilder.parse(fp);

		Element root = document.getDocumentElement();			
		file = document.getDocumentElement();

		//int max =infotags.getLength();
		//List<Button> button = new ArrayList<Button>();
		LinearLayout lla = new LinearLayout(this);
		lla.setOrientation(LinearLayout.VERTICAL);
		setContentView(lla);

		Node eat_out =document.getElementsByTagName(category).item(0);
		for(array=0;array<document.getElementsByTagName("file").getLength();array++){
			/*System.out.println("Debug:file:"+
	        			document.getElementsByTagName("file").item(array).getNodeValue());*/
			if(document.getElementsByTagName("file").item(array).getParentNode()==eat_out){
				//System.out.println(document.getElementsByTagName("file").item(array).getAttributes().getNamedItem("info").getTextContent());
				Button insertbutton =new Button(this);
				insertbutton.setText(document.getElementsByTagName("file").item(array).getAttributes().getNamedItem("info").getNodeValue());

				insertbutton.setOnClickListener(new OnClickListener() {
					int number=array;
					String filename=document.getElementsByTagName("file").item(array).getAttributes().getNamedItem("fname").getNodeValue();

					public void onClick(View view){			            	
						System.out.println("Debug:filename:"+filename);		
						Intent intent = new Intent(Sub3Activity.this,
								Sub1Activity.class );
						intent.putExtra("FILENAME", filename);
						startActivity(intent);
					}
				});
				lla.addView(insertbutton, new LinearLayout.LayoutParams(wc, wc));
			}
		}



	}
	void getFile(int number){
		Intent intent = new Intent(Sub3Activity.this,
				Sub1Activity.class );
		if(file!=null){
			System.out.println("Debug:"+
					document.getElementsByTagName("file").item(number).getAttributes().getNamedItem("fname").getNodeValue());
			intent.putExtra("FILENAME",
					document.getElementsByTagName("file").item(number)
					.getAttributes().getNamedItem("fname").getNodeValue());
			startActivity(intent);

		}
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
