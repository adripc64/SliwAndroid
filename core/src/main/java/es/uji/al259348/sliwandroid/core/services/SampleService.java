package es.uji.al259348.sliwandroid.core.services;

import es.uji.al259348.sliwandroid.core.model.Sample;
import rx.Observable;

public interface SampleService extends Service {

    Observable<Sample> take();

    Sample save(Sample sample);

}
