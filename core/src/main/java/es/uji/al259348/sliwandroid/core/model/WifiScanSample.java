package es.uji.al259348.sliwandroid.core.model;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WifiScanSample {

    public static class WifiScanResult {

        public String SSID;
        public String BSSID;
        public String capabilities;
        public int level;

        public WifiScanResult() {
            super();
        }

        public WifiScanResult(ScanResult scanResult) {
            super();
            this.SSID = scanResult.SSID;
            this.BSSID = scanResult.BSSID;
            this.capabilities = scanResult.capabilities;
            this.level = scanResult.level;
        }

        @Override
        public String toString() {
            return "WifiScanResult{" +
                    "SSID='" + SSID + '\'' +
                    ", BSSID='" + BSSID + '\'' +
                    ", capabilities='" + capabilities + '\'' +
                    ", level=" + level +
                    '}';
        }
    }

    private Date date;
    private List<WifiScanResult> scanResults;

    public WifiScanSample() {
        super();
        this.date = new Date();
        this.scanResults = new ArrayList<>();
    }

    public WifiScanSample(List<ScanResult> scanResults) {
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

    public List<WifiScanResult> getScanResults() {
        return scanResults;
    }

    public void setScanResults(List<WifiScanResult> scanResults) {
        this.scanResults = scanResults;
    }

    @Override
    public String toString() {
        return "WifiScanSample{" +
                "date=" + date +
                ", scanResults=" + scanResults +
                '}';
    }
}
