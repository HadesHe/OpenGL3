package com.example.zhanghehe.myapplication

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30
import android.opengl.Matrix
import android.os.SystemClock
import android.support.annotation.IntDef
import com.example.zhanghehe.util.loadProgram
import com.example.zhanghehe.util.loadShader
import java.lang.RuntimeException
import java.lang.annotation.RetentionPolicy
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class Lesson1Activity:BaseOpenGL3Activity() {
    override fun getTag(): String {
        return Lesson1Activity::class.java.simpleName
    }

    override fun getRender(): GLSurfaceView.Renderer {
        return Lesson1Render()
    }

    inner class Lesson1Render:GLSurfaceView.Renderer{


        private var mHeight=0
        private var mWidth=0
        private var mProgramObject: Int=0
        private var mVertices: FloatBuffer

        init {
            // Define points for equilateral triangles.

            // This triangle is red, green, and blue.
            val mVerticesData = floatArrayOf(
              0.0f,0.5f,0.0f,
                -0.5f,-0.5f,0.0f,
                0.5f,-0.5f,0.0f
            )

            mVertices=ByteBuffer.allocateDirect(mVerticesData.size*4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
            mVertices.put(mVerticesData).position(0)

        }

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glViewport(0,0,mWidth,mHeight)
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
            GLES30.glUseProgram(mProgramObject)

            //设置 layout=0 的常量属性值 0.5,0.5,0.5f,1.0f
            GLES30.glVertexAttrib4f(0,0.5f,0.5f,0.5f,1.0f)

            mVertices.position(0)

            GLES30.glVertexAttribPointer(1,3,GLES30.GL_FLOAT,
                false,
                0,mVertices)

            //开启 layout=1 的顶点数组设置
            GLES30.glEnableVertexAttribArray(1)
            //设置绘制方式
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,3)
            //关闭 layout=1 的顶点数组设置
            GLES30.glDisableVertexAttribArray(1)


        }


        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

            mWidth=width
            mHeight=height

        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            mProgramObject= loadProgram(vertexShader, fragmentShader)

            GLES30.glClearColor(1.0f,1.0f,1.0f,0.0f)
        }

    }

    companion object{
        val vertexShader="""
            #version 300 es
            layout(location=0) in vec4 a_Color;
            layout(location=1) in vec4 a_Position;
            out vec4 v_Color;
            void main(){
                v_Color=a_Color;
                gl_Position=a_Position;
            }
        """.trimIndent()
        val fragmentShader="""
            #version 300 es
            precision mediump float;
            in vec4 v_Color;
            out vec4 o_fragColor;
            void main(){
                o_fragColor=v_Color;
            }
        """.trimIndent()
    }


}
