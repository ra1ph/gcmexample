package com.ra1ph.gcmexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 29.10.13
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class HtmlActivity extends Activity {
    private final static String URL = "http://app.wifix.ru/banner.html";
    private final static String URL_BASE = "http://app.wifix.ru/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.html_activity);
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(URL);
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Document doc = Jsoup.connect(URL).get();
                            Elements el = doc.select(".img img");
                            Element img = el.first();
                            String urlImg = el.attr("src");
                            mDownloadAndSave(URL_BASE + urlImg);
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);    //To change body of overridden methods use File | Settings | File Templates.
                        AlertDialog.Builder builder = new AlertDialog.Builder(HtmlActivity.this);
                        builder.setTitle("Save");
                        builder.setMessage("Picture has been saved in Gallery");
                        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }.execute();
            }
        }

        );
    }

    public void mDownloadAndSave(String url) {
        // Setting up file to write the image to.
        String name = "kupon-" + Long.toString(System.currentTimeMillis()) + ".jpg";
        File f = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_PICTURES, name);

        // Open InputStream to download the image.
        InputStream is;
        try {
            is = new java.net.URL(url).openStream();

            // Set up OutputStream to write data into image file.
            OutputStream os = new FileOutputStream(f);

            CopyStream(is, os);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        addImageGallery(f);
    }

    private void addImageGallery( File file ) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); // setar isso
        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {

        }
    }
}
