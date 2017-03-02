package com.example.thamkimdung.khoaluan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thamkimdung on 19/02/2017.
 */

public class DataCollector extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,SensorEventListener {
    private Button buttonStop,buttonStart,buttonDeleteData,buttonBack;
    private RadioButton radioWalk,radioRun,radioFall,radioLay,radioStop,radioMoving;
    private TextView textViewStatus;
    private StringBuilder vehicle;

    FileOutputStream writer;
    OutputStreamWriter osw;

    private SensorManager sensorManager;
    private Sensor accelerometer,gryoscope,magnetometer;
    private float vibrateThreshold = 0;


    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private float lastX, lastY, lastZ;

    private float xGyroscope;
    private float yGyroscope;
    private float zGyroscope;



    private float xMagnetometer;
    private float yMagnetometer;
    private float zMagnetometer;
    public Vibrator v;

//    private File folder,folder2,folder3;
//    private File filePath,filePath2,filePath3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_colector);

        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);



        init();

    }


    private void init() {

        vehicle=new StringBuilder("");
        radioFall= (RadioButton) findViewById(R.id.radioFall);
        radioWalk= (RadioButton) findViewById(R.id.radioWalk);
        radioRun= (RadioButton) findViewById(R.id.radioRun);
        radioLay= (RadioButton) findViewById(R.id.radioLay);
        radioStop= (RadioButton) findViewById(R.id.radioStop);
        radioMoving= (RadioButton) findViewById(R.id.radioMoving);

        buttonBack= (Button) findViewById(R.id.btnBack);
        buttonDeleteData=(Button) findViewById(R.id.btnDeleteData);
        buttonStart= (Button) findViewById(R.id.btnStart);
        buttonStop= (Button) findViewById(R.id.btnStop);


        buttonDeleteData.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonStart.setOnClickListener(this);
        buttonBack.setOnClickListener(this);

        radioWalk.setOnCheckedChangeListener(this);
        radioFall.setOnCheckedChangeListener(this);
        radioRun.setOnCheckedChangeListener(this);
        radioLay.setOnCheckedChangeListener(this);
    }



    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Gyroscope Data Collector");
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy__HH_mm_ss");
        String currentDateandTime = sdf.format(new Date());
        File filePath = new File(folder+"/DL_Goc_Gyroscope"+currentDateandTime+".txt");
        boolean success = true;



        if (!folder.exists()) {
            success = folder.mkdirs();

        }
        if (success){


            try {
                writer = new FileOutputStream(filePath,true);

                String content=Float.toString(event.values[2])+"\t"+Float.toString(event.values[1])+"\t"+Float.toString(event.values[0])+"\n";
                writer.write(content.getBytes());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }






            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){


                displayCleanValues();

                displayCurrentValues();



                deltaX = Math.abs(lastX - event.values[0]);
                deltaY = Math.abs(lastY - event.values[1]);
                deltaZ = Math.abs(lastZ - event.values[2]);
                String content=deltaX+"\t"+deltaY+"\t"+deltaZ+"\n";



                if ((deltaZ > vibrateThreshold ) || (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold)) {
                    v.vibrate(50);
                }
            }else if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
                xGyroscope = event.values[0];
                yGyroscope = event.values[1];
                zGyroscope = event.values[2];
                String content2=xGyroscope+"\t"+yGyroscope+"\t"+zGyroscope+"\n";



//
            }else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                xMagnetometer = event.values[0];
                yMagnetometer = event.values[1];
                zMagnetometer = event.values[2];
                String content3=xMagnetometer+"\t"+yMagnetometer+"\t"+zMagnetometer+"\n";



            }



    }
    public void displayCleanValues() {

    }


    public void displayCurrentValues() {

    }


    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btnBack:
               Intent intent=new Intent(this,MainActivity.class);
               startActivity(intent);
               break;
           case R.id.btnStart:

               sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_NORMAL);


               break;
           case R.id.btnStop:

               try {

                   writer.flush();
                   writer.close();
                   sensorManager.unregisterListener(this);
                   Toast.makeText(this,"done",Toast.LENGTH_SHORT).show();

               } catch (IOException e) {
                   e.printStackTrace();
               }

               break;
           case R.id.btnDeleteData:

               break;
           default:
               break;
       }
        }

}

