package es.uji.al259348.sliwandroid.core.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import es.uji.al259348.sliwandroid.core.model.Sample;
import rx.Observable;
import rx.Subscriber;

public class WifiServiceImpl implements WifiService {

    private class WifiStateChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            Log.d("WifiStateChangedReceive", "Wifi state changed to " + wifiState);

            if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                onWifiEnabled();
            }

        }

    }

    private class WifiScanReceiver extends BroadcastReceiver {

        private Subscriber<? super Sample> subscriber;

        public WifiScanReceiver(Subscriber<? super Sample> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this);
            wifiScanReceiver = null;

            Sample sample = new Sample(wifiManager.getScanResults());
            subscriber.onNext(sample);
        }

    }

    private Context context;
    private WifiManager wifiManager;

    private WifiScanReceiver wifiScanReceiver;

    private WifiStateChangedReceiver wifiStateChangedReceiver;
    private List<Subscriber> wifiStateChangedSubscribers;

    public WifiServiceImpl(Context context) {
        this.context = context;
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        this.wifiStateChangedSubscribers = new LinkedList<>();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(wifiScanReceiver);
        unregisterReceiver(wifiStateChangedReceiver);
    }

    private void unregisterReceiver(BroadcastReceiver receiver) {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            Log.d("WifiService", receiver.getClass().getSimpleName() + " unregistered.");

        }
    }

    @Override
    public Observable<Void> enableWifi() {
        return Observable.create(subscriber -> {

            Log.d("WifiService", "It has been requested to enable the Wifi.");
            boolean isWifiEnabled = wifiManager.isWifiEnabled();
            if (isWifiEnabled) {
                Log.d("WifiService", "The Wifi is already enabled.");
                subscriber.onCompleted();
            } else {
                wifiStateChangedSubscribers.add(subscriber);

                if (wifiStateChangedReceiver != null) {
                    Log.d("WifiService", "Another request to enable the Wifi is in process.");
                } else {
                    wifiStateChangedReceiver = new WifiStateChangedReceiver();
                    context.registerReceiver(
                            wifiStateChangedReceiver,
                            new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
                    );
                    Log.d("WifiService", wifiStateChangedReceiver.getClass().getSimpleName() + " registered.");

                    wifiManager.setWifiEnabled(true);
                    Log.d("WifiService", "Enabling the Wifi...");
                }
            }

        });
    }

    private void onWifiEnabled() {
        Log.d("WifiService", "The Wifi has been enabled.");
        unregisterReceiver(wifiStateChangedReceiver);
        wifiStateChangedReceiver = null;

        for (Subscriber subscriber : wifiStateChangedSubscribers) {
            subscriber.onCompleted();
        }
        wifiStateChangedSubscribers.clear();
    }

    @Override
    public Observable<Sample> performScan() {
        return Observable.create(subscriber -> {

            enableWifi()
                    .doOnCompleted(() -> {
                        wifiScanReceiver = new WifiScanReceiver(subscriber);
                        context.registerReceiver(
                                wifiScanReceiver,
                                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                        );

                        wifiManager.startScan();
                    })
                    .subscribe();

        });
    }

    @Override
    public String getMacAddress() {
        return wifiManager.getConnectionInfo().getMacAddress();
    }
}
