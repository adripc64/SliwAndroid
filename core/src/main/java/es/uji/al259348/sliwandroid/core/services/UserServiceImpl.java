package es.uji.al259348.sliwandroid.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class UserServiceImpl implements UserService {

    private MQService mqService;
    private ObjectMapper objectMapper;

    public UserServiceImpl(MQService mqService) {
        this.mqService = mqService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void requestUserLinkedTo(String deviceId, ResponseListener responseListener) {

        String topic = "user/linkedTo/" + deviceId;
        mqService.request(topic, (new Date()).toString(), responseListener);

    }

}
