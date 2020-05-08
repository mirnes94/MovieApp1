package com.example.movieapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final  String KEY_EMAIL ="str_email";
    private static final String KEY_PASSWORD = "str_password";
    private static final  String KEY_FIRST_NAME ="str_first_name";
    private static final  String KEY_LAST_NAME ="str_last_name";
    private static final String KEY_EMPTY = "";
    private String login_url = "http://10.0.2.2/member/loginAlbatros.php";

    private EditText etEmail;
    private EditText etPassword;
    private String str_email;
    private String str_password;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);


        Button login = findViewById(R.id.btnLogin);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                str_email = etEmail.getText().toString().trim();
                str_password = etPassword.getText().toString().trim();
                if (validateInputs()) {
                    login();
                }
            }
        });
    }



    /**
     * Display Progress bar while Logging in
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_EMAIL, str_email);
            request.put(KEY_PASSWORD, str_password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully

                            if (response.getInt(KEY_STATUS) == 0) {
                                String get_first_name,get_last_name;
                                get_first_name=response.getString(KEY_FIRST_NAME);
                                get_last_name=response.getString(KEY_LAST_NAME);
                                    Toast.makeText(getApplicationContext(),get_first_name+" "+get_last_name + " is " +response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                   //prelazi se u listu filmova
                                    GoToMovies();

                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {
        if(KEY_EMPTY.equals(str_email)){
            etEmail.setError("Email cannot be empty");
            etEmail.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(str_password)){
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    public  void BackToRegister(View view)
    {
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
        finish();
    }
    public  void GoToMovies()
    {
        Intent i = new Intent(Login.this, MovieMainActivity.class);
        startActivity(i);
        finish();
    }


}
