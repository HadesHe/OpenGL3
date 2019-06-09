package com.example.zhanghehe.myapplication

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30
import android.opengl.Matrix
import android.os.SystemClock
import com.example.zhanghehe.util.loadShader
import java.lang.RuntimeException
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
        private val mModelMatrix=FloatArray(16)
        private var mColorHandle: Int=0
        private var mPositionHandle: Int=0
        private var mMVPMatrixHandle: Int = 0
        private var mProjectMatrix=FloatArray(16)
        private var mViewMatrix=FloatArray(16)
        private var mMVPMatrix=FloatArray(16)
        private var mTriangle1Vertices: FloatBuffer
        private var mTriangle2Vertices: FloatBuffer
        private var mTriangle3Vertices: FloatBuffer
        private val mBytesPerFloat=4
        private val mStrideBytes=7*mBytesPerFloat

        private val mPositionOffset=0
        private val mPositionDataSize=3
        private val mColorOffset=3
        private val mColorDataSize=4

        init {
            // Define points for equilateral triangles.

            // This triangle is red, green, and blue.
            val triangle1VerticesData = floatArrayOf(
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f
            )

            // This triangle is yellow, cyan, and magenta.
            val triangle2VerticesData = floatArrayOf(
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f
            )

            // This triangle is white, gray, and black.
            val triangle3VerticesData = floatArrayOf(
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f,

                0.5f, -0.25f, 0.0f, 0.5f, 0.5f, 0.5f, 1.0f,

                0.0f, 0.559016994f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f
            )

            // Initialize the buffers.
            mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.size * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
            mTriangle2Vertices = ByteBuffer.allocateDirect(triangle2VerticesData.size * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
            mTriangle3Vertices = ByteBuffer.allocateDirect(triangle3VerticesData.size * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()

            mTriangle1Vertices.put(triangle1VerticesData).position(0)
            mTriangle2Vertices.put(triangle2VerticesData).position(0)
            mTriangle3Vertices.put(triangle3VerticesData).position(0)
        }

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)

            var time=SystemClock.uptimeMillis()%10000L
            var angleInDegrees=(360.0f/10000.0f)*(time.toInt())

            Matrix.setIdentityM(mModelMatrix,0)
            Matrix.rotateM(mModelMatrix,0,angleInDegrees,0.0f,0.0f,1.0f)
            drawTriangle(mTriangle1Vertices)

            Matrix.setIdentityM(mModelMatrix,0)
            Matrix.translateM(mModelMatrix,0,0.0f,-1.0f,0.0f)
            Matrix.rotateM(mModelMatrix,0,90.0f,1.0f,0.0f,0.0f)
            Matrix.rotateM(mModelMatrix,0,angleInDegrees,0.0f,0.0f,1.0f)
            drawTriangle(mTriangle2Vertices)


            Matrix.setIdentityM(mModelMatrix,0)
            Matrix.translateM(mModelMatrix,0,1.0f,0.0f,0.0f)
            Matrix.rotateM(mModelMatrix,0,90.0f,0.0f,1.0f,0.0f)
            Matrix.rotateM(mModelMatrix,0,angleInDegrees,0.0f,0.0f,1.0f)
            drawTriangle(mTriangle3Vertices)

        }

        private fun drawTriangle(vertices: FloatBuffer) {
            vertices.position(mPositionOffset)
            GLES30.glVertexAttribPointer(mPositionHandle,mPositionDataSize,GLES30.GL_FLOAT,
                false,mStrideBytes,vertices)
            GLES30.glEnableVertexAttribArray(mPositionHandle)

            vertices.position(mColorOffset)
            GLES30.glVertexAttribPointer(mColorHandle,mColorDataSize,GLES30.GL_FLOAT,false,
                mStrideBytes,vertices)
            GLES30.glEnableVertexAttribArray(mColorHandle)

            Matrix.multiplyMM(mMVPMatrix,0,mViewMatrix,0,mModelMatrix,0)
            Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mMVPMatrix,0)

            GLES30.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mMVPMatrix,0)
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,3)


        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

            GLES30.glViewport(0,0,width, height)
            val ratio=width/height.toFloat()
            val left=-ratio
            val right=ratio
            val bottom=-1.0f
            val top=1.0f
            val near=1.0f
            val far=10.0f
            Matrix.frustumM(mProjectMatrix,0,left,right,bottom,top,near,far)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            var vertextShader=loadShader(GLES30.GL_VERTEX_SHADER, vertexShader)
            var fragmentShader=loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShader)

            var programObject=GLES30.glCreateProgram()
            if(programObject==0){
                return
            }

            GLES30.glAttachShader(programObject,vertextShader)
            GLES30.glAttachShader(programObject,fragmentShader)
            GLES30.glBindAttribLocation(programObject,0,"a_Position")
            GLES30.glBindAttribLocation(programObject,1,"a_Color")

            GLES30.glLinkProgram(programObject)
            var linkStatus=IntArray(1)
            GLES30.glGetProgramiv(programObject,GLES30.GL_LINK_STATUS,linkStatus,0)

            if(linkStatus[0]==0){
                GLES30.glDeleteProgram(programObject)
                programObject=0
            }

            if(programObject==0){
                throw RuntimeException("Error creating program.")
            }

            mMVPMatrixHandle=GLES30.glGetUniformLocation(programObject,"u_MVPMatrix")
            mPositionHandle=GLES30.glGetAttribLocation(programObject,"a_Position")
            mColorHandle=GLES30.glGetAttribLocation(programObject,"a_Color")
            GLES30.glUseProgram(programObject)
        }

    }

    companion object{
        val vertexShader="""
            #version 300 es
            uniform mat4 u_MVPMatrix;
            in vec4 a_Position;
            in vec4 a_Color;
            out vec4 v_Color;

            void main(){
                v_Color=a_Color;
                gl_Position=u_MVPMatrix*a_Position;
            }

        """.trimIndent()
        val fragmentShader="""
            #version 300 es
            precision mediump float;
            in vec4 v_Color;
            out vec4 gl_FragColor;
            void main(){
                gl_FragColor=v_Color;
            }
        """.trimIndent()
    }


}
