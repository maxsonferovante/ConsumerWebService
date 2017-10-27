package com.mferovante.android.consumerwebservice;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.List;

import static com.mferovante.android.consumerwebservice.R.id.id_student;

/**
 * Created by marx on 04/10/17.
 */

public class CustomListViewAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<RowItemStudent> rowItemStudentList;

    public CustomListViewAdapter(Activity activity, List<RowItemStudent> itemStudents) {

        this.activity = activity;
        this.rowItemStudentList = itemStudents;
    }


    @Override
    public int getCount() {
        return rowItemStudentList.size();
    }

    @Override
    public Object getItem(int i) {
        return rowItemStudentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewItem viewItem = null;
        if (convertView == null) {

            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_item, null);

            viewItem = new ViewItem();

            viewItem.IdTextView = (TextView) convertView.findViewById(id_student);
            viewItem.NameTextView = (TextView) convertView.findViewById(R.id.name_student);


            convertView.setTag(viewItem);
        }
        else{
            viewItem = (ViewItem) convertView.getTag();
        }
        viewItem.IdTextView.setText(rowItemStudentList.get(position).getId());
        viewItem.NameTextView.setText(rowItemStudentList.get(position).getName());

        return convertView;
    }
    class ViewItem{
        TextView IdTextView;
        TextView NameTextView;
    }
}
