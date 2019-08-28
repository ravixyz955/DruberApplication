package com.example.user.druberapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.user.druberapplication.constant.Constant;
import com.example.user.druberapplication.service.InFlightService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlightBroadCastReceiver extends BroadcastReceiver {
    int count;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startIntent = new Intent(context, InFlightService.class);
        startIntent.putExtra("count", count++);
        List<Integer> myNumbers = new ArrayList<>();
        Random rand = new Random();
        while (true) {
            int number = rand.nextInt(69) + 1;
            if (!myNumbers.contains(number)) {
                myNumbers.add(number);

                if (myNumbers.size() == 5)
                    break;
            }
        }
        startIntent.setAction(Constant.ACTION.STARTFOREGROUND_ACTION);
        context.startService(startIntent);
    }
}
