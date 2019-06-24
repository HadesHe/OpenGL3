package com.example.zhanghehe.myapplication

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.SystemClock
import com.example.zhanghehe.util.ESShapes
import com.example.zhanghehe.util.ESTransform
import com.example.zhanghehe.util.loadProgram
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
        private var mLastTime: Long=0L
        private var mMVPMatrix: ESTransform= ESTransform()
        private var mAngel=0.0f
        private var mHeight=0
        private var mWidth=0
        private var mMVPLoc: Int=0
        private var mProjectObject: Int=0
        private var mCube=ESShapes()

        override fun onDrawFrame(gl: GL10?) {
            update()
            GLES30.glViewport(0,0,mWidth,mHeight)
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

            GLES30.glUseProgram(mProjectObject)
            GLES30.glVertexAttribPointer(0,3,GLES30.GL_FLOAT,false,
                0,mCube.mVertices)
            GLES30.glEnableVertexAttribArray(0)
            GLES30.glVertexAttrib4f(1,1.0f,0.0f,0.0f,1.0f)

            GLES30.glUniformMatrix4fv(mMVPLoc,1,false,
                mMVPMatrix.getAsFloatBuffer())

            GLES30.glDrawElements(GLES30.GL_TRIANGLES,mCube.mNumIndices,GLES30.GL_UNSIGNED_SHORT,mCube.mIndices)
        }

        private fun update() {
            if(mLastTime==0L){
                mLastTime=SystemClock.uptimeMillis()
            }
            var curTime=SystemClock.uptimeMillis()
            var elapsedTime=curTime-mLastTime
            var deltaTime=elapsedTime/1000.0f
            mLastTime=curTime

            var perspective=ESTransform()
            var modelView=ESTransform()

            var aspect=0.0f
            mAngel+=(deltaTime*40.0f)

            if(mAngel>=360.0f){
                mAngel-=360.0f
            }

            aspect=mWidth.toFloat()/mHeight.toFloat()

            perspective.matrixLoadIdentity()
            perspective.perspective(60.0f,aspect,1.0f,20.0f)

            modelView.matrixLoadIdentity()
            modelView.translate(0.0f,0.0f,-2.0f)
            modelView.rotate(mAngel,1.0f,0.0f,1.0f)
            mMVPMatrix.matrixMultiply(modelView.mMatrix,perspective.mMatrix)

        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            mWidth=width
            mHeight=height
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            mProjectObject= loadProgram(vShaderStr, fShaderStr)
            mMVPLoc=GLES30.glGetUniformLocation(mProjectObject,"u_mvpMatrix")

            mCube.genCube(1.0f)

            mAngel=45.0f

            GLES30.glClearColor(1.0f,1.0f,1.0f,0.0f)
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
                v_color=a_color;
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
