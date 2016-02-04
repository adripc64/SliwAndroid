package es.uji.al259348.sliwandroid.core.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import es.uji.al259348.sliwandroid.core.services.AlarmService;
import es.uji.al259348.sliwandroid.core.services.AlarmServiceImpl;

public class BootCompletedReceiver extends BroadcastReceiver {

    public BootCompletedReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d("BootCompletedReceiver", "Boot completed, setting alarm for TakeSampleReceiver...");
            AlarmService alarmService = new AlarmServiceImpl(context);
            alarmService.setTakeSampleAlarm();
        }
    }

}
