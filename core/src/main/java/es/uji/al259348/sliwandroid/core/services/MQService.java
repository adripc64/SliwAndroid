package es.uji.al259348.sliwandroid.core.services;

import rx.Observable;

public interface MQService {

    Observable<Void> publish(String topic, String msg);
    Observable<String> request(String topic, String msg);

}
