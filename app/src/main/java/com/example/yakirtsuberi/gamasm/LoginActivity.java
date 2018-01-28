package com.example.yakirtsuberi.gamasm;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yakirtsuberi.gamasm.Managers.SessionManager;
import com.example.yakirtsuberi.gamasm.Requests.Requests;

import org.json.JSONException;
import org.json.JSONObject;


import static com.example.yakirtsuberi.gamasm.Managers.SessionManager.KEY_TOKEN;

public class LoginActivity extends Activity {


    // Email, password edittext
    EditText txtUsername, txtPassword;

    // login button
    Button btnLogin;

    // Alert Dialog Manager
//    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManager session;
    Requests requests = new Requests();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Session Manager
        session = new SessionManager(getApplicationContext());

        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        // Login button
        btnLogin = (Button) findViewById(R.id.btnLogin);


        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                // Check if username, password is filled
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    // For testing puspose username, password is checked with sample data
                    // username = test
                    // password = test
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("username", username);
                        jo.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new CheckLogin().execute(jo);
                } else {
                    Snackbar.make(arg0, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

    }

    private class CheckLogin extends AsyncTask<JSONObject, Void, String> { //<Params, Progress, Result>
        ProgressBar loading_spinner;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_spinner = (ProgressBar) findViewById(R.id.loading_spinner);
            loading_spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(JSONObject... jsonObjects) {
            return requests.post("/auth", jsonObjects[0].toString());

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading_spinner.setVisibility(View.INVISIBLE);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    if (jo.has(KEY_TOKEN)) {
                        session.createLoginSession(jo.getString(KEY_TOKEN));

                        // Staring MainActivity
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Snackbar.make(findViewById(R.id.btnLogin), "שם משתמש או סיסמה אינם נכונים.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

    }
}