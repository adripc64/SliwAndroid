package es.uji.al259348.sliwandroid.core.controller;

import android.content.Context;

import java.util.ListIterator;

import es.uji.al259348.sliwandroid.core.model.Config;
import es.uji.al259348.sliwandroid.core.model.User;
import es.uji.al259348.sliwandroid.core.model.WifiScanSample;
import es.uji.al259348.sliwandroid.core.services.MessagingService;
import es.uji.al259348.sliwandroid.core.services.MessagingServiceImpl;
import es.uji.al259348.sliwandroid.core.services.UserService;
import es.uji.al259348.sliwandroid.core.services.UserServiceImpl;
import es.uji.al259348.sliwandroid.core.services.WifiService;
import es.uji.al259348.sliwandroid.core.services.WifiServiceImpl;
import es.uji.al259348.sliwandroid.core.view.ConfigView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConfigControllerImpl implements ConfigController {

    private ConfigView configView;

    private User user;
    private Config config;

    private MessagingService messagingService;
    private UserService userService;
    private WifiService wifiService;

    private ListIterator<Config.ConfigStep> configStepsIter;
    private Config.ConfigStep currentStep;

    public ConfigControllerImpl(ConfigView configView, User user) {
        this.configView = configView;
        this.user = user;
        this.config = new Config(user.getLocations());

        Context context = configView.getContext();

        this.messagingService = new MessagingServiceImpl(context);
        this.userService = new UserServiceImpl(context, messagingService);
        this.wifiService = new WifiServiceImpl(context);
    }

    @Override
    public void onDestroy() {
        messagingService.onDestroy();
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
                    .subscribe(aVoid -> {
                    }, Throwable::printStackTrace, this::onConfigFinished);
        }
    }

    private void onConfigFinished() {
        user.setConfigured(true);
        userService.setCurrentLinkedUser(user);
        configView.onConfigFinished();
    }

}
