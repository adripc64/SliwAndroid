package es.uji.al259348.sliwandroid.core.services;

import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;
import java.util.Map;

public class MQServiceImpl implements MQService, MqttCallback {

    private MqttAndroidClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;
    private Map<String, ResponseListener> responseListenerMap;

    public MQServiceImpl(MqttAndroidClient mqttClient, MqttConnectOptions mqttConnectOptions) {
        this.mqttClient = mqttClient;
        this.mqttConnectOptions = mqttConnectOptions;
        mqttClient.setCallback(this);
        responseListenerMap = new HashMap<>();
    }

    @Override
    public void publish(String topic, String msg) {
        try {
            mqttClient.publish(topic, msg.getBytes(), 2, true);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void request(final String topic, final String msg, final ResponseListener responseListener) {
        Log.d("THREAD", "MQTT request | " + Thread.currentThread().getName());
        IMqttToken token;
        if (!mqttClient.isConnected()) {
            try {
                token = mqttClient.connect(mqttConnectOptions);
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken iMqttToken) {
                        Log.d("THREAD", "MQTT onConnect | " + Thread.currentThread().getName());
                        request(topic, msg, responseListener);
                    }

                    @Override
                    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                        Log.d("THREAD", "MQTT onConnectFailure | " + Thread.currentThread().getName());
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            responseListenerMap.put(topic + "/response", responseListener);
            try {
                mqttClient.subscribe(topic + "/response", 2);
                mqttClient.publish(topic + "/request", msg.getBytes(), 2, false);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        Log.d("THREAD", "MQTT connectionLost | " + Thread.currentThread().getName());
        try {
            mqttClient.connect(mqttConnectOptions);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        Log.d("THREAD", "MQTT messageARrived | " + Thread.currentThread().getName());
        ResponseListener responseListener = responseListenerMap.get(topic);
        if (responseListener != null) {
            mqttClient.unsubscribe(topic + "/response");
            responseListenerMap.remove(topic);
            responseListener.onResponse(new String(mqttMessage.getPayload()));
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Log.d("THREAD", "MQTT deliveryComplete | " + Thread.currentThread().getName());
    }
}
