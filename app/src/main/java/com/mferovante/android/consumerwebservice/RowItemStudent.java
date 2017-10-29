package com.mferovante.android.consumerwebservice;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marx on 04/10/17.
 */

public class RowItemStudent {

    public RowItemStudent(int id, String name) {
        this.id = id;
        this.name = name;

        Log.i("OBJECT", this.name);

    }
    public RowItemStudent(JSONObject object){
        try {
            this.name = object.getString("name");
            this.id = object.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<RowItemStudent> fromJson(JSONArray jsonObjects) {
        ArrayList<RowItemStudent> users = new ArrayList<RowItemStudent>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                users.add(new RowItemStudent(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
