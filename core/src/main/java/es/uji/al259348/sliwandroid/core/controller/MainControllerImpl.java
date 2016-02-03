package es.uji.al259348.sliwandroid.core.controller;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import es.uji.al259348.sliwandroid.core.services.MQService;
import es.uji.al259348.sliwandroid.core.services.MQServiceImpl;
import es.uji.al259348.sliwandroid.core.services.UserService;
import es.uji.al259348.sliwandroid.core.services.UserServiceImpl;
import es.uji.al259348.sliwandroid.core.view.MainView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainControllerImpl implements MainController {

    private static final String MQTT_URI = "tcp://192.168.0.82:61613";
    private static final String MQTT_USER = "admin";
    private static final String MQTT_PASS = "password";

    private MainView mainView;
    private MQService mqService;
    private UserService userService;

    public MainControllerImpl(MainView mainView) {
        this.mainView = mainView;

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        connectOptions.setUserName(MQTT_USER);
        connectOptions.setPassword(MQTT_PASS.toCharArray());

        MqttAndroidClient mqttClient = new MqttAndroidClient(mainView.getContext(), MQTT_URI, "publisher",  new MemoryPersistence());

        this.mqService = new MQServiceImpl(mqttClient, connectOptions);
        this.userService = new UserServiceImpl(mqService);
    }

    @Override
    public void retrieveUserLinked() {
        userService.getUserLinkedTo("a:b:c:d:e")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mainView::onUserLinked);
    }

}
