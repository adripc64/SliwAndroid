package es.uji.al259348.sliwandroid.core.controller;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import es.uji.al259348.sliwandroid.core.R;
import es.uji.al259348.sliwandroid.core.services.AlarmService;
import es.uji.al259348.sliwandroid.core.services.AlarmServiceImpl;
import es.uji.al259348.sliwandroid.core.services.MQService;
import es.uji.al259348.sliwandroid.core.services.MQServiceImpl;
import es.uji.al259348.sliwandroid.core.services.UserService;
import es.uji.al259348.sliwandroid.core.services.UserServiceImpl;
import es.uji.al259348.sliwandroid.core.view.MainView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainControllerImpl implements MainController {

    private MainView mainView;

    private MqttAndroidClient mqttClient;
    private MQService mqService;
    private UserService userService;

    public MainControllerImpl(MainView mainView) {
        this.mainView = mainView;

        Context context = mainView.getContext();

        String brokerHost = context.getResources().getString(R.string.mqtt_broker_host);
        String brokerUser = context.getResources().getString(R.string.mqtt_broker_user);
        String brokerPass = context.getResources().getString(R.string.mqtt_broker_pass);
        String clientId = context.getResources().getString(R.string.mqtt_client_id);

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        connectOptions.setUserName(brokerUser);
        connectOptions.setPassword(brokerPass.toCharArray());

        this.mqttClient = new MqttAndroidClient(context, brokerHost, clientId, new MemoryPersistence());

        this.mqService = new MQServiceImpl(mqttClient, connectOptions);
        this.userService = new UserServiceImpl(mqService);
    }

    @Override
    public void onDestroy() {
        mqttClient.unregisterResources();
        //mqttClient.close();
    }

    @Override
    public void retrieveUserLinked() {
        userService.getUserLinkedTo("a:b:c:d:e")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mainView::onUserLinked);
    }

}
