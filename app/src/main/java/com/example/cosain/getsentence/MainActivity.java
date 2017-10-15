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
import java.util.ArrayList;
import java.util.Arrays;

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
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    //TODO your background code
                        saveThisBiatch();
                        deleteFile(file);
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

        while ((c2 = fin.read()) != -1) {
            temp2 = temp2 + Character.toString((char) c2);
        }
            ArrayList aList= new ArrayList(Arrays.asList(temp2.split(",")));
            for(int i=0;i<aList.size();i++)
            {
                String array = aList.get(i).toString();
                DatabaseReference databaseLog = FirebaseDatabase.getInstance().getReference("string");
                Sentence sen = new Sentence();
                sen.setSentence(array);
                String userLog = databaseLog.push().getKey();
                databaseLog.child(userLog).setValue(sen);
            }
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
