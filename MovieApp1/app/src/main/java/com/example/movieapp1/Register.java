package com.example.movieapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Register extends AppCompatActivity {

    private static final  String KEY_FIRST_NAME ="str_first_name";
    private static final  String KEY_LAST_NAME ="str_last_name";
    private static final  String KEY_EMAIL ="str_email";
    private static final String KEY_PASSWORD = "str_password";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMPTY = "";
    private String request_url = "http://10.0.2.2/member/createAccountAlbatros.php";
    String str_first_name,str_last_name,str_email,str_password, str_confirm_password;
    EditText et_first_name, et_last_name, et_email, et_password, et_confirm_password;
    Button register;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getValues();
        register=findViewById(R.id.btnCreateAccount);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                str_first_name=et_first_name.getText().toString().trim();
                str_last_name=et_last_name.getText().toString().trim();
                str_email=et_email.getText().toString().trim();
                str_password=et_password.getText().toString().trim();
                str_confirm_password= et_confirm_password.getText().toString().trim();
                if(validateInputs()){
                    CreateAccount();
                    Restart();
                    ConfirmRegister();
                }

            }
        });
    }
    public  void BackToLogin(View view){
        Intent i= new Intent(Register.this,Login.class);
        startActivity(i);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(Register.this);
        pDialog.setMessage("Adding data.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    public void getValues(){

        et_first_name=findViewById(R.id.etFirstName);
        et_last_name=findViewById(R.id.etLastName);
        et_email=findViewById(R.id.etEmail);
        et_password=findViewById(R.id.etPassword);
        et_confirm_password=findViewById(R.id.etConfirmPassword);

    }

    private void CreateAccount()
    {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put(KEY_FIRST_NAME,str_first_name);
            request.put(KEY_LAST_NAME,str_last_name);
            request.put(KEY_EMAIL,str_email);
            request.put(KEY_PASSWORD,str_password);
        } catch ( JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, request_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        try {
                            //Check if dish added successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                Toast.makeText(getApplicationContext(),"Create account", Toast.LENGTH_LONG).show();


                            }else if(response.getInt(KEY_STATUS) == 1){
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
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(str_first_name)) {
            et_first_name.setError("First Name cannot be empty");
            et_first_name.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(str_last_name)) {
            et_last_name.setError("Last Name cannot be empty");
            et_last_name.requestFocus();
            return false;

        }

        if (KEY_EMPTY.equals(str_email)) {
            et_email.setError("Email cannot be empty");
            et_email.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(str_password)) {
            et_password.setError("Password cannot be empty");
            et_password.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(str_confirm_password)) {
            et_confirm_password.setError("Confirm Password cannot be empty");
            et_confirm_password.requestFocus();
            return false;
        }
        if (!str_password.equals(str_confirm_password)) {
            et_confirm_password.setError("Password and Confirm Password does not match");
            et_confirm_password.requestFocus();
            return false;
        }

        return true;
    }
    public void Restart() {
        et_first_name.setText("");
        et_last_name.setText("");
        et_email.setText("");
        et_password.setText("");
        et_confirm_password.setText("");
    }
    public void ConfirmRegister(){
        Intent nextact = new Intent(this, Login.class);
        startActivity(nextact);
    }
}
