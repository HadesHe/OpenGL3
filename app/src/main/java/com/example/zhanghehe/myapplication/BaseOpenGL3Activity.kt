package com.example.zhanghehe.myapplication

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.zhanghehe.util.detectOpenGLES30

abstract class BaseOpenGL3Activity:AppCompatActivity(){
    private lateinit var mGLSurface: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGLSurface=GLSurfaceView(this)
        if(detectOpenGLES30()){
            mGLSurface.setEGLContextClientVersion(3)
            mGLSurface.setRenderer(getRender())

        }else{
            Log.e(getTag(),"OpenGL 3.0 not supported on device.")
            finish()
        }

        setContentView(mGLSurface)
    }

    override fun onResume() {
        super.onResume()
        mGLSurface.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGLSurface.onPause()
    }

    abstract fun getTag():String

    abstract fun getRender():GLSurfaceView.Renderer
}