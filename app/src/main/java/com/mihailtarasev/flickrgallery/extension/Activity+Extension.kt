package com.mihailtarasev.flickrgallery.extension

import android.app.Activity
import android.os.Build
import android.view.Display

fun Activity.displayActivity(): Display = when {
    Build.VERSION.SDK_INT >= 30 -> this.display!!
    else -> @Suppress("DEPRECATION") windowManager.defaultDisplay
}