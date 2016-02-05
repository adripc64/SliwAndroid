package es.uji.al259348.sliwandroid.core.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import es.uji.al259348.sliwandroid.core.R;
import es.uji.al259348.sliwandroid.core.receivers.TakeSampleReceiver;

public class AlarmServiceImpl implements AlarmService {

    private Context context;
    private AlarmManager alarmManager;

    private PendingIntent pendingIntentTakeSampleReceiver;

    public AlarmServiceImpl(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intentTakeSampleReceiver = new Intent(context, TakeSampleReceiver.class);
        pendingIntentTakeSampleReceiver = PendingIntent.getBroadcast(context, 0, intentTakeSampleReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void setTakeSampleAlarm() {
        Log.d("AlarmService", "Setting TakeSampleAlarm");
        long triggerAtMillis = SystemClock.elapsedRealtime();
        long intervalMillis = 1000 * context.getResources().getInteger(R.integer.intervalTakeSampleAlarmInSeconds);

        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerAtMillis,
                intervalMillis,
                pendingIntentTakeSampleReceiver);
    }

    @Override
    public void cancelTakeSampleAlarm() {
        Log.d("AlarmService", "Canceling TakeSampleAlarm");
        alarmManager.cancel(pendingIntentTakeSampleReceiver);
    }

}
