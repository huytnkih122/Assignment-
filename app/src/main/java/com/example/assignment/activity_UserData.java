package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class activity_UserData extends AppCompatActivity {
    ListView list_sql;
    TextView txtInstanceID, txtDataxml;
    Database dtb;
    ArrayList<String> namefilearray ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__user_data);

        list_sql = (ListView) findViewById(R.id.listview_dtbfile);
        txtDataxml = (TextView) findViewById(R.id.txt_dataxml);
        txtInstanceID= (TextView) findViewById(R.id.txt_InstanceID);
        dtb = new Database(this,"Assignment.sqlite",null,1);
        

        Cursor namefile = dtb.getData("SELECT * FROM Location");
        namefile.moveToPosition(-1);
//        Log.d("Files", "Size: "+ namefile.getString(0));
        while(namefile.moveToNext())
        {
            namefilearray.add(namefile.getString(0));
            //Toast.makeText(this,namefile.getString(0).toString(),Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter adapter = new ArrayAdapter(activity_UserData.this, android.R.layout.simple_list_item_1, namefilearray);
        list_sql.setAdapter(adapter);

    }




}