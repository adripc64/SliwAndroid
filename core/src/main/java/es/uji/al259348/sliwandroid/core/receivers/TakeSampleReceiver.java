package es.uji.al259348.sliwandroid.core.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

import es.uji.al259348.sliwandroid.core.services.MessagingService;
import es.uji.al259348.sliwandroid.core.services.MessagingServiceImpl;
import es.uji.al259348.sliwandroid.core.services.WifiService;
import es.uji.al259348.sliwandroid.core.services.WifiServiceImpl;

public class TakeSampleReceiver extends BroadcastReceiver {

    public TakeSampleReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TakeSampleReceiver", "onReceive: " + (new Date()).toString());

        MessagingService messagingService = new MessagingServiceImpl(context.getApplicationContext());
        WifiService wifiService = new WifiServiceImpl(context.getApplicationContext());

        wifiService.performScan()
                .doOnError(Throwable::printStackTrace)
                .doOnNext(sample -> {
                    Log.d("TakeSampleReceiver", "Sample: " + sample);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();

                        String topic = "user/1/sample";
                        String msg = objectMapper.writeValueAsString(sample);

                        messagingService.publish(topic, msg)
                                .doOnError(Throwable::printStackTrace)
                                .doOnCompleted(() -> Log.d("TakeSampleReceiver", "Sample published!"))
                                .subscribe();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                })
                .subscribe();
    }

}
