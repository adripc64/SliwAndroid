package es.uji.al259348.sliwandroid.core.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.util.Date;

import es.uji.al259348.sliwandroid.core.R;
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

        String brokerHost = context.getResources().getString(R.string.mqtt_broker_host);
        String brokerUser = context.getResources().getString(R.string.mqtt_broker_user);
        String brokerPass = context.getResources().getString(R.string.mqtt_broker_pass);
        String clientId = context.getResources().getString(R.string.mqtt_client_id);
        Log.d("TakeSampleReceiver", "brokerHost: " + brokerHost);
        Log.d("TakeSampleReceiver", "brokerUser: " + brokerUser);
        Log.d("TakeSampleReceiver", "brokerPass: " + brokerPass);
        Log.d("TakeSampleReceiver", "clientId: " + clientId);

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        connectOptions.setUserName(brokerUser);
        connectOptions.setPassword(brokerPass.toCharArray());

        MqttAndroidClient mqttClient = new MqttAndroidClient(context.getApplicationContext(), brokerHost, clientId);

        MessagingService messagingService = new MessagingServiceImpl(mqttClient, connectOptions);
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
