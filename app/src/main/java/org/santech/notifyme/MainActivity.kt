package org.santech.notifyme

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import org.santech.notifyme.databinding.ActivityMainBinding

import android.app.NotificationChannel
import android.app.NotificationManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity(), MyListener {
    lateinit var txtView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NotificationService().setListener(this)
        txtView = findViewById(R.id.textView)
        val btnCreateNotification: Button = findViewById(R.id.btnCreateNotification)
        btnCreateNotification.setOnClickListener {

            val mNotificationManager: NotificationManager? =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
            val mBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this@MainActivity, default_notification_channel_id)
            mBuilder.setContentTitle("My Notification")
            mBuilder.setContentText("Notification Listener Service Example")
            mBuilder.setTicker("Notification Listener Service Example")
            mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
            mBuilder.setAutoCancel(true)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val importance: Int = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "NOTIFICATION_CHANNEL_NAME",
                    importance
                )
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
                assert(mNotificationManager != null)
                mNotificationManager?.createNotificationChannel(notificationChannel)
            }
            assert(mNotificationManager != null)
            val id = System.currentTimeMillis().toInt()
            mNotificationManager?.notify(
                id,
                mBuilder.build()
            )

        }

    }

//    fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        getMenuInflater().inflate(R.menu.menu_main, menu) //Menu Resource, Menu
//        return true
//    }

//    fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.getItemId()) {
//            R.id.action_settings -> {
//                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
//                startActivity(intent)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }


    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        private const val default_notification_channel_id = "default"
    }

    override fun setValue(packageName: String?) {
        txtView.append(" \n $packageName")
    }
}