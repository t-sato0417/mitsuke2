package com.example.mitsuke_UI;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
//import android.content.Context;
import android.os.Bundle;
import android.view.View;
//import android.view.Window;
import android.widget.Button;
import android.content.Intent;


public class MainActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // res/layout/screen1.xml Çèâä˙âÊñ Ç…
        setContentView(R.layout.activity_main);
        
 
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);
 
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                    Sub1Activity.class );
                startActivity(intent);
            }
        });
 
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                    Sub2Activity.class );
                startActivity(intent);
            }
        });
    }

}
