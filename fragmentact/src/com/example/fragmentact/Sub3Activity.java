package com.example.fragmentact;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.FragmentActivity;

public class Sub3Activity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub3);
        Button btn9 = (Button) findViewById(R.id.btn9);
        
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sub3Activity.this,
                    Sub1Activity.class );
                startActivity(intent);
            }
        });
	}
}
