package es.uji.al259348.sliwandroid.core.services;

import rx.Observable;

public interface MQService {

    Observable<Void> publish(String topic, String msg);
    void request(String topic, String msg, ResponseListener responseListener);

}
