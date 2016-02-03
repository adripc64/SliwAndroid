package es.uji.al259348.sliwandroid.core.services;

import es.uji.al259348.sliwandroid.core.model.WifiScanSample;
import rx.Observable;

public interface WifiService {

    Observable<WifiScanSample> performScan();

}
