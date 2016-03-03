package es.uji.al259348.sliwandroid.core.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

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

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("WifiScanReceiver", "Scan results available.");
            onScanPerformed();

        }

    }

    private Context context;
    private WifiManager wifiManager;

    private WifiScanReceiver wifiScanReceiver;
    private Queue<Subscriber<? super Sample>> wifiScanSubscribers;

    private WifiStateChangedReceiver wifiStateChangedReceiver;
    private Queue<Subscriber> wifiStateChangedSubscribers;

    public WifiServiceImpl(Context context) {
        this.context = context;
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        this.wifiScanSubscribers = new LinkedList<>();
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
    public String getMacAddress() {
        return wifiManager.getConnectionInfo().getMacAddress();
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

        while (!wifiStateChangedSubscribers.isEmpty()) {
            Subscriber subscriber = wifiStateChangedSubscribers.poll();
            subscriber.onCompleted();
        }
    }

    @Override
    public Observable<Sample> takeSample() {
        return Observable.create(subscriber -> {

            Log.d("WifiService", "It has been requested to take a sample.");
            wifiScanSubscribers.add(subscriber);
            if (wifiScanReceiver != null) {
                Log.d("WifiService", "Another request to take a sample is in process, queueing this request.");
            } else {
                enableWifi()
                        .doOnCompleted(this::performScan)
                        .subscribe();
            }

        });
    }

    private void performScan() {
        Log.d("WifiService", "It has been requested to perform a Wifi scan.");

        wifiScanReceiver = new WifiScanReceiver();
        context.registerReceiver(
                wifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        );
        Log.d("WifiService", wifiScanReceiver.getClass().getSimpleName() + " registered.");

        wifiManager.startScan();
        Log.d("WifiService", "Performing a Wifi scan...");
    }

    private void onScanPerformed() {
        Log.d("WifiService", "The scan has been performed.");
        unregisterReceiver(wifiScanReceiver);
        wifiScanReceiver = null;

        Sample sample = new Sample(wifiManager.getScanResults());

        Subscriber<? super Sample> subscriber = wifiScanSubscribers.poll();
        if (subscriber != null) {
            Log.d("WifiService", "The sample has been taken.");
            subscriber.onNext(sample);
        }

        if (!wifiScanSubscribers.isEmpty()) {
            Log.d("WifiService", "The are requests to take a sample in the queue.");
            performScan();
        }
    }

}
