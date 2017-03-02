package com.example.thamkimdung.khoaluan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends  AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Button btnExit,btnUser,btnSetting,btnDataCollector;
    final Context context=this;
    private int id=0;




    private EditText edtBirthday,edtName;
    private RadioButton radioMale,radioFemale;


    private Spinner spinner;
    StringBuilder gender,jobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        gender = new StringBuilder("");
        jobs=new StringBuilder("");
        btnExit= (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);

        btnUser= (Button) findViewById(R.id.btnUser);
        btnUser.setOnClickListener(this);

        btnDataCollector= (Button) findViewById(R.id.btnDataCollector);
        btnDataCollector.setOnClickListener(this);

        btnSetting= (Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);



//        spinner= (Spinner) findViewById(R.id.spinner);
//        spinner.setOnItemSelectedListener(this);
//        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,jobs);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnExit:
                AppExit();
                break;
            case R.id.btnUser:



                AlertDialog.Builder alert = new AlertDialog.Builder(context);


                LayoutInflater layoutInflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View v=layoutInflater.inflate(R.layout.dialog,null);
                edtName= (EditText) v.findViewById(R.id.edtName);
                radioMale= (RadioButton) v.findViewById(R.id.radioMale);
                radioFemale= (RadioButton) v.findViewById(R.id.radioFemale);
                radioMale.setOnCheckedChangeListener(this);
                radioFemale.setOnCheckedChangeListener(this);


                alert.setView(v);
                spinner= (Spinner) v.findViewById(R.id.spinner);



                alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(context,"d",Toast.LENGTH_SHORT).show();
                        File folder = new File(Environment.getExternalStorageDirectory() +
                                File.separator + "USER_DATA_COLLECT");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        File filePath = new File(folder+"/infor.txt");

                        boolean success = true;
                        StringBuilder text = new StringBuilder();
                        if (!folder.exists()) {
                            success = folder.mkdirs();

                        }
                        if (success) {
                            // Do something on success
                            try {


                                OutputStreamWriter osw;
                                FileOutputStream writer = new FileOutputStream(filePath,true);
                                String line;


                                int lineCount = 0;
                                BufferedReader br = new BufferedReader(new FileReader(filePath));
                                while ((line = br.readLine()) != null) {
                                    text.append(line);
                                    text.append('\n');

                                    lineCount++;
                                }
                                String content="ID: "+lineCount+" Name: "+edtName.getText().toString()+" Gender: "+gender+" Job: "+jobs+" Date: ";
                                writer.write(content.getBytes());
                                osw = new OutputStreamWriter(writer);
                                osw.append(" "+currentDateandTime);
                                osw.append("\r\n");
                                osw.flush();
                                osw.close();
                                writer.flush();
                                writer.close();
                                Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
                            id++;

                        } else {
                            // Do something else on failure
                        }

                    }
                });
                alert.show();

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.option,android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String a=adapterView.getItemAtPosition(i).toString();
                        jobs.setLength(0);
                        jobs.append(a.toString());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case R.id.btnDataCollector:
                Intent intent = new Intent(this,DATA.class);
                startActivity(intent);
                break;
            case R.id.btnSetting:
                AlertDialog.Builder alert2 = new AlertDialog.Builder(context);


                LayoutInflater layoutInflater2= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v2=layoutInflater2.inflate(R.layout.dialog_setting,null);


                alert2.setView(v2);
                spinner= (Spinner) v2.findViewById(R.id.spinnerSetting);
                alert2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(context,"d",Toast.LENGTH_SHORT).show();

                    }
                });
                alert2.show();
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.option2,android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter2);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            default:
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            if((RadioButton)compoundButton==radioMale){
                gender.setLength(0);
                gender.append("Male");
            }
            else{
                gender.setLength(0);
                gender.append("Female");
            }
        }

    }
    public void AppExit()
    {

        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    /*int pid = android.os.Process.myPid();=====> use this if you want to kill your activity. But its not a good one to do.
    android.os.Process.killProcess(pid);*/

    }
}
