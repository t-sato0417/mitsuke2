package com.example.fragmentact;

//import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemSelectedListener;
import android.support.v4.app.FragmentActivity;


public class Sub2Activity extends FragmentActivity {

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_sub2);
	        Button category1 = (Button) findViewById(R.id.category1);
	        
	        category1.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Intent intent = new Intent(Sub2Activity.this,
	                		Sub3Activity.class );
	                intent.putExtra("CATEGORY", "local");
	                startActivity(intent);
	            }
	        });
	        
	        Button category2 = (Button) findViewById(R.id.category2);
	        
	        category2.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Intent intent = new Intent(Sub2Activity.this,
	                		Sub3Activity.class );
	                intent.putExtra("CATEGORY", "walk");
	                startActivity(intent);
	            }
	        });
	        
	        Button category3 = (Button) findViewById(R.id.category3);
	        
	        category3.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Intent intent = new Intent(Sub2Activity.this,
	                		Sub3Activity.class );
	                intent.putExtra("CATEGORY", "eat_out");
	                startActivity(intent);
	            }
	        });
	        
	        Button category4 = (Button) findViewById(R.id.category4);
	        
	        category4.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Intent intent = new Intent(Sub2Activity.this,
	                		Sub3Activity.class );
	                intent.putExtra("CATEGORY", "walk");
	                startActivity(intent);
	            }
	        });
	        
	        Button category5 = (Button) findViewById(R.id.category5);
	        
	        category5.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Intent intent = new Intent(Sub2Activity.this,
	                		Sub3Activity.class );
	                intent.putExtra("CATEGORY", "walk");
	                startActivity(intent);
	            }
	        });
	        /*
	  
	        // ArrayAdapter を作成  
	        String items[] = {"カテゴリを選んでください","items1","items2"};
	        ArrayAdapter<String> adapter
	                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
	        //または
	        //ArrayAdapter<String> adapter
	        //        = new ArrayAdapter<string>(this, android.R.layout.simple_spinner_item）
	        //adapter.add("item1");
	        //adapter.add("item2");
	        //adapter.add("item3");
	        
	        // ドロップダウンリストのレイアウトを設定   
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
	         
	        //spinnerにAdapterを設定
	        Spinner spinner  = (Spinner)this.findViewById(R.id.Spinner01);
	        spinner.setAdapter(adapter);
	        // Spinner に表示させるプロンプトを設定   
	        spinner.setPromptId(R.string.SpinnerPrompt );
	  
	        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
	        /*spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
	            //Spinnerのドロップダウンアイテムが選択された時
	            public void onItemSelected(AdapterView parent, View viw, int arg2, long arg3) {
	                Spinner spinner = (Spinner)parent;
	                String item = (String)spinner.getSelectedItem();
	                Toast.makeText(Sub2Activity.this, item, Toast.LENGTH_LONG).show();
	            }
	            //Spinnerのドロップダウンアイテムが選択されなかった時
	            public void onNothingSelected(AdapterView parent) {
	            }});*/
	    }
	/* @Override
	    protected void onStart () {
	    super.onStart();
	    // 初期選択状態の設定
	        ((Spinner)findViewById(R.id.Spinner01)).setSelection(0);
	    }
*/

}
