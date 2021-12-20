package android.julianmf.studentTermTracker.Alarms;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.julianmf.studentTermTracker.R;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class CourseEndDateReceiver extends BroadcastReceiver {
    String channel_id = "courseNotification";
    static int courseNotificationId;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("key2"), Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channel_id);
        Notification notification2 = new NotificationCompat.Builder(context,channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(intent.getStringExtra("key2"))
                .setContentTitle("Course End Notification").build();
        NotificationManager notificationManager2 = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager2.notify(courseNotificationId++, notification2);
    }

    private void createNotificationChannel(Context context, String CHANNEL_ID){
        CharSequence name = context.getResources().getString(R.string.channel_name);
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager2 = context.getSystemService(NotificationManager.class);
        notificationManager2.createNotificationChannel(channel);
    }
}