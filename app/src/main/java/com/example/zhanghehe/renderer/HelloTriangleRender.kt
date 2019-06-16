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
        /**
         * index: 0~最大顶点属性数-1
         * size: 顶点数组中为索引引用的顶点属性所指定的分量数量 1~4
         * type: 数据格式
         * normalized: 用于表示非浮点数数据格式类型在转换为浮点数时是否应该规范化
         * stride: 索引I 和(I+1)表示的顶点数据之间的位移
         * ptr:顶点缓冲区
         */
        GLES30.glVertexAttribPointer(0,3,GLES30.GL_FLOAT,false,0,mVertices)
        GLES30.glEnableVertexAttribArray(0)

        /**
         * mode：图元类型
         * first：启用的顶点数组中的起始顶点索引
         * count:指定要绘制的顶点数量
         */
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,3)

//        启用图元重启
        GLES30.glEnable(GLES30.GL_PRIMITIVE_RESTART_FIXED_INDEX)
//        关闭图元重启
        GLES30.glDisable(GLES30.GL_PRIMITIVE_RESTART_FIXED_INDEX)

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

        /**
         * program: 程序对象名称
         * index: 通用顶点属性索引
         * name: 属性变量名称
         */
        GLES30.glBindAttribLocation(programObject,0,"vPosition")
//        确保顶点着色器写入片段着色器使用的所有顶点着色器输出变量，确保任何在顶点和片段着色其中都声明的统一变量和统一变量缓冲区的类型相符
        GLES30.glLinkProgram(programObject)
//      GL_ACTIVE_ATTRIBUTES:返回顶点着色器中活动属性的数量
//      GL_ACTIVE_ATTRIBUTE_MAX_LENGTH：最大属性名称的最大长度
//      GL_ACTIVE_UNIFORMS：返回活动统一变量的数量
//      GL_ACTIVE_UNIFORM_MAX_LENGTH：最大统一变量名称的最大长度
//      GL_ATTACHED_SHADER:连接到程序对象的着色器数量
//        GL_DELETE_STATUS：是否已经标记为删除
//        GL_INFO_LOG_LENGTH:信息日志
        GLES30.glGetProgramiv(programObject,GLES30.GL_LINK_STATUS,linked,0)

        if(linked[0]==0){
            Log.e(TAG,"Error linking program:")
            Log.e(TAG,GLES30.glGetProgramInfoLog(programObject))
            GLES30.glDeleteProgram(programObject)
            return
        }
//        设置为活动对象
        GLES30.glUseProgram(programObject)
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
