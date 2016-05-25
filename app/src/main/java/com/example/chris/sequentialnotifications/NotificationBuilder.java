package com.example.chris.sequentialnotifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.net.Inet4Address;
import java.util.Calendar;

/**
 * Created by Matthew on 05/25/16.
 */
public class NotificationBuilder {

    private int uniqueID;
    private Context context;
    private Notification oldNotification;
    private Intent oldIntent;
    private String oldContent;
    private int oldID;
    private NotificationPublisher notiPub;

    public NotificationBuilder(Context con, Intent oldIntent, String oldContent) {
        this.context = con;
        if(oldIntent != null) {
            this.oldIntent = oldIntent;
        }
        if(oldContent != null) {
            this.oldContent = oldContent;
            this.oldID = oldIntent.getIntExtra(NotificationPublisher.NOTIFICATION_ID, 0);
//            this.uniqueID = oldID+1;
            this.uniqueID = oldID+1;
        }else {
            this.uniqueID = 0;
        }
        Log.i("In notifBuilder()", "ID"+uniqueID);
    }

    public void setBaseNotification(String content, int delay) {
        scheduleNotification(content, delay);
    }

    public void setNewNotification() {
        scheduleNotification(oldContent, 5000);
    }

    public void cancelAlert(int notificationID)
    {
        Intent intent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,notificationID , intent, 0);
        Log.i("In BgetCancel", "Canceling:"+(notificationID));
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(context, "Canceled alert for: X" , Toast.LENGTH_SHORT).show();
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
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

        Log.i("In BgetNot", "ID:" + uniqueID);

        //Adds an action button which will appear under the notification
        Intent actionIntent = new Intent(context, NotificationPublisher.class);
        actionIntent.setAction("ACTION");
        actionIntent.putExtra("ID", uniqueID);
        PendingIntent pIntentAction = PendingIntent.getBroadcast(context, uniqueID, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 16) {
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Action", pIntentAction).build();
            builder.addAction(action);
        }
        if (Build.VERSION.SDK_INT < 16) {

            return builder.getNotification();
        } else {
//            Log.i("In getNotification", "SDK >=16");
            return builder.build();
        }

    }

    private void scheduleNotification(String content, int delay) {
        Notification notification = getNotification(content);
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, uniqueID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_CONTENT, content);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, uniqueID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        Log.i("In BschedNotification", "" + delay + " ID:" + uniqueID);

        this.uniqueID++;
        Toast.makeText(context, "Alert set", Toast.LENGTH_SHORT).show();
    }

    private void scheduleNotification(String content, Calendar calendar) {
        Notification notification = getNotification(content);
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, uniqueID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_CONTENT, content);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, uniqueID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i("In sNoti(cal)", "" + calendar.HOUR_OF_DAY);
        Log.i("In sNoti(cal)", "Priority:" + notification.priority);

        long futureInMillis = calendar.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        //can use setRepeating
        if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
            Log.i("In BschedNotification", "SDK >=19");
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        }

        if (calendar.MINUTE < 10) {
            Toast.makeText(context, "Set alert for: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + "0" + calendar.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Set alert for: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
        }

        this.uniqueID++;
    }

    public static void cancelAllAlerts(Context con, int notificationID)
    {
        Intent intent = new Intent(con, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(con, notificationID, intent, 0);
        AlarmManager alarmManager = (AlarmManager)con.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.i("In BgetCancel", "Canceling:"+(notificationID));
    }

    public int getUniqueID() {
        Log.i("In getID", "ID:" +uniqueID);
        return uniqueID;
    }
}