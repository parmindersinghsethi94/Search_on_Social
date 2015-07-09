package com.example.parminder.searchonsocial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    Button btn;
    EditText txt;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] user = new String[1];
        btn= (Button) findViewById(R.id.searchButton);
        txt= (EditText) findViewById(R.id.searchtext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(txt.getText().toString()).isEmpty()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    user[0] =txt.getText().toString().replace(" ","_");
                    editor.putString("user", String.valueOf(user[0]));
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Text Box Empty!!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }



}
