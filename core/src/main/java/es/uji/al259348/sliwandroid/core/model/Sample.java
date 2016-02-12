package es.uji.al259348.sliwandroid.core.model;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sample {

    public static class WifiScanResult {

        public String SSID;
        public String BSSID;
        public int level;

        public WifiScanResult() {
            super();
        }

        public WifiScanResult(ScanResult scanResult) {
            super();
            this.SSID = scanResult.SSID;
            this.BSSID = scanResult.BSSID;
            this.level = scanResult.level;
        }

        @Override
        public String toString() {
            return "WifiScanResult{" +
                    "SSID='" + SSID + '\'' +
                    ", BSSID='" + BSSID + '\'' +
                    ", level=" + level +
                    '}';
        }

    }

    private Date date;
    private Location location;
    private List<WifiScanResult> scanResults;

    public Sample() {
        super();
        this.date = new Date();
        this.location = null;
        this.scanResults = new ArrayList<>();
    }

    public Sample(List<ScanResult> scanResults) {
        super();
        this.date = new Date();
        this.scanResults = new ArrayList<>();
        for (ScanResult scanResult : scanResults) {
            WifiScanResult wifiScanResult = new WifiScanResult(scanResult);
            this.scanResults.add(wifiScanResult);
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<WifiScanResult> getScanResults() {
        return scanResults;
    }

    public void setScanResults(List<WifiScanResult> scanResults) {
        this.scanResults = scanResults;
    }

    @Override
    public String toString() {
        return "Sample{" +
                "date=" + date +
                ", location=" + location +
                ", scanResults=" + scanResults +
                '}';
    }

}
