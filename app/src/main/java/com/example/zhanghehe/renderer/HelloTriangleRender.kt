package com.example.zhanghehe.renderer

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class HelloTriangleRender(context: Context) : GLSurfaceView.Renderer {

    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private var mProgramObject: Int = 0
    private var mVertices: FloatBuffer

    init {
        mVertices=ByteBuffer.allocateDirect(mVerticesData.size*4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mVertices.put(mVerticesData).position(0)
    }

    private fun loadShader(type:Int,shaderSrc:String):Int{
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
            Log.e(TAG,GLES30.glGetShaderInfoLog(shader))
            GLES30.glDeleteShader(shader)
            return 0
        }

        return shader
    }


    override fun onDrawFrame(gl: GL10?) {
        GLES30.glViewport(0,0,mWidth,mHeight)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glUseProgram(mProgramObject)
        GLES30.glVertexAttribPointer(0,3,GLES30.GL_FLOAT,false,0,mVertices)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,3)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        mWidth=width
        mHeight=height
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        var linked=IntArray(1)
        var vertexShader=loadShader(GLES30.GL_VERTEX_SHADER, vShaderStr)
        var fragmentShader=loadShader(GLES30.GL_FRAGMENT_SHADER, fShaderStr)

        var programObject=GLES30.glCreateProgram()
        if(programObject==0){
            return
        }

        GLES30.glAttachShader(programObject,vertexShader)
        GLES30.glAttachShader(programObject,fragmentShader)

        GLES30.glBindAttribLocation(programObject,0,"vPosition")
        GLES30.glLinkProgram(programObject)
        GLES30.glGetProgramiv(programObject,GLES30.GL_LINK_STATUS,linked,0)

        if(linked[0]==0){
            Log.e(TAG,"Error linking program:")
            Log.e(TAG,GLES30.glGetProgramInfoLog(programObject))
            GLES30.glDeleteProgram(programObject)
            return
        }
        mProgramObject=programObject
        GLES30.glClearColor(1.0f,1.0f,1.0f,0.0f)
    }

    companion object{
        private val mVerticesData= floatArrayOf(
            0.0f,0.5f,0.0f,
            -0.5f,-0.5f,0.0f,
            0.5f,-0.5f,0.0f
        )

        val vShaderStr="""
            #version 300 es
            in vec4 vPosition;
            void main()
            {
                gl_Position=vPosition;
            }
        """.trimIndent()

        val fShaderStr="""
            #version 300 es
            precision mediump float;
            out vec4 fragColor;
            void main()
            {
                fragColor=vec4(1.0,1.0,0.0,1.0f);
            }
        """.trimIndent()

        private val TAG=HelloTriangleRender::class.java.simpleName
    }

}
