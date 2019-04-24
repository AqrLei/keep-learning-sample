package com.open.aqrlei.ipc

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

/**
 * @author aqrlei on 2018/9/13
 */
fun queryActivities(context: Context, intent: Intent): Boolean {
    return context.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY) != null
}