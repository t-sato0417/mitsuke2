package com.example.fragmentact;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub3);
        
			try {
				initButton();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

		public void initButton() throws SAXException, IOException,
		ParserConfigurationException {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			File fp = new File(Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/manage_test.xml");
			Document document = documentBuilder.parse(fp);
		
			Element root = document.getDocumentElement();
			NodeList rootChildren = root.getChildNodes();

			int max =rootChildren.getLength();
			Button button[] = new Button[max];
			LinearLayout lla = new LinearLayout(this);
			lla.setOrientation(LinearLayout.VERTICAL);
	        setContentView(lla);
	        System.out.println(max);
			for(array=0;array<max;array++) {
			    final int index = array;
			    button[array] = new Button(this);
			    button[array].setText("ƒ{ƒ^ƒ“["+  array + "]");
			    //button[array].setHeight(30);
			    //button[array].setWidth(30);
			    
			    button[array].setOnClickListener(new OnClickListener() {
			            public void onClick(View view){
			               Toast.makeText(Sub3Activity.this,"Number =" + index ,Toast.LENGTH_SHORT).show();
			            }
			    });
			    lla.addView(button[array], new LinearLayout.LayoutParams(wc, wc));
    
			}
		}
}
