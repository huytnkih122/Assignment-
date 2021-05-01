package com.example.assignment;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ReadFileXml extends AsyncTask<Void, String, String> {

    public static String  read(InputStream in)  throws IOException, SAXException, ParserConfigurationException {

        //String url = "src\\main\\assets\\data\\All_in_One_GEN007_2015-07-06_11-31-03-74.xml";

            //File fXmlFile = new File(url);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);

            doc.getDocumentElement().normalize();
            NodeList res = doc.getElementsByTagName("instanceID");
            if (doc.getElementsByTagName("instanceID").getLength() > 0) {
                Integer a = doc.getElementsByTagName("instanceID").getLength();
             //   String result = doc.getElementsByTagName("instanceID").item(1).getTextContent();

                return a.toString();
            }
            else {
                return "No";
            }



    }

    @Override
    protected String doInBackground(Void... voids) {
        return "Xonggggg";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.print(s);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.print("Bắt đầu!!!!!!!!!!!!");

    }
}
