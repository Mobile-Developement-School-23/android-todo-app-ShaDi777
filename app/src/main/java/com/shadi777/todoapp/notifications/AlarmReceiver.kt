package com.shadi777.todoapp.notifications

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.impl.utils.ForceStopRunnable.BroadcastReceiver
import com.shadi777.todoapp.R
import com.shadi777.todoapp.ui.screen.MainActivity
import com.shadi777.todoapp.util.Constants

@SuppressLint("RestrictedApi")
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        val id = intent.getStringExtra(Constants.INTENT_ID_KEY) ?: "Unknown ID"
        val title = intent.getStringExtra(Constants.INTENT_ID_TITLE_KEY) ?: "Unknown Title"
        val priority = intent.getStringExtra(Constants.INTENT_ID_IMPORTANCE_KEY) ?: "Unknown Priority"
        val priorityText = context.getString(R.string.priority) + ":" + priority

        val resultIntent = Intent(context, MainActivity::class.java).putExtra(Constants.TASK_ID_KEY, id)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val notification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(id)
            .setContentText(priorityText)
            .setPriority(
                when(priority) {
                    context.getString(R.string.high_priority) -> NotificationCompat.PRIORITY_HIGH
                    context.getString(R.string.low_priority) -> NotificationCompat.PRIORITY_LOW
                    else -> NotificationCompat.PRIORITY_DEFAULT
                }

            )
            //.setStyle(NotificationCompat.BigTextStyle().bigText(TEXT))
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }
}