package es.uji.al259348.sliwandroid.core.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import es.uji.al259348.sliwandroid.core.model.Sample;
import rx.Observable;
import rx.Subscriber;

public class WifiServiceImpl implements WifiService {

    public class WifiScanReceiver extends BroadcastReceiver {

        private Subscriber<? super Sample> subscriber;

        public WifiScanReceiver(Subscriber<? super Sample> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(this);

            Sample sample = new Sample(wifiManager.getScanResults());
            subscriber.onNext(sample);
        }

    }

    private Context context;
    private WifiManager wifiManager;

    public WifiServiceImpl(Context context) {
        this.context = context;
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public Observable<Sample> performScan() {
        return Observable.create(subscriber -> {
            context.registerReceiver(
                    new WifiScanReceiver(subscriber),
                    new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            );
            wifiManager.startScan();
        });
    }

    @Override
    public String getMacAddress() {
        return wifiManager.getConnectionInfo().getMacAddress();
    }
}
