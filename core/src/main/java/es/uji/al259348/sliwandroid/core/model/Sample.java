package es.uji.al259348.sliwandroid.core.model;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sample {

    private Date date;
    private List<ScanResult> scanResultList;

    public Sample() {
        this.date = new Date();
        this.scanResultList = new ArrayList<>();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<ScanResult> getScanResultList() {
        return scanResultList;
    }

    public void setScanResultList(List<ScanResult> scanResultList) {
        this.scanResultList = scanResultList;
    }

}
