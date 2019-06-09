package com.example.zhanghehe.util

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log

fun GLSurfaceView.Renderer.loadShader(type:Int,shaderSrc:String):Int{
    var shader=0
    var compiled=IntArray(1)

    shader=GLES30.glCreateShader(type)

    if(shader==0){
        return 0
    }
    GLES30.glShaderSource(shader,shaderSrc)
    GLES30.glCompileShader(shader)
    GLES30.glGetShaderiv(shader,GLES30.GL_COMPILE_STATUS,compiled,0)

    if(compiled[0]==0){
        Log.e("loadShader",GLES30.glGetShaderInfoLog(shader))
        GLES30.glDeleteShader(shader)
        return 0
    }

    return shader

}