package es.uji.al259348.sliwandroid.core.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.UUID;

import es.uji.al259348.sliwandroid.core.services.DeviceService;
import es.uji.al259348.sliwandroid.core.services.DeviceServiceImpl;
import es.uji.al259348.sliwandroid.core.services.MessagingService;
import es.uji.al259348.sliwandroid.core.services.MessagingServiceImpl;
import es.uji.al259348.sliwandroid.core.services.UserService;
import es.uji.al259348.sliwandroid.core.services.UserServiceImpl;
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
        DeviceService deviceService = new DeviceServiceImpl(context.getApplicationContext());
        UserService userService = new UserServiceImpl(context.getApplicationContext());

        wifiService.takeSample()
                .doOnError(Throwable::printStackTrace)
                .doOnNext(sample -> {

                    String deviceId = deviceService.getId();
                    String userId = userService.getCurrentLinkedUserId();

                    sample.setId(UUID.randomUUID().toString());
                    sample.setUserId(userId);
                    sample.setDeviceId(deviceId);

                    Log.d("TakeSampleReceiver", "Sample: " + sample);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();

                        String topic = "user/" + userId + "/sample";
                        String msg = objectMapper.writeValueAsString(sample);

                        messagingService.publish(topic, msg)
                                .doOnError(Throwable::printStackTrace)
                                .doOnCompleted(() -> {
                                    Log.d("TakeSampleReceiver", "Sample published!");

                                    // Send broadcast to request a the feedback process
//                                    Intent i = new Intent("es.uji.al259348.sliwandroid.FEEDBACK_REQUEST_ACTION");
//                                    i.putExtra("asdf", "AASDFFASDF");
//                                    context.sendBroadcast(i);

                                })
                                .subscribe();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                })
                .subscribe();
    }

}
