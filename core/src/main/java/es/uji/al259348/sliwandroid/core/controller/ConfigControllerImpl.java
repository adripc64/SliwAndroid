package es.uji.al259348.sliwandroid.core.controller;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ListIterator;

import es.uji.al259348.sliwandroid.core.model.Config;
import es.uji.al259348.sliwandroid.core.model.User;
import es.uji.al259348.sliwandroid.core.model.WifiScanSample;
import es.uji.al259348.sliwandroid.core.services.MQService;
import es.uji.al259348.sliwandroid.core.services.MQServiceImpl;
import es.uji.al259348.sliwandroid.core.services.UserService;
import es.uji.al259348.sliwandroid.core.services.UserServiceImpl;
import es.uji.al259348.sliwandroid.core.services.WifiService;
import es.uji.al259348.sliwandroid.core.services.WifiServiceImpl;
import es.uji.al259348.sliwandroid.core.view.ConfigView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConfigControllerImpl implements ConfigController {

    private static final String MQTT_URI = "tcp://192.168.0.90:61613";
    private static final String MQTT_USER = "admin";
    private static final String MQTT_PASS = "password";

    private ConfigView configView;

    private User user;
    private Config config;

    private MqttAndroidClient mqttClient;
    private MQService mqService;
    private UserService userService;
    private WifiService wifiService;

    private ListIterator<Config.ConfigStep> configStepsIter;
    private Config.ConfigStep currentStep;

    public ConfigControllerImpl(ConfigView configView, User user) {
        this.configView = configView;
        this.user = user;
        this.config = new Config(user.getLocations());

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(true);
        connectOptions.setUserName(MQTT_USER);
        connectOptions.setPassword(MQTT_PASS.toCharArray());

        this.mqttClient = new MqttAndroidClient(configView.getContext(), MQTT_URI, "publisher",  new MemoryPersistence());

        this.mqService = new MQServiceImpl(mqttClient, connectOptions);
        this.userService = new UserServiceImpl(mqService);
        this.wifiService = new WifiServiceImpl(configView.getContext());
    }

    @Override
    public void onDestroy() {
        mqttClient.unregisterResources();
        mqttClient.close();
    }

    @Override
    public void startConfig() {
        configStepsIter = config.getSteps().listIterator();
        if (configStepsIter.hasNext()) {
            currentStep = configStepsIter.next();
            configView.onNextStep(currentStep.getLocation().getConfigMsg());
        }
    }

    @Override
    public void startStep() {
        performScan();
    }

    private void performScan() {
        wifiService.performScan()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onScanPerformed);
    }

    private void onScanPerformed(WifiScanSample sample) {
        currentStep.addSample(sample);

        int progress = currentStep.getProgress();
        configView.onStepProgressUpdated(progress);

        if (currentStep.isCompleted()) {
            onStepFinished();
        } else {
            performScan();
        }
    }

    private void onStepFinished() {
        if (configStepsIter.hasNext()) {
            currentStep = configStepsIter.next();
            configView.onNextStep(currentStep.getLocation().getConfigMsg());
        } else {
            userService.configureUser(user, config)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aVoid -> {}, Throwable::printStackTrace, configView::onConfigFinished);
        }
    }
}
