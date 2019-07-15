package com.example.zhanghehe.myapplication

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.example.zhanghehe.util.loadProgramFromAsset
import com.example.zhanghehe.util.loadTextureFromAsset
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MultiTextureActivity:BaseOpenGL3Activity() {
    override fun getTag(): String {
        return MultiTextureActivity::class.java.simpleName
    }

    override fun getRender(): GLSurfaceView.Renderer {
        return MultiTextureRenderer(this@MultiTextureActivity)
    }

    inner class MultiTextureRenderer(val context: Context) : GLSurfaceView.Renderer {

        private var mLightMapTexId: Int=0
        private var mBaseMapTexId: Int=0
        private var mLightMapLoc: Int=0
        private var mBaseMapLoc: Int=0
        private var mProgramObject: Int=0
        private var mHeight: Int=0
        private var mWidth: Int=0
        private var mIndices: ShortBuffer
        private var mVertices: FloatBuffer

        init {
            mVertices=ByteBuffer.allocateDirect(mVerticesData.size*4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
            mVertices.put(mVerticesData).position()
            mIndices=ByteBuffer.allocateDirect(mIndicesData.size*2)
                .order(ByteOrder.nativeOrder()).asShortBuffer()
            mIndices.put(mIndicesData).position(0)
        }

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glViewport(0,0,mWidth,mHeight)
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

            GLES30.glUseProgram(mProgramObject)

            mVertices.position(0)

            GLES30.glVertexAttribPointer(0,3,GLES30.GL_FLOAT,
                false,5*4,mVertices)

            mVertices.position(3)
            GLES30.glVertexAttribPointer(1,2,GLES30.GL_FLOAT,
                false,5*4,mVertices)

            GLES30.glEnableVertexAttribArray(0)
            GLES30.glEnableVertexAttribArray(1)

            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,mBaseMapTexId)

            GLES30.glUniform1i(mBaseMapLoc,0)

            GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,mLightMapTexId)

            GLES30.glUniform1i(mLightMapLoc,1)
            GLES30.glDrawElements(GLES30.GL_TRIANGLES,6,GLES30.GL_UNSIGNED_SHORT,mIndices)

        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            mWidth=width
            mHeight=height
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            mProgramObject= loadProgramFromAsset(context,
                "multitextvertext.glsl",
                "multitextfragment.glsl")

            mBaseMapLoc=GLES30.glGetUniformLocation(mProgramObject,"s_baseMap")
            mLightMapLoc=GLES30.glGetUniformLocation(mProgramObject,"s_lightMap")

            mBaseMapTexId= loadTextureFromAsset(context,"textures/basemap.png")
            mLightMapTexId= loadTextureFromAsset(context,"textures/lightmap.png")

            GLES30.glClearColor(1.0f,1.0f,1.0f,0.0f)

        }

    }

    companion object{
        private val mVerticesData= floatArrayOf(
            -0.5f,0.5f,0.0f,
            0.0f,0.0f,
            -0.5f,-0.5f,0.0f,
            0.0f,1.0f,
            0.5f,-0.5f,0.0f,
            1.0f,1.0f,
            0.5f,0.5f,0.0f,
            1.0f,0.0f
        )

        private val mIndicesData= shortArrayOf(
            0,1,2,0,2,3
        )
    }

}
