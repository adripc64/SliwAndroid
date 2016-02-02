package es.uji.al259348.sliwandroid.core.view;

import android.content.Context;

import es.uji.al259348.sliwandroid.core.model.User;

public interface MainView {

    Context getContext();
    void onUserLinked(User user);

}
