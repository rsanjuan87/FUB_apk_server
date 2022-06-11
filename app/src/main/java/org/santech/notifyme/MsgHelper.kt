package org.santech.notifyme

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.Drawable
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MsgHelper {
    companion object{
        fun print(context: Context, tag: String, text: String){
            Log.d(context.packageName, "$tag, $text")
        }

        fun printNotif(context: Context, tag: String, status: String, sbn: StatusBarNotification){
            saveAppIcon(context, sbn.packageName)
            val text = (if (sbn.notification.tickerText == null || sbn.notification.tickerText.isBlank()) "" else sbn.notification.tickerText.toString())
            print(context, tag,
                "{"+
                "\"status\": \"$status\" \t " +
                "\"NID\": \"${sbn.id}\" \t " +
                "\"MSG\": \"${text}\" \t " +
                "\"PKGID\": \"${sbn.packageName}\" \t " +
                "\"PKGNAME\": \"${getPkgName(context, sbn.packageName)}\" \t"
                +"}")
        }


        fun getPkgIcon(context: Context, pkgId: String) : Drawable? {

            val pm = context.packageManager
            val ai: ApplicationInfo? = try {
                pm.getApplicationInfo(pkgId, PackageManager.GET_META_DATA)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                return null
            }

            if (ai != null) {
                return pm.getApplicationIcon(ai)
            }
            return null
        }

        fun getPkgName(context: Context, pkgId: String) : String?{

            val pm = context.packageManager
            val ai: ApplicationInfo? = try {
                pm.getApplicationInfo(pkgId, PackageManager.GET_META_DATA)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                return null
            }

            if (ai != null) {
                return pm.getApplicationLabel(ai).toString()
            }
            return null
        }

        fun getLauncherApps(context: Context): MutableList<ResolveInfo>? {
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            return context?.packageManager?.queryIntentActivities(mainIntent, 0)
        }

        fun drawableToBitmap(pd: Drawable): Bitmap {
            return pd.toBitmap(pd.intrinsicWidth, pd.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        /**
         * @param dir you can get from many places like Environment.getExternalStorageDirectory() or mContext.getFilesDir() depending on where you want to save the image.
         * @param fileName The file name.
         * @param bm The Bitmap you want to save.
         * @param format Bitmap.CompressFormat can be PNG,JPEG or WEBP.
         * @param quality quality goes from 1 to 100. (Percentage).
         * @return true if the Bitmap was saved successfully, false otherwise.
         */
        fun saveBitmapToFile(
            dir: File?, fileName: String, bm: Bitmap,
            format: CompressFormat?, quality: Int
        ): Boolean {
            val imageFile = File(dir, fileName)
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(imageFile)
                bm.compress(format, quality, fos)
                fos.close()
                return true
            } catch (e: IOException) {
                e.printStackTrace()
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }
                }
            }
            return false
        }

        fun saveAppIcon(context: Context, pkgId: String){
            val draw: Drawable = (getPkgIcon(context, pkgId)?:context.getDrawable(R.drawable.ic_launcher_foreground)) as Drawable
            val bm = drawableToBitmap(draw)
            var doSave = true
            var dir = File( "/data/local/tmp/fup")
            if (!dir.exists()) {
                doSave = dir.mkdirs()
            }
            dir= File(dir, "icons")
            if (!dir.exists()) {
                doSave = dir.mkdirs()
            }

            if (File("/data/local/tmp/fup/icons/$pkgId").length() <= 1L) {
                print(context, Defs.KEY_GET_PACKAGE_ICON, "/data/local/tmp/fup/icons/$pkgId")
                saveBitmapToFile(dir, pkgId, bm, CompressFormat.PNG, 100)
            } else {
                Log.e("app", "Couldn't create target directory.")
            }
            print(context, Defs.KEY_SAVED_PACKAGE_ICON, "/data/local/tmp/fup/icons/$pkgId")

        }

        fun saveIcon(context: Context, name: String, draw: Drawable){
            val bm = drawableToBitmap(draw)
            var dir = File( "/data/local/tmp/fup/icons")

            if (File("/data/local/tmp/fup/icons/$name").length() <= 1L) {
                print(context, Defs.KEY_GET_PACKAGE_ICON, "/data/local/tmp/fup/icons/$name")
                saveBitmapToFile(dir, name, bm, CompressFormat.PNG, 100)
            } else {
                Log.e("app", "Couldn't create target directory.")
            }
            print(context, Defs.KEY_SAVED_PACKAGE_ICON, "/data/local/tmp/fup/icons/$name")

        }

    }
}