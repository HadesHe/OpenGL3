package com.example.zhanghehe.myapplication

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.example.zhanghehe.util.loadProgramFromAsset
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MipMap2DActivity :BaseOpenGL3Activity(){
    override fun getTag(): String {
        return MipMap2DActivity::class.java.simpleName
    }

    override fun getRender(): GLSurfaceView.Renderer {
        return MipMap2DRender(this@MipMap2DActivity)
    }

    inner class MipMap2DRender(val context: Context) : GLSurfaceView.Renderer {
        private var mOffsetLoc: Int=0
        private var mSamplerLoc: Int=0
        private var mProgramsObject: Int=0
        private var mHeight: Int=0
        private var mWidth: Int=0

        override fun onDrawFrame(gl: GL10?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            mWidth=width
            mHeight=height
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            mProgramsObject= loadProgramFromAsset(context,"vertexShader.glsl","fragmentShader.glsl")
            mSamplerLoc=GLES30.glGetUniformLocation(mProgramsObject,"s_texture")
            mOffsetLoc=GLES30.glGetUniformLocation(mProgramsObject,"u_offset")
            mTextureId=createMipMappedTexture2D()

            GLES30.glClearColor(1.0f,1.0f,1.0f,0.0f)
        }

    }

    companion object{
        private val mVerticesData= floatArrayOf(
            -0.5f,0.5f,0.0f,1.5f,
            0.0f,0.0f,
            -0.5f,-0.5f,0.0f,0.75f,
            0.0f,1.0f,
            0.5f,-0.5f,0.0f,0.75f,
            1.0f,1.0f,
            0.5f,0.5f,0.0f,1.5f,
            1.0f,0.0f
        )

        private val mIndicesData= shortArrayOf(
            0,1,2,0,2,3
        )
    }

}