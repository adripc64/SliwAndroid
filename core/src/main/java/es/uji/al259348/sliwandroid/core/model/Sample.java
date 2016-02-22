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

    private String id;
    private String userId;
    private String deviceId;
    private String location;
    private Date date;
    private List<WifiScanResult> scanResults;
    private boolean valid;

    public Sample() {
        super();
        this.date = new Date();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "Sample{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", scanResults=" + scanResults +
                ", valid=" + valid +
                '}';
    }

}
