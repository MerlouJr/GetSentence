package com.example.cosain.getsentence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by cosain on 10/13/2017.
 */

public class NetworkListener extends BroadcastReceiver {
    private static String file = "offline";


    @Override
    public void onReceive(final Context context, Intent intent) {


        if (isOnline()) {
            Toast.makeText(context, "Internet Connection Available", Toast.LENGTH_SHORT).show();

                    context.sendBroadcast(new Intent("Internet is Available"));

                }else{

            Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
        }
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
