package com.example.zhanghehe.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.zhanghehe.myapplication.MainActivity

fun Activity.detectOpenGLES30():Boolean{
    val am=getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val info=am.deviceConfigurationInfo
    return (info.reqGlEsVersion>=0x30000)
}