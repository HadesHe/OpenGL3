package com.example.zhanghehe.util

import android.content.Context
import android.opengl.GLES30
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import javax.microedition.khronos.opengles.GL

/**
 * type: GLE30.GL_VERTEX_SHADER or GLE30.GL_FRAGMENT_SHADER
 */
fun loadShader(type:Int,shaderSrc:String):Int{
    var shader=0
    var compiled=IntArray(1)

    shader=GLES30.glCreateShader(type)

    if(shader==0){
        return 0
    }
    GLES30.glShaderSource(shader,shaderSrc)
    GLES30.glCompileShader(shader)

    /**
     * pname:GL_COMPILE_STATUS: 编译成功为1 编译失败为0
     *      GL_SHADER_TYPE: GL_VERTEXT_SHADER or GL_FRAGMENT_SHADER
     *      GL_SHADER_SOURCE_LENGTH:源代码长度（包括NULL）
     *      GL_DELETE_STATUE:glDelteShader标记是否为删除
     */
    GLES30.glGetShaderiv(shader,GLES30.GL_COMPILE_STATUS,compiled,0)

    if(compiled[0]==0){

        /**
         * glGetShaderInfoLog错误信息获取
         */
        Log.e("loadShader",GLES30.glGetShaderInfoLog(shader))
        //glDeleteShader 并不会立刻删除着色器，而是将着色器标记为删除，在着色器不再连接到任何程序对象时，它的内存将被释放
        GLES30.glDeleteShader(shader)
        return 0
    }

    return shader

}

fun loadProgramFromAsset(context: Context,vertexShaderFileName:String,fragShaderFileName:String):Int{
    var vertexShader:Int
    var fragmentShader:Int
    var programObject:Int
    var linked=IntArray(1)

    var vertShaderSrc:String?=null
    var fragShaderSrc:String?=null

    vertShaderSrc= readShader(context,vertexShaderFileName)

    if(vertShaderSrc==null){
        return 0
    }

    fragShaderSrc= readShader(context,fragShaderFileName)
    if(fragShaderSrc==null){
        return 0
    }

    vertexShader= loadShader(GLES30.GL_VERTEX_SHADER,vertShaderSrc)

    if(vertexShader==0){
        return 0
    }

    fragmentShader= loadShader(GLES30.GL_FRAGMENT_SHADER,fragShaderSrc)
    if(fragmentShader==0){
        GLES30.glDeleteShader(vertexShader)
        return 0
    }
    programObject=GLES30.glCreateProgram()

    if(programObject==0){
        return 0
    }

    GLES30.glAttachShader(programObject,vertexShader)
    GLES30.glAttachShader(programObject,fragmentShader)

    GLES30.glLinkProgram(programObject)

    GLES30.glGetProgramiv(programObject,GLES30.GL_LINK_STATUS,linked,0)

    if(linked[0]==0){
        Log.e("ESSahder","Error linking program:")
        Log.e("ESSahder",GLES30.glGetProgramInfoLog(programObject))
        GLES30.glDeleteProgram(programObject)
        return 0
    }

    GLES30.glDeleteShader(vertexShader)
    GLES30.glDeleteShader(fragmentShader)
    return programObject
}

fun readShader(context: Context, fileName:String):String?{
    var shaderSource:String?=null
    if(fileName== null){
        return shaderSource
    }

    // Read the shader file from assets
    var inputStream: InputStream? = null
    val buffer: ByteArray

    try {
        inputStream = context.assets.open(fileName)

        // Create a buffer that has the same size as the InputStream
        buffer = ByteArray(inputStream!!.available())

        // Read the text file as a stream, into the buffer
        inputStream!!.read(buffer)

        val os = ByteArrayOutputStream()

        // Write this buffer to the output stream
        os.write(buffer)

        // Close input and output streams
        os.close()
        inputStream!!.close()

        shaderSource = os.toString()
    } catch (ioe: IOException) {
        inputStream = null
    }


    return if (inputStream == null) {
        shaderSource
    } else shaderSource
}

fun loadProgram(vertShaderSrc:String,fragShaderSrc:String): Int {
    var vertexShader=0
    var fragmentShader=0
    var programObject=0
    var linked=IntArray(1)

    vertexShader= loadShader(GLES30.GL_VERTEX_SHADER,vertShaderSrc)

    if(vertexShader==0){
        return 0
    }

    fragmentShader= loadShader(GLES30.GL_FRAGMENT_SHADER,fragShaderSrc)

    if(fragmentShader==0){
        GLES30.glDeleteShader(vertexShader)
        return 0
    }

    programObject=GLES30.glCreateProgram()

    if(programObject==0){
        return 0
    }

    GLES30.glAttachShader(programObject,vertexShader)
    GLES30.glAttachShader(programObject,fragmentShader)

    GLES30.glLinkProgram(programObject)
    GLES30.glGetProgramiv(programObject,GLES30.GL_LINK_STATUS,linked,0)

    if(linked[0]==0){
        Log.e("ESShader","error linking program")
        Log.e("ESShader",GLES30.glGetProgramInfoLog(programObject))
        GLES30.glDeleteProgram(programObject)
        return 0
    }

    GLES30.glDeleteShader(vertexShader)
    GLES30.glDeleteShader(fragmentShader)
    return programObject
}