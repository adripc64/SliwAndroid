package es.uji.al259348.sliwandroid.core.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import es.uji.al259348.sliwandroid.core.R;
import es.uji.al259348.sliwandroid.core.receivers.TakeSampleReceiver;

public class AlarmServiceImpl implements AlarmService {

    private Context context;
    private AlarmManager alarmManager;

    public AlarmServiceImpl(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void setTakeSampleAlarm() {
        Intent intent = new Intent(context, TakeSampleReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long triggerAtMillis = SystemClock.elapsedRealtime();
        long intervalMillis = 1000 * context.getResources().getInteger(R.integer.intervalTakeSampleAlarmInSeconds);

        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerAtMillis,
                intervalMillis,
                pendingIntent);
    }

}
