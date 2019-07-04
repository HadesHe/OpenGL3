package com.example.zhanghehe.myapplication

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.example.zhanghehe.util.loadProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureWrapActivity:BaseOpenGL3Activity() {

    companion object{
        private val vShaderStr="""
            #version 300 es
            uniform float u_offset;
            layout(location=0) in vec4 a_position;
            layout(location=1) in vec2 a_texCoord;
            out vec2 v_texCoord;
            void main(){
                gl_Position=a_position;
                gl_Position.x+=u_offset;
                v_texCoord=a_texCoord;
            }
        """.trimIndent()

        private val fShaderStr="""
            #version 300 es
            precision mediump float;
            in vec2 v_texCoord;
            layout(location=0) out vec4 outColor;
            uniform sampler2D s_texture;
            void main(){
                outColor=texture(s_texture,v_texCoord);
            }
        """.trimIndent()

        private val mVerticesData= floatArrayOf(
            -0.3f,0.3f,0.0f,1.0f,-1.0f,-1.0f,
            -0.3f,-0.3f,0.0f,1.0f,-1.0f,2.0f,
            0.3f,-0.3f,0.0f,1.0f,2.0f,2.0f,
            0.3f,0.3f,0.0f,1.0f,2.0f,-1.0f
        )

        private val mIndicesData= shortArrayOf(
            0,1,2,0,2,3
        )
    }
    override fun getTag(): String {
        return TextureWrapActivity::class.java.simpleName
    }

    override fun getRender(): GLSurfaceView.Renderer {
        return TextureWrapRenderer()
    }

    inner class TextureWrapRenderer:GLSurfaceView.Renderer{

        private var mIndices: ShortBuffer
        private var mVertices: FloatBuffer
        private var mTextureId: Int=0
        private var mOffsetLoc: Int=0
        private var mSamplerLoc: Int=0
        private var mProgramObject: Int=0
        private var mHeight: Int=0
        private var mWidth: Int=0

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
            mVertices.position(0)

            GLES30.glVertexAttribPointer(0,4,GLES30.GL_FLOAT,
                false,6*4,mVertices)

            mVertices.position(4)

            GLES30.glVertexAttribPointer(1,2,GLES30.GL_FLOAT,
                false,6*4,mVertices)

            GLES30.glEnableVertexAttribArray(0)
            GLES30.glEnableVertexAttribArray(1)

            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,mTextureId)

            GLES30.glUniform1i(mSamplerLoc,0)

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT)
            GLES30.glUniform1f(mOffsetLoc,-0.7f)
            GLES30.glDrawElements(GLES30.GL_TRIANGLES,6,GLES30.GL_UNSIGNED_SHORT,mIndices)

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE)
            GLES30.glUniform1f(mOffsetLoc,0.0f)
            GLES30.glDrawElements(GLES30.GL_TRIANGLES,6,GLES30.GL_UNSIGNED_SHORT,mIndices)

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_MIRRORED_REPEAT)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_MIRRORED_REPEAT)
            GLES30.glUniform1f(mOffsetLoc,0.7f)
            GLES30.glDrawElements(GLES30.GL_TRIANGLES,6,GLES30.GL_UNSIGNED_SHORT,mIndices)

        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            mWidth=width
            mHeight=height
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            mProgramObject=loadProgram(vShaderStr,fShaderStr)
            mSamplerLoc=GLES30.glGetUniformLocation(mProgramObject,"s_texture")
            mOffsetLoc=GLES30.glGetUniformLocation(mProgramObject,"u_offset")
            mTextureId=createTexture2D()
            GLES30.glClearColor(1.0f,1.0f,1.0f,0.0f)

        }

        private fun createTexture2D():Int {
            var textureId=IntArray(1)
            var width=256
            var height=256

            var pixels=genCheckImage(width,height,64)

            /**
             * n:生成纹理的数量
             * textures: 保存n个纹理ID的无符号数组
             */
            GLES30.glGenTextures(1,textureId,0)

            /**
             * target:将纹理对象绑定到 GL_TEXTURE_2D, GL_TEXTURE_3D,GL_TEXTURE_2D_ARRAY,GL_TEXTURE_CUBE_MAP
             * textures：要绑定的纹理对象句柄
             */
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,textureId[0])

            /**
             * 用于加载2D和立方图纹理
             * target：GL_TEXTURE_2D、GL_TEXTURE_CUBE_MAP
             * level:要加载的mip级别，第一个级别为0，后续的mip贴图级别递增
             * internalFormat:纹理存储的内部格式  GL_RGBA,GL_RGB,GL_LUMINANCE_ALPHA,GL_LUMINANCE,GL_ALPHA
             * width:图像的宽度
             * height:图像的高度
             * border：保持与桌面的OpenGL接口兼容；默认为0
             * format：输入的纹理格式 GL_RED,GL_RED_INTEGER,GL_RG,GL_RG_INTEGER,GL_RGB,GL_RGB_INTEGER,GL_RGBA,GL_RGBA_INTEGER,GL_DEPTH,GL_DEPTH_COMPONENT,
             *                      GL_DEPTH_STENCIL,GL_LUMINACCE_ALPHA,GL_ALPHA
             * type：输入像素数据的类型   GL_UNSIGNED_BYTE,GL_BYTE,GL_UNSIGNED_SHORT,GL_SHORT,GL_UNSIGNED_INT,GL_INT,GL_HALF_FLOAT,GL_FLOAT,
             *                          GL_UNSIGNED_SHORT_5_6_5,GL_UNSIGNED_SHORT_4_4_4_4,GL_UNSIGNED_SHORT_5_5_5_1,
             *                          GL_UNSIGNED_INT_2_10_10_REV,GL_UNSIGNED_INT_10F_11F_11F_REV,GL_UNSIGNED_INT_5_9_9_9_REV,GL_UNSIGNED_INT_24_8,
             *                          GL_FLOAT_32_UNSIGNED_INT_24_8_REV,GL_UNSIGNED_SHORT_5_6_5
             *
             */
            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D,0,GLES30.GL_RGB,width,height,
                0,GLES30.GL_RGB,GLES30.GL_UNSIGNED_BYTE,pixels)

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR)

            return textureId[0]



        }

        private fun genCheckImage(width: Int,height:Int,checkSize:Int): ByteBuffer? {
            var x=0
            var y=0
            var pixels=ByteArray(width*height*3)

            for(y in 0 until height)
                for (x in 0 until width){
                    var rColor:Byte
                    var bColor:Byte

                    if ( ( x / checkSize ) % 2 == 0 )
                    {
                        rColor =  ( 127 * ( ( y / checkSize ) % 2 ) ).toByte()
                        bColor = ( 127 * ( 1 - ( ( y / checkSize ) % 2 ) ) ).toByte()
                    }
                    else
                    {
                        bColor =  ( 127 * ( ( y / checkSize ) % 2 ) ) .toByte()
                        rColor = ( 127 * ( 1 - ( ( y / checkSize ) % 2 ) ) ).toByte()
                    }

                    pixels[ ( y * width + x ) * 3] = rColor
                    pixels[ ( y * width + x ) * 3 + 1] = 0
                    pixels[ ( y * width + x ) * 3 + 2] = bColor
                }

            var result=ByteBuffer.allocateDirect(width*height*3)
            result.put(pixels).position(0)
            return result

        }





    }
}

