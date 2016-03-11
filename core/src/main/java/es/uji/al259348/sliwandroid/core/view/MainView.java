package es.uji.al259348.sliwandroid.core.view;

import es.uji.al259348.sliwandroid.core.model.User;

public interface MainView extends View {

    void onError(Throwable throwable);

    void hasToLink();
    void onUserLinked(User user);

    void hasToConfigure();

    void isOk();

}
