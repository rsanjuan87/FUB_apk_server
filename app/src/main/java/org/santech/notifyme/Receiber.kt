package org.santech.notifyme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast


class Broadcaster : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val data = intent.getStringExtra("action")
            when(data){
                Defs.KEY_START_SERVICE->{
                    context.startService(Intent(context, NotificationService::class.java))
                }
                Defs.KEY_STOP_SERVICE->{
                    context.startService(Intent(context, NotificationService::class.java))
                }
                Defs.KEY_GET_LAUNCHERS->{
                    val pm = context.packageManager
                    var text = "["
                    val list = MsgHelper.getLauncherApps(context)
                    for (i in list!!) {
                        var s = "{"
                        val actyName = "${i.activityInfo.packageName}-${i.activityInfo.name}"
                        MsgHelper.saveIcon(context, actyName, i.loadIcon(pm))
                        s+= "\"LAUNCHER_LABEL\": \"${i.loadLabel(pm)}\" \t "
                        s+= "\"LAUNCHER_ACTY\": \"${i.activityInfo.name}\" \t "
                        s+= "\"LAUNCHER_PKG\": \"${i.activityInfo.packageName}\" \t "
                        s+="}"
                        text+= "$s, "
                    }
                    text +="]"
                    MsgHelper.print(context, Defs.KEY_GET_LAUNCHERS, text)
                }
                Defs.KEY_GET_PACKAGES->{

                }

            }


            Log.d("broadcast", data ?: "done");
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}