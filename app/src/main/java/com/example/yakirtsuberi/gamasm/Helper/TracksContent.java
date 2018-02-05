package com.example.yakirtsuberi.gamasm.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TracksContent {

    public List<TrackItem> ITEMS = new ArrayList<>();

    public void addItem(TrackItem trackItem) {
        ITEMS.add(trackItem);
    }

    public static class TrackItem {
        public int id;
        public String company;
        public float price;
        public String track_name;
        public String description;
        public boolean kosher;

        private TrackItem(int id, String company, float price, String track_name, String description, boolean kosher) {
            this.id = id;
            this.company = company;
            this.price = price;
            this.track_name = track_name;
            this.description = description;
            this.kosher = kosher;
        }

        public TrackItem(JSONObject jo) {
            try {
                this.id = jo.getInt("id");
                this.company = jo.getString("company");
                this.price = jo.getInt("price");
                this.track_name = jo.getString("track_name");
                this.description = jo.getString("description");
                this.kosher = jo.getBoolean("kosher");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        @Override
        public String toString() {
            return company;
        }
    }

}
