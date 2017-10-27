package com.mferovante.android.consumerwebservice;

import android.util.Log;

import org.json.JSONArray;

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
