package es.uji.al259348.sliwandroid.core.controller;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import es.uji.al259348.sliwandroid.core.services.MQService;
import es.uji.al259348.sliwandroid.core.services.MQServiceImpl;
import es.uji.al259348.sliwandroid.core.services.ResponseListener;
import es.uji.al259348.sliwandroid.core.services.UserService;
import es.uji.al259348.sliwandroid.core.services.UserServiceImpl;

public class MainController {

    private static final String MQTT_URI = "tcp://192.168.0.104:61613";
    private static final String MQTT_USER = "admin";
    private static final String MQTT_PASS = "password";

    private Context context;
    private MQService mqService;
    private UserService userService;

    public MainController(Context context) {
        this.context = context;

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        connectOptions.setUserName(MQTT_USER);
        connectOptions.setPassword(MQTT_PASS.toCharArray());

        MqttAndroidClient mqttClient = new MqttAndroidClient(context, MQTT_URI, "publisher",  new MemoryPersistence());

        this.mqService = new MQServiceImpl(mqttClient, connectOptions);
        this.userService = new UserServiceImpl(mqService);
    }

    public void requestUserLinkedTo(ResponseListener responseListener) {
        userService.requestUserLinkedTo("a:b:c:d:e", responseListener);
    }

}
