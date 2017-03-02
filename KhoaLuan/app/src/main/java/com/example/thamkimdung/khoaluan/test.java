package com.example.thamkimdung.khoaluan;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by thamkimdung on 02/03/2017.
 */

public class test extends Activity implements SensorEventListener{
    private float[] gravityValues = null;
    private float[] magneticValues = null;
    private SensorManager sensorManager;
    private Sensor accelerometer,gryoscope,magnetometer,myGravitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometer , SensorManager.SENSOR_DELAY_GAME);
        gryoscope=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(this,gryoscope , SensorManager.SENSOR_DELAY_GAME);
        magnetometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this,magnetometer,SensorManager.SENSOR_DELAY_GAME);
        myGravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this,myGravitySensor,SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if ((gravityValues != null) && (magneticValues != null)
                && (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)) {

//            float[] deviceRelativeAcceleration = new float[4];
//            deviceRelativeAcceleration[0] = event.values[0];
//            deviceRelativeAcceleration[1] = event.values[1];
//            deviceRelativeAcceleration[2] = event.values[2];
//            deviceRelativeAcceleration[3] = 0;
//
//            // Change the device relative acceleration values to earth relative values
//            // X axis -> East
//            // Y axis -> North Pole
//            // Z axis -> Sky
//
//            float[] R = new float[16], I = new float[16], earthAcc = new float[16];
//
//            SensorManager.getRotationMatrix(R, I, gravityValues, magneticValues);
//
//            float[] inv = new float[16];
//
//            android.opengl.Matrix.invertM(inv, 0, R, 0);
//            android.opengl.Matrix.multiplyMV(earthAcc, 0, inv, 0, deviceRelativeAcceleration, 0);
//            Log.d("Acceleration", "Values: (" + earthAcc[0] + ", " + earthAcc[1] + ", " + earthAcc[2] + ")");
//



            float[] R = new float[9];
            float[] I = new float[9];
            SensorManager.getRotationMatrix(R, I, gravityValues, magneticValues);
            float [] A_D = event.values.clone();
            float [] A_W = new float[3];
            A_W[0] = R[0] * A_D[0] + R[1] * A_D[1] + R[2] * A_D[2];
            A_W[1] = R[3] * A_D[0] + R[4] * A_D[1] + R[5] * A_D[2];
            A_W[2] = R[6] * A_D[0] + R[7] * A_D[1] + R[8] * A_D[2];

            Log.d("Accele: "," "+A_W[0]+", "+A_W[1]+", "+A_W[2]);
        } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            gravityValues = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticValues = event.values;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
