package com.example.fragmentact;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

import com.example.sshconnect.Sftp;

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
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

public class Sub3Activity extends FragmentActivity {
	int wc = ViewGroup.LayoutParams.WRAP_CONTENT; 
	int array;
	Node connector;
	NamedNodeMap attributes ;
	Element file;
	Document document;
	
	//TextView subtitle;
	String loadfile;
	LinearLayout lla;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub3);
		
		//subtitle = (TextView)findViewById(R.id.textView1);
		lla = (LinearLayout)findViewById(R.id.LinearLayout);//new LinearLayout(this);
		
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

	public void localfilebutton(){
		//subtitle.setText("作成されたルートファイル");
		System.out.println("Debug:localfilebuton");
		
		
		File folder = new File(GeneralValue.savefolder);
		String filelist[] =folder.list();
		System.out.println("Debug:filelist:"+filelist[0]);
		
		
		for(int i=0;i<filelist.length;i++){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = null;
			String info=null;
			try {
				documentBuilder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				info =filelist[i];
			}
			File fp = new File(GeneralValue.savefolder+"/"+filelist[i]);
			
			try {
				document = documentBuilder.parse(fp);
			} catch (SAXException e) {
				info =filelist[i];
			} catch (IOException e) {
				info =filelist[i];
			}
			Element root = document.getDocumentElement();			
			file = document.getDocumentElement();
			try{
			info = document.getElementsByTagName("infomation").item(0).getTextContent();
			}catch(Exception e){
				info =filelist[i];
			}
			Button insertbutton =new Button(this);
			insertbutton.setText(info);
			final String fname=filelist[i];
			insertbutton.setOnClickListener(new OnClickListener() {
				int number=array;
				String filename=fname;
				
				public void onClick(View view){			            	
					System.out.println("Debug:filename:"+filename);
					loadfile = filename;
					saveorview();					
				}
			});
			lla.addView(insertbutton, new LinearLayout.LayoutParams(wc, wc));
		}
		
		

	}
	public void initButton() throws SAXException, IOException,
	ParserConfigurationException {
		System.out.println("Debug:initButton");
		Intent intent = getIntent();

		//Sub2Activityから値を受け取る
		//CharSequence getstring = intent.getCharSequenceExtra("CATEGORY");
		String category=intent.getStringExtra("CATEGORY");
		if(category.equalsIgnoreCase("local")){
			localfilebutton();
			return ;
		}
		//subtitle.setText("カテゴリーのファイル");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		File fp = new File(GeneralValue.approot+"/manage.xml");
		
		//System.out.println("Debug:manage.xmlが存在しない");
		Sftp sftp = new Sftp();
		System.out.println("Debug:AccessFIle:"+GeneralValue.onlinedir+"/manage.xml");
		sftp.setSetting(GeneralValue.onlinedir+"/manage.xml",GeneralValue.approot+"/manage.xml",true);
		sftp.start();
		try {
			sftp.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		document = documentBuilder.parse(fp);

		

		//int max =infotags.getLength();
		//List<Button> button = new ArrayList<Button>();

		Node eat_out =document.getElementsByTagName(category).item(0);
		for(array=0;array<document.getElementsByTagName("file").getLength();array++){
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
						intent.putExtra("LOCALFILE",false);
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
	public void fileinfo(String info,String timestamp){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// アラートダイアログのタイトルを設定します
		alertDialogBuilder.setTitle("ファイル選択");
		// アラートダイアログのメッセージを設定します
		alertDialogBuilder.setMessage("");
		// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
		alertDialogBuilder.setPositiveButton("ルートを閲覧",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				intentsub1();
			}
		});
		alertDialogBuilder.setNeutralButton("ルートを共有",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//intentsub1();
				Sftp sftp=new Sftp();
				sftp.setSetting(GeneralValue.savefolder+"/"+loadfile,GeneralValue.onlinedir+"/"+loadfile, false);
				sftp.start();
			}
		});
		alertDialogBuilder.setNegativeButton("削除",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("Debug:Deletefile:"+GeneralValue.savefolder+"/"+loadfile);
				//deleteFile(GeneralValue.approot+"/"+loadfile);
				
				File deleteFile = new File(GeneralValue.savefolder+"/"+loadfile);
				deleteFile.delete();
				
				localfilebutton();
				//fp.close();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		// アラートダイアログを表示します
		alertDialog.show();
	}
	public void saveorview(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// アラートダイアログのタイトルを設定します
		alertDialogBuilder.setTitle("ファイル選択");
		// アラートダイアログのメッセージを設定します
		alertDialogBuilder.setMessage("");
		// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
		alertDialogBuilder.setPositiveButton("ルートを閲覧",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				intentsub1();
			}
		});
		alertDialogBuilder.setNeutralButton("ルートを共有",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//intentsub1();
				Sftp sftp=new Sftp();
				sftp.setSetting(GeneralValue.savefolder+"/"+loadfile,GeneralValue.onlinedir+"/"+loadfile, false);
				sftp.start();
			}
		});
		alertDialogBuilder.setNegativeButton("削除",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//System.out.println("Debug:Deletefile:"+GeneralValue.approot+"/"+loadfile);
				//deleteFile(GeneralValue.approot+"/"+loadfile);
				
				System.out.println("Debug:Deletefile:"+GeneralValue.savefolder+"/"+loadfile);
				File deleteFolder = new File(GeneralValue.savefolder+"/"+loadfile);
				deleteFolder.delete();
                Intent intent = new Intent(Sub3Activity.this,
                		Sub2Activity.class );
                
                startActivity(intent);
				//lla.invalidate();
				
				//localfilebutton();
				//fp.close();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		// アラートダイアログを表示します
		alertDialog.show();
	}
	public void intentsub1(){
		Intent intent = new Intent(Sub3Activity.this,
				Sub1Activity.class );
		intent.putExtra("FILENAME",loadfile);
		intent.putExtra("LOCALFILE",true);
		startActivity(intent);
		
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
