package com.example.zhanghehe.myapplication

import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.zhanghehe.renderer.HelloTriangleRender
import com.example.zhanghehe.util.detectOpenGLES30
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mGLSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGLSurfaceView=GLSurfaceView(this)
        if(detectOpenGLES30()){
            mGLSurfaceView.setEGLContextClientVersion(CONTEXT_CLIENT_VERSION)
            mGLSurfaceView.setRenderer(HelloTriangleRender(this))

        }else{

            Log.e(TAG,"OpenGL ES 3.0 not supported on device. Existing...")
//            finish()
        }


        setContentView(mGLSurfaceView)

    }

    override fun onResume() {
        super.onResume()
        mGLSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGLSurfaceView.onPause()
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }

        private val CONTEXT_CLIENT_VERSION=3
        private val TAG=MainActivity::class.java.simpleName
    }
}
