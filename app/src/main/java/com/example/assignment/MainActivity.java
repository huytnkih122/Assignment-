package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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
    Button btnload,btn_import,btn_move;
    ListView listview_file;
    TextView tv;
    ArrayList<String> namefile ;
    String[] files ;
    String path;
    adapterfilename adapter;
    int current_pos;
    Database dtb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnload = (Button) findViewById(R.id.btn_Load);
        btn_import = (Button) findViewById(R.id.btn_import);
        btn_move = (Button)findViewById(R.id.btn_move);
        tv = (TextView) findViewById(R.id.textView2);
        listview_file = (ListView) findViewById(R.id.listviewfile);
        dtb = new Database(this,"Assignment.sqlite",null,1);
        Log.d("Files",  getDatabasePath("Assignment.sqlite").getPath());
        namefile = new ArrayList<>();
        current_pos = -1;
        files = new String[0];
        if(current_pos == -1) btn_import.setEnabled(false);
        btnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssetManager asset = getAssets();
                path = "data";
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
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,activity_UserData.class);
                startActivity(intent);
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
                new ReadFileXml(namefile.get(current_pos),path,dtb).execute();
            }
        });
    }

    class ReadFileXml extends AsyncTask<Void, String, String> {

        InputStream in;
        ProgressBar pg;
        int count;
        String name;
        String path;
        Database dtb;
        public ReadFileXml(String s, String path, Database dtb) {
            this.name = s;
            this.dtb = dtb;
            this.path = path;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv.setText(s);

        }

        @Override
        protected String doInBackground(Void... voids) {
            AssetManager asset = getAssets();
            String url = path +"/" + name;
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
            doc.getDocumentElement().normalize();
            String result = "";
            if (doc.getElementsByTagName("instanceID").getLength() > 0) {
               // Log.d("Files", "Size: "+ "INSERT INTO Location VALUES('" + name + "','" + path +"');");
                dtb.QueryDt("INSERT INTO Location VALUES('" + name + "','" + path +"');");
               // dtb.QueryDt("INSERT INTO Location VALUES('ccccccccccccccc','abccccccc');");
                for(int i=0;i<doc.getElementsByTagName("instanceID").getLength();i++) {
                    result = doc.getElementsByTagName("instanceID").item(i).getTextContent();
                    dtb.QueryDt("INSERT INTO Data VALUES(null,'" + name + "','" + result +"')");
                }

                return "Successful";
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
            Createtable(dtb);
            tv.setText("Runningggggggggggggggggggg");
            pg.setVisibility(View.VISIBLE);
            System.out.print("Bắt đầu!!!!!!!!!!!!");

        }
    }

    public static String getExtension(String name) {

        int dotIndex = name.lastIndexOf('.');
        return (dotIndex == -1) ? "" : name.substring(dotIndex + 1);
    }

    void Createtable(Database dtb)
    {
        dtb.QueryDt("CREATE TABLE IF NOT EXISTS Location(NAME VARCHAR(100) PRIMARY KEY, LOCAT NVARCHAR(100));");
        dtb.QueryDt("CREATE TABLE IF NOT EXISTS Data(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100) , LOCAT NVARCHAR(100),  CONSTRAINT fk_data_loca FOREIGN KEY (NAME)  REFERENCES Location(NAME)); ");
    }



}
