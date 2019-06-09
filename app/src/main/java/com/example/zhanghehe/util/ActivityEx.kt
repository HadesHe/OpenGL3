package com.example.zhanghehe.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context

fun Activity.detectOpenGLES30():Boolean{
    val am=getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val info=am.deviceConfigurationInfo
    return (info.reqGlEsVersion>=0x30000)
}