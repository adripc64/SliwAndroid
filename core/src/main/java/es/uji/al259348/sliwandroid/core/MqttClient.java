package es.uji.al259348.sliwandroid.core;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClient implements IMqttActionListener, MqttCallback {

    private static final String MQTT_URI = "tcp://192.168.0.114:61613";
    private static final String MQTT_USER = "admin";
    private static final String MQTT_PASS = "password";

    private Context context;

    public MqttClient(Context context) {
        this.context = context;

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        connectOptions.setUserName(MQTT_USER);
        connectOptions.setPassword(MQTT_PASS.toCharArray());

        MqttAndroidClient client = new MqttAndroidClient(context, MQTT_URI, "publisher",  new MemoryPersistence());
        client.setCallback(this);
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @Override
    public void onSuccess(IMqttToken iMqttToken) {

    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

    }

}
