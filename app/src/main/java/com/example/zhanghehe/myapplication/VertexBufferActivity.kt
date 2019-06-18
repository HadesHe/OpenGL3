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

class VertexBufferActivity :BaseOpenGL3Activity(){

    override fun getTag(): String {
        return VertexBufferActivity::class.java.simpleName
    }

    override fun getRender(): GLSurfaceView.Renderer {
        return VertexBufferRenderer(VertexBufferActivity@this)
    }

    inner class VertexBufferRenderer(context: Context):GLSurfaceView.Renderer{
        private var mIndices: ShortBuffer
        private var mVertices1: FloatBuffer
        private var mVertices: FloatBuffer
        private var mProgramObject: Int=0
        private var mHeight: Int=0
        private var mWidth: Int=0

        init {
            mVertices=ByteBuffer.allocateDirect(mVerticesData.size*4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
            mVertices.put(mVerticesData).position(0)

            for(i in 0 until 3){
                var current=i*(VERTEX_POS_SIZE+ VERTEX_COLOR_SIZE)+0
                mVerticesData[current]+=1.0f
            }

            mVertices1=ByteBuffer.allocateDirect(mVerticesData.size*4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
            mVertices1.put(mVerticesData).position(0)


            mIndices=ByteBuffer.allocateDirect(mIndicesData.size*2)
                .order(ByteOrder.nativeOrder()).asShortBuffer()
            mIndices.put(mIndicesData).position(0)
        }

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glViewport(0,0,mWidth,mHeight)
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
            GLES30.glUseProgram(mProgramObject)
            drawPrimitiveWithoutVBOs()
            drawPrimitiveWithVBOs()
        }

        private fun drawPrimitiveWithVBOs() {
            var offset=0
            var numVertices=3
            var numIndices=3
            var vtxStride=4*(VERTEX_POS_SIZE+ VERTEX_COLOR_SIZE)

            if(mVBOIds[0]==0 && mVBOIds[1]==0){
                GLES30.glGenBuffers(2, mVBOIds,0)
                mVertices1.position(0)

                GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOIds[0])
                GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,vtxStride*numVertices,
                    mVertices1,GLES30.GL_STATIC_DRAW)

                mIndices.position(0)
                GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[1])
                GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER,2*numIndices,
                    mIndices,GLES30.GL_STATIC_DRAW)
            }

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOIds[0])
            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[1])

            GLES30.glEnableVertexAttribArray(VERTEX_POS_INDX)
            GLES30.glEnableVertexAttribArray(VERTEX_COLOR_INDX)

            GLES30.glVertexAttribPointer(VERTEX_POS_INDX, VERTEX_POS_SIZE,
                GLES30.GL_FLOAT,false,vtxStride,offset)
            offset+= VERTEX_POS_SIZE*4
            GLES30.glVertexAttribPointer(VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE,
                GLES30.GL_FLOAT,false,vtxStride,offset)

            GLES30.glDrawElements(GLES30.GL_TRIANGLES,numIndices,
                GLES30.GL_UNSIGNED_SHORT,0)

            GLES30.glDisableVertexAttribArray(VERTEX_POS_INDX)
            GLES30.glDisableVertexAttribArray(VERTEX_COLOR_INDX)

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0)
            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0)
        }

        private fun drawPrimitiveWithoutVBOs() {
            val numIndices=3
            val vtxStride=4*(VERTEX_POS_SIZE+ VERTEX_COLOR_SIZE)
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0)
            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0)

            GLES30.glEnableVertexAttribArray(VERTEX_POS_INDX)
            GLES30.glEnableVertexAttribArray(VERTEX_COLOR_INDX)

            mVertices.position(0)

            GLES30.glVertexAttribPointer(VERTEX_POS_INDX, VERTEX_POS_SIZE,
                GLES30.GL_FLOAT,false,vtxStride,mVertices)

            mVertices.position(VERTEX_POS_SIZE)

            GLES30.glVertexAttribPointer(VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE,
                GLES30.GL_FLOAT,false,vtxStride,mVertices)

            GLES30.glDrawElements(GLES30.GL_TRIANGLES,numIndices,
                GLES30.GL_UNSIGNED_SHORT,mIndices)

            GLES30.glDisableVertexAttribArray(VERTEX_POS_INDX)
            GLES30.glDisableVertexAttribArray(VERTEX_COLOR_INDX)



        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            mWidth=width
            mHeight=height
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            mProgramObject= loadProgram(vShaderStr, fShaderStr)
            mVBOIds[0]=0
            mVBOIds[1]=0
            GLES30.glClearColor(1.0f,1.0f,1.0f,0.0f)

        }



    }

    companion object{
//        (x,y,z,r,g,b,a)
        private val mVerticesData= floatArrayOf(
            -0.5f,0.5f,0.0f,1.0f,0.0f,0.0f,1.0f,
            -1.0f,-0.5f,0.0f,0.0f,1.0f,0.0f,1.0f,
             0.0f,-0.5f,0.0f,0.0f,0.0f,1.0f,1.0f
        )

        private var mVBOIds=IntArray(2)

        private val mIndicesData= shortArrayOf(0,1,2)

        val VERTEX_POS_SIZE=3
        val VERTEX_COLOR_SIZE=4

        val VERTEX_POS_INDX=0
        val VERTEX_COLOR_INDX=1

        val vertex_stride=(4*(VERTEX_COLOR_SIZE+ VERTEX_POS_SIZE))

        val vShaderStr="""
            #version 300 es
            layout(location=0) in vec4 a_position;
            layout(location=1) in vec4 a_color;
            out vec4 v_color;
            void main(){
                v_color=a_color;
                gl_Position=a_position;
            }
        """.trimIndent()

        val fShaderStr="""
            #version 300 es
            precision mediump float;
            in vec4 v_color;
            out vec4 o_fragColor;
            void main(){
                o_fragColor=v_color;
            }
        """.trimIndent()
    }

}