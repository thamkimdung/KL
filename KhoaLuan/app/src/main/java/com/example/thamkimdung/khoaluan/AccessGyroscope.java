package com.example.thamkimdung.khoaluan;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thamkimdung on 20/02/2017.
 */

public class AccessGyroscope extends Activity implements SensorEventListener, View.OnClickListener {
    //a TextView
    private TextView tv;
    //the Sensor Manager
    private SensorManager sManager;
    FileOutputStream writer;
    OutputStreamWriter osw;
    private Button buttonDM,buttonDMS;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm);

        //get the TextView from the layout file
        //tv = (TextView) findViewById(R.id.TV);

        //get a hook to the sensor service
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        buttonDM= (Button) findViewById(R.id.btnDM);
        buttonDM.setOnClickListener(this);
        buttonDMS= (Button) findViewById(R.id.btnDMS);
        buttonDMS.setOnClickListener(this);
    }

    //when this Activity starts
//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        /*register the sensor listener to listen to the gyroscope sensor, use the
//        callbacks defined in this class, and gather the sensor information as quick
//        as possible*/
//        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_NORMAL);
//    }

    //When this Activity isn't visible anymore
  //  @Override
//    protected void onStop()
//    {
//        //unregister the sensor listener
//        sManager.unregisterListener(this);
//        super.onStop();
//    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        //Do nothing.
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        //else it will output the Roll, Pitch and Yawn values
//        tv.setText("Orientation X (Roll) :"+ Float.toString(event.values[2]) +"\n"+
//                "Orientation Y (Pitch) :"+ Float.toString(event.values[1]) +"\n"+
//                "Orientation Z (Yaw) :"+ Float.toString(event.values[0]));

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "AAAAAAAAAAAAAAAAAAAA");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        File filePath = new File(folder+"/DL_Goc_Gyroscope_"+currentDateandTime+".txt");
        boolean success = true;

        if (!folder.exists()) {
            success = folder.mkdirs();
            //Toast.makeText(this,"done",Toast.LENGTH_SHORT).show();

        }
        if (success){

            try {
                 writer = new FileOutputStream(filePath,true);
                osw = new OutputStreamWriter(writer);
                String content=Float.toString(event.values[2])+"\t"+Float.toString(event.values[1])+"\t"+Float.toString(event.values[0])+"\n";
                writer.write(content.getBytes());


                //osw.append("\t"+currentDateandTime);
                //osw.append("\n\r");

                //Toast.makeText(this,"done",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnDM){
            try {
                osw.flush();
                osw.close();
                writer.flush();
                writer.close();
                sManager.unregisterListener(this);
                Toast.makeText(this,"done",Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(view.getId()==R.id.btnDMS){
            sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_NORMAL);


        }
    }
}