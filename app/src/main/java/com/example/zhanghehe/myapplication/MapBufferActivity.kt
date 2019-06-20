package com.example.zhanghehe.myapplication

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.example.zhanghehe.util.loadProgram
import java.nio.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import java.nio.ByteOrder.nativeOrder
import android.R.attr.order



class MapBufferActivity:BaseOpenGL3Activity(){
    override fun getTag(): String {
        return MapBufferActivity::class.java.simpleName
    }

    override fun getRender(): GLSurfaceView.Renderer {
        return MapBufferRender()
    }

    inner class MapBufferRender : GLSurfaceView.Renderer {
        private lateinit var idxMappedBuf: ShortBuffer
        private lateinit var vtxMappedBuf: FloatBuffer
        private var mHeight: Int=0
        private var mWidth: Int = 0
        private var mProgramObject: Int=0

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glViewport(0,0,mWidth,mHeight)
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

            GLES30.glUseProgram(mProgramObject)

            drawPrimitiveWithVBOsMapBuffers()
        }

        private fun drawPrimitiveWithVBOsMapBuffers() {
            var offset=0
            var numVertices=3
            var numIndices=3
            var vtxStride=4*(VERTEX_POS_SIZE+ VERTEX_COLOR_SIZE)

            if(mVBOIds[0]==0&& mVBOIds[1]==0){
                GLES30.glGenBuffers(2, mVBOIds,0)

                GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOIds[0])
                GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,vtxStride*numVertices,
                    null,GLES30.GL_STATIC_DRAW)

                /**
                 * 返回指向所有或者一部分缓冲区对象数据存储的指针
                 * target:GL_ARRAY_BUFFER 顶点标志
                 *        GL_ELEMENT_ARRAY_BUFFER 图元标志
                 * offset: 缓冲区数据存储中的偏移量，字节算
                 * length:需要映射的缓冲区数据的字节数
                 * access:访问标志的喂域结合
                 *       GL_MAP_READ_BIT：应用程序将从返回的指针读取
                 *       GL_MAP_WRITE_BIT:应用程序将写入返回的指针
                 *       GL_MAP_INVALIDATE_RANGE_BIT:指定范围内的缓冲区可以在返回指针之前由驱动程序放弃。不能与 GL_MAP_READ_BIT 组合使用
                 *       GL_MAP_INVALIDATE_BUFFER_BIT: 整个缓冲区的内容可以在返回指针之前由去东城放弃。不能与 GL_MAP_READ_BIT 组合使用
                 *       GL_MAP_FLUSH_EXPLICIT_BIT: 应用程序将明确地用 glFlashMappedBufferRange 刷新对映射范围子范围的操作。不能与 GL_MAP_WRITE_BIT 组合使用
                 *       GL-MAP_UNSUNCHRONIZED_BIT: 驱动程序在返回缓冲区范围的指针之前不需要等待缓冲对象上的未决操作。如果有未决操作，则未决操作的结果和缓冲区对象上的任何未来操作都变成为定义
                 */
                vtxMappedBuf=(GLES30.glMapBufferRange(
                    GLES30.GL_ARRAY_BUFFER,0,vtxStride*numVertices,
                    GLES30.GL_MAP_WRITE_BIT or GLES30.GL_MAP_INVALIDATE_BUFFER_BIT
                ) as ByteBuffer).order(ByteOrder.nativeOrder()).asFloatBuffer()
                vtxMappedBuf.put(mVerticesData).position(0)


                /**
                 * 取消之前的缓冲区映射
                 * target: 必须设置为 GL_ARRAY_BUFFER
                 */
                GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER)

                GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[1])
                GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER,2*numIndices,
                    null,GLES30.GL_STATIC_DRAW)

                idxMappedBuf = (GLES30.glMapBufferRange(
                    GLES30.GL_ELEMENT_ARRAY_BUFFER, 0, 2 * numIndices,
                    GLES30.GL_MAP_WRITE_BIT or GLES30.GL_MAP_INVALIDATE_BUFFER_BIT
                ) as ByteBuffer).order(ByteOrder.nativeOrder()).asShortBuffer()


                idxMappedBuf.put(mIndicesData).position(0)

                GLES30.glUnmapBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER)
            }
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOIds[0])

            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[1])
            GLES30.glEnableVertexAttribArray(VERTEX_POS_INDX)
            GLES30.glEnableVertexAttribArray(VERTEX_COLOR_INDX)

            GLES30.glVertexAttribPointer(VERTEX_POS_INDX, VERTEX_POS_SIZE,
                GLES30.GL_FLOAT,false,vtxStride,offset)
            offset+= (VERTEX_POS_SIZE*4)
            GLES30.glVertexAttribPointer(VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE,
                GLES30.GL_FLOAT,false,vtxStride,offset)

            GLES30.glDrawElements(GLES30.GL_TRIANGLES,numIndices,
                GLES30.GL_UNSIGNED_SHORT,0)

            GLES30.glDisableVertexAttribArray(VERTEX_POS_INDX)
            GLES30.glDisableVertexAttribArray(VERTEX_COLOR_INDX)
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0)
            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0)
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

        private var mVBOIds=IntArray(2)
        private val mVerticesData= floatArrayOf(
            0.0f,0.5f,0.0f,
            1.0f,0.0f,0.0f,1.0f,
            -0.5f,-0.5f,0.0f,
            0.0f,1.0f,0.0f,1.0f,
            0.5f,-0.5f,0.0f,
            0.0f,0.0f,1.0f,1.0f
        )

        private val mIndicesData= shortArrayOf(
            0,1,2
        )

        private val VERTEX_POS_SIZE=3
        private val VERTEX_COLOR_SIZE=4

        private val VERTEX_POS_INDX=0
        private val VERTEX_COLOR_INDX=1

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
    }


}