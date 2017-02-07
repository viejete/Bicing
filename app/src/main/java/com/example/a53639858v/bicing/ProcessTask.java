package com.example.a53639858v.bicing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class ProcessTask {

    static ArrayList<Station> processJson(String response) {

        ArrayList<Station> stations = new ArrayList<>();

        try {
            JSONObject mData = new JSONObject(response);
            JSONArray mDataStations = mData.getJSONArray("stations");
            for (int i = 0; i < mDataStations.length(); i++) {
                JSONObject mStation = mDataStations.getJSONObject(i);
                int totalBikes = Integer.parseInt(mStation.getString("slots")) + Integer.parseInt(mStation.getString("bikes"));
                Station station = new Station(
                        mStation.getString("id"),
                        mStation.getString("type"),
                        mStation.getString("latitude"),
                        mStation.getString("longitude"),
                        mStation.getString("streetName"),
                        mStation.getString("streetNumber"),
                        totalBikes,
                        Integer.parseInt(mStation.getString("bikes"))
                );
                stations.add(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stations;
    }
}
