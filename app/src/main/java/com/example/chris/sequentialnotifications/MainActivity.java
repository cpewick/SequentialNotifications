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
    private Button cancelButton;
    private TimePicker alarmTimePicker;
    private TextView alarmTextView;
    private NotificationBuilder notiBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uniqueID = 0;
        this.notiBuilder = new NotificationBuilder(this, null, null);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create pendingIntent to set alerts at a later time
//                scheduleNotification();
//                scheduleNotification(getNotification("Time For: X"), 1000);
//                scheduleNotification("Time for Medication", 1000);
                notiBuilder.setBaseNotification("Time for Medication", 1000);
            }
        });

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationBuilder.cancelAllAlerts(getApplicationContext(), NotificationPublisher.PUBLISHER_ID);
            }
        });

        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
    }





//    private Notification getNotification(String content) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setContentTitle("Scheduled Notification");
//        builder.setContentText(content);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
////        long timeStamp = System.currentTimeMillis() + 1000*60;
////        builder.setWhen(timeStamp);
//        //PRIORITY_MAX places notification at top of notification list and defaults to expanded
//        builder.setPriority(NotificationCompat.PRIORITY_MAX);
//        //DEFUALT_VIBRATE causes vibration and pup-up notification (provided prioirty == HIGH or greater)
//        builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
//        builder.setCategory(NotificationCompat.CATEGORY_ALARM);
//
//        Log.i("In getNot", ""+uniqueID);
//        Log.i("In getNot", "Priority"+NotificationCompat.PRIORITY_MAX);
//
//        //Adds an action button which will appear under the notification
//        Intent actionIntent = new Intent(this, NotificationPublisher.class);
//        actionIntent.setAction("ACTION");
//        actionIntent.putExtra("ID", uniqueID);
//        PendingIntent pIntentAction = PendingIntent.getBroadcast(this, uniqueID, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        if (Build.VERSION.SDK_INT >= 16) {
//            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Action", pIntentAction).build();
//            builder.addAction(action);
//        }
//        if (Build.VERSION.SDK_INT < 16) {
//
//            return builder.getNotification();
//        } else {
//            Log.i("In getNotification", "SDK >=16");
//            return builder.build();
//        }
//
//    }
//
//    private void scheduleNotification(String content, int delay) {
//        Notification notification = getNotification(content);
//        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
////        notificationIntent.putExtra("notification-id", uniqueID);
////        notificationIntent.putExtra("notification", notification);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, uniqueID);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_CONTENT, content);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//        Log.i("In schedNotification", ""+delay + ":"+uniqueID);
//
//        uniqueID++;
//        Toast.makeText(this, "Alert set" , Toast.LENGTH_SHORT).show();
//    }
//
//    private void cancelAlert(int notificationID)
//    {
//        Intent intent = new Intent(this, NotificationPublisher.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),notificationID , intent, 0);
//        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(pendingIntent);
//
//        Toast.makeText(this, "Canceled alert for: X" , Toast.LENGTH_SHORT).show();
//    }
//
////    private void cancelAlerts()
////    {
////        NotificationPublisher publisher = new NotificationPublisher();
//////        publisher.cancelAlert(this);
////    }
//
//    private void scheduleNotification() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
//        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
//        calendar.set(Calendar.SECOND, 0);
//        long futureInMillis = calendar.getTimeInMillis();
//
//        Log.i("In scheduleNotification", ""+calendar.HOUR_OF_DAY);
//
////        scheduleNotification(getNotification("Time For: X"), calendar);
//        scheduleNotification(getNotification("Time For: X"), 1000);
//    }
//
//    private void scheduleNotification(Notification notification, Calendar calendar) {
//        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
//
////        notificationIntent.putExtra("notification-id", uniqueID);
////        notificationIntent.putExtra("notification", notification);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, uniqueID);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Log.i("In sNoti(cal)", ""+calendar.HOUR_OF_DAY);
//        Log.i("In sNoti(cal)", "Priority:"+notification.priority);
//
//        long futureInMillis = calendar.getTimeInMillis();
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//
//        //can use setRepeating
//        if (Build.VERSION.SDK_INT >= 19) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
//            Log.i("In schedNotification", "SDK >=19");
//        } else {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
//        }
//
//        if(calendar.MINUTE < 10) {
//            Toast.makeText(this, "Set alert for: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" +"0" + calendar.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Set alert for: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
//        }
//
//        uniqueID++;
//    }
//
//
//
//    private void scheduleNotification(Notification notification, int delay) {
//
//        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
////        notificationIntent.putExtra("notification-id", uniqueID);
////        notificationIntent.putExtra("notification", notification);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, uniqueID);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//        Log.i("In schedNotification", ""+delay + ":"+uniqueID);
//
//        uniqueID++;
//        Toast.makeText(this, "Alert set" , Toast.LENGTH_SHORT).show();
//    }

}
