package com.example.thamkimdung.khoaluan;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thamkimdung on 20/02/2017.
 */

public class abc extends Activity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm);
        init();

    }

    private void init() {
        textView= (TextView) findViewById(R.id.dm);
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "USER_DATA_COLLECT");

        File filePath = new File(folder+"/a.txt");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;


            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');

                lineCount++;
            }
            textView.setText(String.valueOf(lineCount));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
