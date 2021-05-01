package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    Button btnload,btn_import;
    ListView listview_file;
    TextView tv;
    ArrayList<String> namefile ;
    String[] files ;
    adapterfilename adapter;
    int pos ;
    int current_pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnload = (Button) findViewById(R.id.btn_Load);
        btn_import = (Button) findViewById(R.id.btn_import);

        tv = (TextView) findViewById(R.id.textView2);
        listview_file = (ListView) findViewById(R.id.listviewfile);
        namefile = new ArrayList<>();
        current_pos = -1;
        files = new String[0];
        if(current_pos == -1) btn_import.setEnabled(false);
        btnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssetManager asset = getAssets();

                try {
                    files = asset.list("data");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<files.length;i++)
                {
                    Toast.makeText(getApplicationContext(), files[i], Toast.LENGTH_SHORT).show();
                    if(getExtension(files[i]).equals("xml"))
                        namefile.add(files[i]);
                }
                adapter = new adapterfilename(MainActivity.this,R.layout.name_file,namefile);
                listview_file.setAdapter(adapter);
            }
        });
        listview_file.setClickable(true);
        listview_file.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_pos = position;
                btn_import.setEnabled(true);

            }
        });

        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReadFileXml(namefile.get(current_pos)).execute();
            }
        });
    }
    public static String getExtension(String name) {

        int dotIndex = name.lastIndexOf('.');
        return (dotIndex == -1) ? "" : name.substring(dotIndex + 1);

    }

    class ReadFileXml extends AsyncTask<Void, String, String> {

        InputStream in;
        ProgressBar pg;
        int count;
        String name;
        public ReadFileXml(String s) {
            name = s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.print(s);
            tv.setText(tv.getText() + "\n" +s);
        }

        @Override
        protected String doInBackground(Void... voids) {
            AssetManager asset = getAssets();
            String url = "data/" + name;
            try {
                in = asset.open(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document doc = null;
            try {
                doc = dBuilder.parse(in);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            if(doc == null) return "was't made by parser";
           // doc.getDocumentElement().normalize();
            String result = "";
            if (doc.getElementsByTagName("instanceID").getLength() > 0) {
                for(int i=0;i<doc.getElementsByTagName("instanceID").getLength();i++)
                   result =result+"\n"+ doc.getElementsByTagName("instanceID").item(i).getTextContent();

                return result;
            }
            else {
                return "No tag instance Id";
            }

        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg = (ProgressBar) findViewById(R.id.progressBar);
            pg.setMax(100);
            pg.setProgress(0);
            count = 0;
            tv.setText("Runningggggggggggggggggggg");
            pg.setVisibility(View.VISIBLE);
            System.out.print("Bắt đầu!!!!!!!!!!!!");

        }
    }


}