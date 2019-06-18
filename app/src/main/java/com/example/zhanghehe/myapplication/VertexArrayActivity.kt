package com.example.zhanghehe.myapplication

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.example.zhanghehe.util.loadProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class VertexArrayActivity:BaseOpenGL3Activity(){
    override fun getTag(): String {
        return VertexArrayActivity::class.java.simpleName
    }

    override fun getRender(): GLSurfaceView.Renderer {
        return VAORender(this@VertexArrayActivity)
    }


    inner class VAORender(context:Context) : GLSurfaceView.Renderer {
        private var mProgramObject: Int = 0
        private var mHeight: Int=0
        private var mWidth: Int = 0
        private var mIndices: ShortBuffer
        private var mVertices: FloatBuffer

        init {
            mVertices=ByteBuffer.allocateDirect(mVerticesData.size*4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
            mVertices.put(mVerticesData).position(0)

            mIndices=ByteBuffer.allocateDirect(mIndicesData.size*2)
                .order(ByteOrder.nativeOrder()).asShortBuffer()
            mIndices.put(mIndicesData).position(0)
        }
        override fun onDrawFrame(gl: GL10?) {
            GLES30.glViewport(0,0,mWidth,mHeight)
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
            GLES30.glUseProgram(mProgramObject)
            GLES30.glBindVertexArray(mVAOId[0])
            GLES30.glDrawElements(GLES30.GL_TRIANGLES, mIndicesData.size,GLES30.GL_UNSIGNED_SHORT,0)

            GLES30.glBindVertexArray(0)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            mWidth=width
            mHeight=height
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            mProgramObject= loadProgram(vShaderStr, fShaderStr)

            GLES30.glGenBuffers(2, mVBOId,0)
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOId[0])
            mVertices.position(0)

            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, mVerticesData.size*4,
                mVertices,GLES30.GL_STATIC_DRAW)

            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,
                mVBOId[1])
            mIndices.position(0)
            GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER,2* mIndicesData.size,
                mIndices,GLES30.GL_STATIC_DRAW)

            /**
             * 创建新的数组对象
             * n：返回数组对象的数量
             * arrays:分配的顶点数组对象存储的位置
             * offset：存储位置的起始位置
             */
            GLES30.glGenVertexArrays(1, mVAOId,0)

            /**
             * 绑定顶点数组对象
             */
            GLES30.glBindVertexArray(mVAOId[0])

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOId[0])
            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOId[1])

            GLES30.glEnableVertexAttribArray(VERTEX_POS_INDX)
            GLES30.glEnableVertexAttribArray(VERTEX_COLOR_INDX)

            GLES30.glVertexAttribPointer(VERTEX_POS_INDX, VERTEXT_POS_SIZE,
                GLES30.GL_FLOAT,false, VERTEX_STRIDE,0)

            GLES30.glVertexAttribPointer(VERTEX_COLOR_INDX, VERTEXT_COLOR_SIZE,
                GLES30.GL_FLOAT,false, VERTEX_STRIDE,(VERTEXT_POS_SIZE*4))

            GLES30.glBindVertexArray(0)
            GLES30.glClearColor(1.0f,1.0f,1.0f,0.0f)
        }
    }

    companion object{
        private val vShaderStr="""
            #version 300 es
            layout(location=0) in vec4 a_position;
            layout(location=1) in vec4 a_color;
            out vec4 v_color;
            void main(){
                v_color=a_color;
                gl_Position=a_position;
            }
        """.trimIndent()

        private val fShaderStr="""
            #version 300 es
            precision mediump float;
            in vec4 v_color;
            out vec4 o_fragColor;
            void main(){
                o_fragColor=v_color;
            }
        """.trimIndent()

        private var mVBOId=IntArray(2)
        private var mVAOId=IntArray(1)

//        x,y,z,r,g,b,a
        private val mVerticesData= floatArrayOf(
            0.0f,0.5f,0.0f,1.0f,0.0f,0.0f,1.0f,
            -0.5f,-0.5f,0.0f,0.0f,1.0f,0.0f,1.0f,
            0.5f,-0.5f,0.0f,0.0f,0.0f,1.0f,1.0f
        )

        private val mIndicesData= shortArrayOf(
            0,1,2
        )

        private val VERTEXT_POS_SIZE=3
        private val VERTEXT_COLOR_SIZE=4

        private val VERTEX_POS_INDX=0
        private val VERTEX_COLOR_INDX=1

        private val VERTEX_STRIDE=(4*(VERTEXT_POS_SIZE+ VERTEXT_COLOR_SIZE))
    }

}