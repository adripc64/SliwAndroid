package es.uji.al259348.sliwandroid.core.view;

import android.content.Context;

import es.uji.al259348.sliwandroid.core.model.User;

public interface MainView {

    Context getContext();

    void hasToLink();
    void onUserLinked(User user);

    void hasToConfigure();

    void isOk();

}
