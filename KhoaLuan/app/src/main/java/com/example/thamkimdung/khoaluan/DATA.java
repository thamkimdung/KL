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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by thamkimdung on 21/02/2017.
 */

public class DATA extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,SensorEventListener {
    private Button buttonStop,buttonStart,buttonDeleteData,buttonBack;
    private RadioButton radioWalk,radioRun,radioFall,radioLay,radioStop,radioMoving;
    private TextView textViewStatus;
    private StringBuilder vehicle;


    private final float alpha = (float) 0.8;





    private boolean pressed=false;
    FileOutputStream writer;
    OutputStreamWriter osw;

    FileOutputStream writer2;
    OutputStreamWriter osw2;

    FileOutputStream writer3;
    FileOutputStream writerEarthAll;

    FileOutputStream writeEarthOnly;


    private SensorManager sensorManager;
    private Sensor accelerometer,gryoscope,magnetometer,gravity;
    private float vibrateThreshold = 0;

    private float[] gravityValues = null;
    private float[] magneticValues = null;


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
        textViewStatus= (TextView) findViewById(R.id.tvStatus);
        radioFall= (RadioButton) findViewById(R.id.radioFall);
        radioWalk= (RadioButton) findViewById(R.id.radioWalk);
        radioRun= (RadioButton) findViewById(R.id.radioRun);
        radioLay= (RadioButton) findViewById(R.id.radioLay);
        radioStop= (RadioButton) findViewById(R.id.radioStop);
        radioMoving= (RadioButton) findViewById(R.id.radioMoving);


        radioFall.setOnCheckedChangeListener(this);
        radioWalk.setOnCheckedChangeListener(this);
//        radioWalk.setChecked(true);
        radioRun.setOnCheckedChangeListener(this);
        radioLay.setOnCheckedChangeListener(this);


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

    protected void onResume() {
        super.onResume();



        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gryoscope, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,gravity,SensorManager.SENSOR_DELAY_GAME);
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            if((RadioButton)compoundButton==radioWalk){
                vehicle.setLength(0);
                vehicle.append("Walk");
            } else if((RadioButton)compoundButton==radioRun){
                vehicle.setLength(0);
                vehicle.append("Run");
            }else if((RadioButton)compoundButton==radioLay){
                vehicle.setLength(0);
                vehicle.append("Lay");
            }else{
                vehicle.setLength(0);
                vehicle.append("Fall");
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;



        long currentDateandTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);



        boolean success = true;


        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Accelerometer Data Collector");
        File filePath = new File(folder+"/DL_Goc_Accelerometer_"+vehicle+""+".csv");


        Date lastModDate = new Date(filePath.lastModified());
        File filePathEarthAll = new File(folder+"/Earth_All_Accelerometer_"+vehicle+"_"+".csv");


        File filePathEarthOnly = new File(folder+"/Earth_Only_Accelerometer_"+vehicle+""+".csv");


        File folder2 = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Gyroscope Data Collector");
        File filePath2 = new File(folder2+"/DL_Goc_Gyroscope_"+vehicle+""+".csv");

        File folder3 = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Magnerometer Data Collector");
        File filePath3 = new File(folder3+"/DL_Goc_Magnerometer_"+vehicle+""+".csv");


//        File csv = new File(folder3+"/DL_Goc_Magnerometer_"+vehicle+"_"+currentDateandTime+".csv");
//
//        String csvFile = folder+"/abc.csv";
        //FileWriter writerCSV = null;



        if (!folder.exists()) {
            success = folder.mkdirs();

        }
        if (!folder2.exists()) {
            success = folder2.mkdirs();

        }
        if (!folder3.exists()) {
            success = folder3.mkdirs();

        }


        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            try {
                String s=currentDateandTime+","+event.values[0]+","+event.values[1]+","+event.values[2]+"\n";
                writer=new FileOutputStream(filePath,true);
                writer.write(s.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if((gravityValues != null) && (magneticValues != null)){

                float[] deviceRelativeAcceleration = new float[4];
                deviceRelativeAcceleration[0] = event.values[0];
                deviceRelativeAcceleration[1] = event.values[1];
                deviceRelativeAcceleration[2] = event.values[2];
                deviceRelativeAcceleration[3] = 0;

                // Change the device relative acceleration values to earth relative values
                // X axis -> East
                // Y axis -> North Pole
                // Z axis -> Sky

                float[] R = new float[16], I = new float[16], earthAcc = new float[16];

                SensorManager.getRotationMatrix(R, I, gravityValues, magneticValues);

                float[] inv = new float[16];

                android.opengl.Matrix.invertM(inv, 0, R, 0);
                android.opengl.Matrix.multiplyMV(earthAcc, 0, inv, 0, deviceRelativeAcceleration, 0);
                String s=currentDateandTime+","+earthAcc[0]+","+earthAcc[1]+","+earthAcc[2]+"\n";
                try {
                    writerEarthAll=new FileOutputStream(filePathEarthAll,true);
                    writerEarthAll.write(s.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(gravityValues==null || magneticValues==null){
                float deviceRelativeAcceleration[] = new float[4];
                deviceRelativeAcceleration[0] = event.values[0];
                deviceRelativeAcceleration[1] = event.values[1];
                deviceRelativeAcceleration[2] = event.values[2];
                deviceRelativeAcceleration[3] = 0;


                //Invert R
                float invert[];
                float R[];
                float earthAccel[];
                invert = new float[16];
                R = new float[16];
                earthAccel = new float[16];
                android.opengl.Matrix.invertM(invert,0,R,0);
                android.opengl.Matrix.multiplyMV(earthAccel,0,invert,0,deviceRelativeAcceleration,0);

                try {
                    writeEarthOnly=new FileOutputStream(filePathEarthOnly,true);
                    String s = currentDateandTime+","+earthAccel[0]+","+earthAccel[1]+","+earthAccel[2]+"\n";
                    writeEarthOnly.write(s.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }




        }else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            gravityValues = event.values;

        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticValues = event.values;
            try {
                writer3=new FileOutputStream(filePath3,true);
                String s=currentDateandTime+","+magneticValues[0]+","+magneticValues[1]+","+magneticValues[2]+"\n";
                writer3.write(s.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            try {
                writer2=new FileOutputStream(filePath2,true);
                String s=currentDateandTime+","+event.values[0]+","+event.values[1]+","+event.values[2]+"\n";
                writer2.write(s.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                pressed=true;
                textViewStatus.setText("Start");
                radioMoving.setChecked(true);

                accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(this,accelerometer , SensorManager.SENSOR_DELAY_GAME);
                gryoscope=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

                sensorManager.registerListener(this,gryoscope , SensorManager.SENSOR_DELAY_GAME);
                magnetometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                sensorManager.registerListener(this,magnetometer,SensorManager.SENSOR_DELAY_GAME);

                gravity=sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
                sensorManager.registerListener(this,gravity,SensorManager.SENSOR_DELAY_GAME);


                vibrateThreshold = accelerometer.getMaximumRange() / 2;
                v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);



                break;
            case R.id.btnStop:

                if(pressed==true){
                    try {

                        textViewStatus.setText("Stop");
                        radioStop.setChecked(true);


                        writer.flush();
                        writer.close();

                        writerEarthAll.flush();
                        writerEarthAll.close();


                        writeEarthOnly.flush();
                        writeEarthOnly.close();


                        writer2.flush();
                        writer2.close();



                        writer3.flush();
                        writer3.close();

                        sensorManager.unregisterListener(this);
                        pressed=false;
                        Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.btnDeleteData:
                Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }
    }


}

