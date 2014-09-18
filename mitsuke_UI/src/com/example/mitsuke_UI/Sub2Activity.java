package com.example.mitsuke_UI;

//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


public class Sub2Activity extends Activity {

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_sub2);
	        setTitle("画面3");
	 
	        Button btn6 = (Button) findViewById(R.id.btn6);
	        Button btn7 = (Button) findViewById(R.id.btn7);
	 
	        btn6.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                Intent intent = new Intent(Sub2Activity.this, 
	                    MainActivity.class );
	                startActivity(intent);
	                finish(); // アクティビティ終了
	            }
	        });
	 
	        btn7.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                Intent intent = new Intent(Sub2Activity.this,
	                    Sub1Activity.class );
	                startActivity(intent);
	                finish(); // アクティビティ終了
	            }
	        });
	    }

}
