package es.uji.al259348.sliwandroid.core.services;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;

import es.uji.al259348.sliwandroid.core.model.User;
import rx.Observable;
import rx.Subscriber;

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
            mqService.request(topic, (new Date()).toString(), msg -> {
                User user = null;
                try {
                    user = objectMapper.readValue(msg, User.class);
                    subscriber.onNext(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

}
