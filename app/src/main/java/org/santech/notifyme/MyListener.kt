package org.santech.notifyme

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

interface MyListener {
    fun setValue(packageName: String?)
}

class NotificationService : NotificationListenerService() {
    private val TAG: String = "NOTIFICATION_CHANNEL"
    private lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        MsgHelper.printNotif(context, TAG, "posted", sbn)
        try {
            myListener!!.setValue("Post: " + sbn.packageName)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        MsgHelper.printNotif(context, TAG, "removed", sbn)
        try {
            myListener!!.setValue("Remove: " + sbn.packageName)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun setListener(myListener: MyListener?) {
        Companion.myListener = myListener
    }

    companion object {
        var myListener: MyListener? = null
    }


}


