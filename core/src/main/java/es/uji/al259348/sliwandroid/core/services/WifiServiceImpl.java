package es.uji.al259348.sliwandroid.core.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

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

            boolean isWifiEnabled = wifiManager.isWifiEnabled();
            Log.d("isWifiEnabled", "" + isWifiEnabled);

            if (!isWifiEnabled) {

                context.registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                        Log.d("WifiChangeStateReceiver", "" + wifiState);

                        if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                            context.unregisterReceiver(this);
                            context.registerReceiver(
                                    new WifiScanReceiver(subscriber),
                                    new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                            );

                            boolean res = wifiManager.startScan();
                            Log.d("WifiChangeStateReceiver", "startScanResult: " + res);
                        }
                    }
                }, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));

                boolean res = wifiManager.setWifiEnabled(true);
                Log.d("setWifiEnabled result", "" + res);

            } else {
                context.registerReceiver(
                        new WifiScanReceiver(subscriber),
                        new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                );

                wifiManager.startScan();
            }

        });
    }

    @Override
    public String getMacAddress() {
        return wifiManager.getConnectionInfo().getMacAddress();
    }
}
