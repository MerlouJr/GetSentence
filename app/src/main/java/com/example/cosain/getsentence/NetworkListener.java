package com.example.cosain.getsentence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by cosain on 10/13/2017.
 */

public class NetworkListener extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if (noConnectivity) {
                Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Internet Connection Available", Toast.LENGTH_SHORT).show();
                 context.sendBroadcast(new Intent("Internet is Available"));
            }
        }

//        if (isOnline(context)) {
//            Toast.makeText(context, "Internet Connection Available", Toast.LENGTH_SHORT).show();
//
//                    context.sendBroadcast(new Intent("Internet is Available"));
//
//                }else{
//
//            Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
//        }
    }



    public boolean isOnline(Context context) {
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int     exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        }
//        catch (IOException e)          { e.printStackTrace(); }
//        catch (InterruptedException e) { e.printStackTrace(); }
//
//        return false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
        }catch (Exception e){

        }
        return false;
    }
}
