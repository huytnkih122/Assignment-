package com.example.assignment;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class adapterfilename extends BaseAdapter {

    private Context context;

    private List<String> arraylist;
    private int layout;
    private int progressbar;



    public adapterfilename(Context context, int layout,List<String> arraylist) {
        this.context = context;
        this.layout = layout;

        this.arraylist = arraylist;
    }




    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout,null);
        String file =  arraylist.get(position);
        TextView txtTenfile = (TextView) convertView.findViewById(R.id.textView);
        txtTenfile.setText(file);
        ProgressBar prgbar = (ProgressBar) convertView.findViewById(R.id.progressBar);

        return convertView;
    }






}
