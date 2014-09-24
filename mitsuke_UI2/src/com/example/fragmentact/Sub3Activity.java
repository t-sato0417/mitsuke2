package com.example.fragmentact;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	        Intent intent = getIntent();
	        //Sub2Activity‚©‚ç’l‚ðŽó‚¯Žæ‚é
	        CharSequence getstring = intent.getCharSequenceExtra("String Value");

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			File fp = new File(Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/manage_test.xml");
			document = documentBuilder.parse(fp);
		
			Element root = document.getDocumentElement();
			
			NodeList infotags = root.getElementsByTagName("info");
			
			file = document.getDocumentElement();

			
			int max =infotags.getLength();
			Button button[] = new Button[max];
			LinearLayout lla = new LinearLayout(this);
			lla.setOrientation(LinearLayout.VERTICAL);
	        setContentView(lla);
	        
	        /*System.out.println("Debug:walk="
	        +document.getElementsByTagName("walk").item(0).);
	        */
	        
	        /*
			for(array=0;array<max;array++) {			
			    button[array] = new Button(this);
			    Node category = root.getFirstChild();
				while (category != null) {
		            if (!category.getNodeName().equals(getstring)) {
		            	category = category.getNextSibling();
		                continue;
		            }
		            Node fileNode = category.getFirstChild();
		            while (fileNode != null) {
		                if (!fileNode.getNodeName().equals("file")) {
		                    fileNode = fileNode.getNextSibling();
		                    continue;
		                }
		                Node nameNode = fileNode.getFirstChild();
		                while (nameNode != null) {
		                	if (!nameNode.getNodeName().equals("info")) {
		                		nameNode = nameNode.getNextSibling();
		                		continue;
		                	}
		                	Node node = nameNode.getFirstChild();
		                	if (node != null) {
		                		button[array].setText(node.getNodeValue());
		                	}
		                	break;
		                }
		                fileNode = fileNode.getNextSibling();
		            }
		            category = category.getNextSibling();    
		        }
			    button[array].setText(infotags.item(array).getFirstChild().getNodeValue());
			    //button[array].setHeight(30);
			    //button[array].setWidth(30);
			    //connector = file.getElementsByTagName("file").item(array);
				//attributes = connector.getAttributes();
										    
			    button[array].setOnClickListener(new OnClickListener() {
			    		int number=array;
			            public void onClick(View view){			            	
			            	getFile(number);			            	
			            }
			    });
			    lla.addView(button[array], new LinearLayout.LayoutParams(wc, wc));  
			}*/
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
				//System.out.println("Debug:"+file.getTextContent());
				/*NodeList fileelement = file.getElementsByTagName("file");
				
				System.out.println("Debug:fileemelen="+fileelement.getLength());
				if(fileelement.getLength()!=0){
				connector = fileelement.item(array);
				attributes = connector.getAttributes();
				intent.putExtra("String Value",attributes.item(0).getNodeValue() );
				System.out.println(attributes.item(0).getNodeValue());
				//Intent intent = getIntent();
				//CharSequence getmapString = intent.getCharSequenceExtra("String Value");
				startActivity(intent);
				}*/
				
			}
		}
}
