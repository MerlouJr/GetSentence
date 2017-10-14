package com.example.cosain.getsentence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText string;
    TextView view;
    Button save,read;
    private static String file = "offline.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        string = (EditText) findViewById(R.id.sentence);
        save = (Button) findViewById(R.id.nat);
        view = (TextView) findViewById(R.id.display);

        read = (Button) findViewById(R.id.readnat);


        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes = string.getText().toString();
                String comma = ",";
                if (isOnline()) {
                    DatabaseReference databaseLog = FirebaseDatabase.getInstance().getReference("string");
                    Sentence sen = new Sentence();
                    sen.setSentence(mes);
                    String userLog = databaseLog.push().getKey();
                    databaseLog.child(userLog).setValue(sen);
                    //finish();
                } else {

                    try {
                        FileOutputStream fileOutputStream = openFileOutput(file, MODE_APPEND);
                        fileOutputStream.write(mes.getBytes());
                        fileOutputStream.write(comma.getBytes());
                        fileOutputStream.close();
                        Toast.makeText(getApplication(), "File successfully saved...", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //Todo read file then save to database
                }
            }
        });

        registerReceiver(broadcastReceiver, new IntentFilter("Internet is Available"));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // internet lost alert dialog method call from here...
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    //TODO your background code
                    saveThisBiatch();
                }
            });
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void saveThisBiatch(){

        try {
        FileInputStream fin = openFileInput(file);
        int c2;
        String temp2 = "";
        String separator = ",";

        while ((c2 = fin.read()) != -1) {
            temp2 = temp2 + Character.toString((char) c2);

        }
        //String[] separate = temp2.split(separator);
        DatabaseReference databaseLog = FirebaseDatabase.getInstance().getReference("string");
        Sentence sen = new Sentence();
            sen.setSentence(temp2);
        String userLog = databaseLog.push().getKey();
        databaseLog.child(userLog).setValue(sen);
        Toast.makeText(getApplication(), "file save to db", Toast.LENGTH_LONG).show();
        deleteFile(file);
        } catch (Exception e) {
        }
    }

public void deleteFile(){

    deleteFile(file);
}
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}
