package es.uji.al259348.sliwandroid.core.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;

import es.uji.al259348.sliwandroid.core.model.Config;
import es.uji.al259348.sliwandroid.core.model.User;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserServiceImpl implements UserService {

    private MQService mqService;
    private ObjectMapper objectMapper;

    public UserServiceImpl(MQService mqService) {
        this.mqService = mqService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Observable<User> getUserLinkedTo(final String deviceId) {
        return Observable.create(subscriber -> {
            String topic = "user/linkedTo/" + deviceId;
            String msg = (new Date()).toString();

            mqService.request(topic, msg)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread())
                    .subscribe(s -> {
                        try {
                            User user = objectMapper.readValue(s, User.class);
                            subscriber.onNext(user);
                        } catch (IOException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    });
        });
    }

    @Override
    public Observable<Void> configureUser(User user, Config config) {
        return Observable.create(subscriber -> {
            try {
                String topic = "user/" + user.getId() + "/configure";
                String msg = objectMapper.writeValueAsString(config);

                mqService.publish(topic, msg)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(subscriber);

            } catch (JsonProcessingException e) {
                subscriber.onError(e);
            }
        });
    }

}
