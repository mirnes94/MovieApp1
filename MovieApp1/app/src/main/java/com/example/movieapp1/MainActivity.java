package com.example.movieapp1;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;





import java.io.IOException;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public  void goToRegister(View view){
        Intent i= new Intent(MainActivity.this,Register.class);
        startActivity(i);
    }

    public  void goToLogin(View view){
        Intent i= new Intent(MainActivity.this,Login.class);
        startActivity(i);
    }
}

