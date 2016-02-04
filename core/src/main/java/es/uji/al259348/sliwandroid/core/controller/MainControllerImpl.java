package es.uji.al259348.sliwandroid.core.controller;

import android.content.Context;

import es.uji.al259348.sliwandroid.core.services.MessagingService;
import es.uji.al259348.sliwandroid.core.services.MessagingServiceImpl;
import es.uji.al259348.sliwandroid.core.services.UserService;
import es.uji.al259348.sliwandroid.core.services.UserServiceImpl;
import es.uji.al259348.sliwandroid.core.view.MainView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainControllerImpl implements MainController {

    private MainView mainView;

    private MessagingService messagingService;
    private UserService userService;

    public MainControllerImpl(MainView mainView) {
        this.mainView = mainView;

        Context context = mainView.getContext();

        this.messagingService = new MessagingServiceImpl(context);
        this.userService = new UserServiceImpl(context, messagingService);
    }

    @Override
    public void onDestroy() {
        messagingService.onDestroy();
    }

    @Override
    public void retrieveUserLinked() {
        userService.getUserLinkedTo("a:b:c:d:e")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mainView::onUserLinked);
    }

}
