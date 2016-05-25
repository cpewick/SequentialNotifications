package com.example.chris.sequentialnotifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Chris on 5/24/2016.
 */
public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_CONTENT = "notification-content";
    public static int PUBLISHER_ID = 0;
    private NotificationBuilder notiBuilder;
    private Context context;
    private Intent oldIntent;

    public void onReceive(Context context, Intent intent) {
        //n real project, will need to read from databse to create new notification, and write the info from the actions

        //Behavior for the action buttons in the notification
        this.oldIntent = intent;
        if(intent.getAction() != null ) {
            String action = intent.getAction();

            Log.i("Alarm Test", action + intent.getIntExtra("ID", 0));
            Toast.makeText(context, "Action " + intent.getIntExtra("ID", 0), Toast.LENGTH_SHORT).show();
            return;
        }

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        Log.i("In onReceive" , "N_ID"+id);
        notificationManager.notify(id, notification);


        this.notiBuilder = new NotificationBuilder(context, intent, intent.getStringExtra(NOTIFICATION_CONTENT));
        this.notiBuilder.setNewNotification();
//        PUBLISHER_ID++;
        PUBLISHER_ID = id+1;
        Log.i("In onReceive" , "P_ID"+PUBLISHER_ID);
    }

}
