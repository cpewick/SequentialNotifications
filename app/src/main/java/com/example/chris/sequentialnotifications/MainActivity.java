package com.example.chris.sequentialnotifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private int uniqueID;
    private Button button;
    private TimePicker alarmTimePicker;
    private TextView alarmTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uniqueID = 0;

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create pendingIntent to set alerts at a later time
                scheduleNotification();
            }
        });

        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
    }

    private void scheduleNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);
        long futureInMillis = calendar.getTimeInMillis();

        Log.i("In scheduleNotification", ""+calendar.HOUR_OF_DAY);

        scheduleNotification(getNotification("Time For: X"), calendar);
    }

    private void scheduleNotification(Notification notification, Calendar calendar) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);

//        notificationIntent.putExtra("notification-id", uniqueID);
//        notificationIntent.putExtra("notification", notification);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, uniqueID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i("In sNoti(cal)", ""+calendar.HOUR_OF_DAY);

        long futureInMillis = calendar.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        //can use setRepeating
        if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
            Log.i("In getNotification", "SDK >=19");
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        }

        if(calendar.MINUTE < 10) {
            Toast.makeText(this, "Set alert for: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" +"0" + calendar.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Set alert for: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
        }

        uniqueID++;
    }

    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
//        notificationIntent.putExtra("notification-id", uniqueID);
//        notificationIntent.putExtra("notification", notification);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, uniqueID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        uniqueID++;
        Toast.makeText(this, "Alert set" , Toast.LENGTH_SHORT).show();
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
//        long timeStamp = System.currentTimeMillis() + 1000*60;
//        builder.setWhen(timeStamp);
        //PRIORITY_MAX places notification at top of notification list and defaults to expanded
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        //DEFUALT_VIBRATE causes vibration and pup-up notification (provided prioirty == HIGH or greater)
        builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        builder.setCategory(NotificationCompat.CATEGORY_ALARM);

        Log.i("In getNot", ""+uniqueID);

        //Adds an action button which will appear under the notification
        Intent actionIntent = new Intent(this, NotificationPublisher.class);
        actionIntent.setAction("ACTION");
        actionIntent.putExtra("ID", uniqueID);
        PendingIntent pIntentAction = PendingIntent.getBroadcast(this, uniqueID, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 16) {
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Action", pIntentAction).build();
            builder.addAction(action);
        }
        if (Build.VERSION.SDK_INT < 16) {

            return builder.getNotification();
        } else {
            Log.i("In getNotification", "SDK >=16");
            return builder.build();
        }

    }

//    private class NotificationPublisher extends BroadcastReceiver {
//
//        public String NOTIFICATION_ID = "notification-id";
//        public String NOTIFICATION = "notification";
//
//        public void onReceive(Context context, Intent intent) {
//
//            //Behavior for the action buttons in the notification
//            if(intent.getAction() != null ) {
//                String action = intent.getAction();
//
//                Log.i("Alarm Test", action + intent.getIntExtra("ID", 0));
//                Toast.makeText(context, "Action " + intent.getIntExtra("ID", 0), Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//            Notification notification = intent.getParcelableExtra(NOTIFICATION);
//            int id = intent.getIntExtra(NOTIFICATION_ID, 0);
//            Log.i("In onReceive" , ""+id);
//            notificationManager.notify(id, notification);
//
//        }
//    }

}
