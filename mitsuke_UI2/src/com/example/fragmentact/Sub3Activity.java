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
			AlertBox("ERROR","XML�t�@�C������������");
		} catch (IOException e) {
			AlertBox("ERROR","XML�t�@�C�����݂���܂���");
		} catch (ParserConfigurationException e) {
			AlertBox("ERROR","XML�t�@�C������������");
		}
	}

	public void localfilebutton(){
		//subtitle.setText("�쐬���ꂽ���[�g�t�@�C��");
		System.out.println("Debug:localfilebuton");
		
		
		File folder = new File(GeneralValue.savefolder);
		String filelist[] =folder.list();
		System.out.println("Debug:filelist:"+filelist[0]);
		for(int i=0;i<filelist.length;i++){
			Button insertbutton =new Button(this);
			insertbutton.setText(filelist[i]);
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

		//Sub2Activity����l���󂯎��
		//CharSequence getstring = intent.getCharSequenceExtra("CATEGORY");
		String category=intent.getStringExtra("CATEGORY");
		if(category.equalsIgnoreCase("local")){
			localfilebutton();
			return ;
		}
		//subtitle.setText("�J�e�S���[�̃t�@�C��");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		File fp = new File(Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/manage.xml");
		
		//System.out.println("Debug:manage.xml�����݂��Ȃ�");
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

		Element root = document.getDocumentElement();			
		file = document.getDocumentElement();

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
	public void saveorview(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// �A���[�g�_�C�A���O�̃^�C�g����ݒ肵�܂�
		alertDialogBuilder.setTitle("�t�@�C���I��");
		// �A���[�g�_�C�A���O�̃��b�Z�[�W��ݒ肵�܂�
		alertDialogBuilder.setMessage("");
		// �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
		alertDialogBuilder.setPositiveButton("���[�g���{��",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				intentsub1();
			}
		});
		alertDialogBuilder.setNeutralButton("���[�g�����L",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//intentsub1();
				Sftp sftp=new Sftp();
				sftp.setSetting(GeneralValue.savefolder+"/"+loadfile,GeneralValue.onlinedir+"/"+loadfile, false);
				sftp.start();
			}
		});
		alertDialogBuilder.setNegativeButton("�폜",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("Debug:Deletefile:"+GeneralValue.approot+"/"+loadfile);
				File fp = new File(GeneralValue.approot+"/"+loadfile);
				fp.delete();
				//fp.close();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		// �A���[�g�_�C�A���O��\�����܂�
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
		// �A���[�g�_�C�A���O�̃^�C�g����ݒ肵�܂�
		alertDialogBuilder.setTitle(title);
		// �A���[�g�_�C�A���O�̃��b�Z�[�W��ݒ肵�܂�
		alertDialogBuilder.setMessage(Message);
		// �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		// �A���[�g�_�C�A���O��\�����܂�
		alertDialog.show();
	}
}
