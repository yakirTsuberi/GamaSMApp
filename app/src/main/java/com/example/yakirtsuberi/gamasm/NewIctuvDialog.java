package com.example.yakirtsuberi.gamasm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yakirtsuberi.gamasm.Helper.TracksContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Iterator;

public class NewIctuvDialog extends Dialog {
    private Activity c;
    private TracksContent.TrackItem item;
    JSONObject tracks;

    public NewIctuvDialog(Activity a, TracksContent.TrackItem item, JSONObject tracks) {
        super(a);
        this.c = a;
        this.item = item;
        this.tracks = tracks;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_inctuv_dialog);

        TextView title = findViewById(R.id.titleDialog);
        TextView sub_title = findViewById(R.id.sub_title);
        TextView action_add = findViewById(R.id.action_add);

        title.setText(String.valueOf(item.company));
        sub_title.setText(String.valueOf(item.track_name));
        action_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject tmp = new JSONObject();
                EditText sim_num = findViewById(R.id.sim_num);
                EditText phone_num = findViewById(R.id.phone_num);

                if (checkVlue(sim_num.getText().toString(), phone_num.getText().toString())) {
                    try {
                        tmp.put("sim_num", sim_num.getText());
                        tmp.put("phone_num", phone_num.getText());

                        Log.i("TMP", tmp.toString());
                        if (!tracks.has(String.valueOf(item.id))) {
                            tracks.put(String.valueOf(item.id), new JSONArray());
                        }
                        tracks.getJSONArray(String.valueOf(item.id)).put(tmp.toString());
                        Intent i = new Intent(c, ItemListActivity.class);
                        c.startActivity(i);
                        dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private boolean checkVlue(String sim_num, String phone_num) {
        return !sim_num.isEmpty() & !phone_num.isEmpty() & (phone_num.length() == 10);

    }

}


