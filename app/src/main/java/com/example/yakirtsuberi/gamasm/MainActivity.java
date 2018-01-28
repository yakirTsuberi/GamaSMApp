package com.example.yakirtsuberi.gamasm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yakirtsuberi.gamasm.Managers.SessionManager;
import com.example.yakirtsuberi.gamasm.Requests.Requests;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import static com.example.yakirtsuberi.gamasm.Managers.SessionManager.KEY_TOKEN;

public class MainActivity extends AppCompatActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        DateFormat df = new SimpleDateFormat("MM.yyyy");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);

        TextView secondTitle = (TextView) findViewById(R.id.secondTitle);
        secondTitle.setText("נכון לחודש " + reportDate);
        if (session.isLoggedIn()) {
            new GetData().execute();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ItemListActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            session.logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Requests requests = new Requests();
            try {
                JSONObject data = new JSONObject(requests.get("/api/transactions_by_user", session.getUserDetails().get(KEY_TOKEN)));
                TextView countSuccess = (TextView) findViewById(R.id.countSuccess);
                TextView countFail = (TextView) findViewById(R.id.countFail);
                TextView countWarning = (TextView) findViewById(R.id.countWarning);

                countSuccess.setText(data.getString("success"));
                countFail.setText(data.getString("fail"));
                countWarning.setText(data.getString("waiting"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
