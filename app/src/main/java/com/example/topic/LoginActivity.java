package com.example.topic;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{

    private EditText idText, pwText;
    private TextView findPW;

    private String email, password;
    private String URL = "http://10.0.2.2/topick/login.php";
    private long pressedTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = password = "";

        idText = findViewById(R.id.idText);
        pwText = findViewById(R.id.pwText);
        findPW = findViewById(R.id.findPW);

        findPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPw.class);
                startActivity(intent);
            }
        });

    }
    //???????????? 2??? ?????? ??? ??? ??????
    //??????????????? ?????? ???????????? 2??? ?????? ?????? ??? ??????????????? ??? ?????? ??????
    public void onBackPressed() {
        if(pressedTime == 0) {
            Toast.makeText(this, "?????? ??? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int)(System.currentTimeMillis() - pressedTime);

            if(seconds > 2000) {
                pressedTime = 0;
            }   else {
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        }
    }


    public void login(View view) {
        email = idText.getText().toString().trim();
        password = pwText.getText().toString().trim();

        //???????????? ??????????????? ???????????? ?????? ??????
        if(!email.equals("") && !password.equals("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else if (response.equals("failure")) {
                        Toast.makeText(LoginActivity.this, "ID ?????? ??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, error.toString().trim() ,Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                //HashMap?????? php??? ?????? DB??? ????????? ??????
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("email", email);
                    data.put("password", password);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "???????????? ?????? ?????? ????????????!", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view) {
        Intent intent = new Intent(LoginActivity.this, Signup.class);
        startActivity(intent);
        finish();
    }

}


