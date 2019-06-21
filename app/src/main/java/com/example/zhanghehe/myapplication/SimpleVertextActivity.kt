package com.example.zhanghehe.myapplication

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SimpleVertextActivity:BaseOpenGL3Activity() {
    override fun getTag(): String {
        return SimpleVertextActivity::class.java.simpleName
    }

    override fun getRender(): GLSurfaceView.Renderer {
        return SimpleVertexRender()
    }

    inner class SimpleVertexRender : GLSurfaceView.Renderer {
        override fun onDrawFrame(gl: GL10?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    companion object{

        val vShaderStr="""
            #version 300 es
            uniform mat4 u_mvpMatrix;
            layout(location=0) in vec4 a_position;
            layout(location=1) in vec4 a_color;
            out vec4 v_color;
            void main(){
                v_color_a_color;
                gl_Position=u_mvpMatrix * a_position;
            }
        """.trimIndent()

        val fShaderStr="""
            #version 300 es
            precision mediump float;
            in vec4 v_color;
            layout(location=0) out vec4 outColor;
            void main(){
                outColor=v_color;
            }
        """.trimIndent()
    }


}
