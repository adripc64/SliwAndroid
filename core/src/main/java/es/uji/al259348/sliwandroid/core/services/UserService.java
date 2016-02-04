package es.uji.al259348.sliwandroid.core.services;

import es.uji.al259348.sliwandroid.core.model.Config;
import es.uji.al259348.sliwandroid.core.model.User;
import rx.Observable;

public interface UserService {

    User getCurrentLinkedUser();
    boolean setCurrentLinkedUser(User user);

    Observable<User> getUserLinkedTo(String deviceId);
    Observable<Void> configureUser(User user, Config config);

}
