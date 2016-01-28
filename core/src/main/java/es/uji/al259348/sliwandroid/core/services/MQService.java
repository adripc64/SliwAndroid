package es.uji.al259348.sliwandroid.core.services;

public interface MQService {

    void publish(String topic, String msg);
    void request(String topic, String msg, ResponseListener responseListener);

}
