package com.example.zhanghehe.myapplication

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.example.zhanghehe.util.loadProgramFromAsset
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MipMap2DActivity :BaseOpenGL3Activity(){
    override fun getTag(): String {
        return MipMap2DActivity::class.java.simpleName
    }

    override fun getRender(): GLSurfaceView.Renderer {
        return MipMap2DRender(this@MipMap2DActivity)
    }

    inner class MipMap2DRender(val context: Context) : GLSurfaceView.Renderer {
        private var mIndices: ShortBuffer
        private var mVertices: FloatBuffer
        private var mTextureId: Int=0
        private var mOffsetLoc: Int=0
        private var mSamplerLoc: Int=0
        private var mProgramsObject: Int=0
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

            GLES30.glUseProgram(mProgramsObject)
            mVertices.position(0)
            GLES30.glVertexAttribPointer(0,4,
                GLES30.GL_FLOAT,false,
                6*4,mVertices)

            mVertices.position(4)
            GLES30.glVertexAttribPointer(
                1,2,GLES30.GL_FLOAT,
                false,6*4,mVertices)

            GLES30.glEnableVertexAttribArray(0)
            GLES30.glEnableVertexAttribArray(1)

            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,mTextureId)

            GLES30.glUniform1i(mSamplerLoc,0)

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST)
            GLES30.glUniform1f(mOffsetLoc,-0.6f)
            GLES30.glDrawElements(GLES30.GL_TRIANGLES,6,GLES30.GL_UNSIGNED_SHORT,mIndices)

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MIN_FILTER,
                GLES30.GL_LINEAR_MIPMAP_LINEAR)
            GLES30.glUniform1f(mOffsetLoc,0.6f)
            GLES30.glDrawElements(GLES30.GL_TRIANGLES,6,GLES30.GL_UNSIGNED_SHORT,mIndices)

        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            mWidth=width
            mHeight=height
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            mProgramsObject= loadProgramFromAsset(context,"vertexShader.glsl","fragmentShader.glsl")
            mSamplerLoc=GLES30.glGetUniformLocation(mProgramsObject,"s_texture")
            mOffsetLoc=GLES30.glGetUniformLocation(mProgramsObject,"u_offset")
            mTextureId=createMipMappedTexture2D()

            GLES30.glClearColor(1.0f,1.0f,1.0f,0.0f)
        }

        private fun createMipMappedTexture2D(): Int {
            var textureId=IntArray(1)
            var width=256
            var height=256
            var level:Int
            var pixels=genCheckImage(width,height,8)

            GLES30.glGenTextures(1,textureId,0)

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,textureId[0])

            var pixelBuffer=ByteBuffer.allocateDirect(width*height*3)
            pixelBuffer.put(pixels).position(0)


            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D,0,GLES30.GL_RGB,
                width,height,0,GLES30.GL_RGB,GLES30.GL_UNSIGNED_BYTE,pixelBuffer)

            level=1
            var prevImage = pixels

            while (width>1 && height>1){
                var newWidth:Int
                var newHeight:Int

                newWidth=width/2

                if (newWidth<=0){
                    newWidth=1
                }

                newHeight=height/2
                if(newHeight<=0){
                    newHeight=1
                }

                var newImage = getMipMap2D(prevImage, width, height, newWidth, newHeight)

                pixelBuffer= ByteBuffer.allocateDirect(newWidth*newHeight*3)
                pixelBuffer.put(newImage).position(0)

                GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D,level,GLES30.GL_RGB,
                    newWidth,newHeight,0,GLES30.GL_RGB,
                    GLES30.GL_UNSIGNED_BYTE,pixelBuffer)

                prevImage=newImage
                level++
                width=newWidth
                height=newHeight
            }

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MIN_FILTER,
                GLES30.GL_LINEAR_MIPMAP_NEAREST)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,
                GLES30.GL_LINEAR)
            return textureId[0]
        }

        private fun getMipMap2D(
            src: ByteArray,
            srcWidth: Int,
            srcHeight: Int,
            dstWidth: Int,
            dstHeight: Int
        ): ByteArray {
            var texelSize=3
            var dst=ByteArray(texelSize*(dstWidth)*(dstHeight))

            for(y in 0 until dstHeight){
                for(x in 0 until dstWidth){
                    var srcIndex=IntArray(4)
                    var r=0.0f
                    var g=0.0f
                    var b=0.0f

                    srcIndex[0]=(((y*2)*srcWidth)+(x*2))*texelSize
                    srcIndex[1]=(((y*2)*srcWidth)+(x*2+1))*texelSize
                    srcIndex[2]=((((y*2)+1)*srcWidth)+(x*2))*texelSize
                    srcIndex[3]=((((y*2)+1)*srcWidth)+(x*2+1))*texelSize

                    for(sample in 0 until 4){
                        r+=src[srcIndex[sample]]
                        g+=src[srcIndex[sample]+1]
                        b+=src[srcIndex[sample]+2]
                    }

                    r/=4.0f
                    g/=4.0f
                    b/=4.0f

                    dst[(y*(dstWidth)+x)*texelSize]=r.toByte()
                    dst[(y*(dstWidth)+x)*texelSize+1]=g.toByte()
                    dst[(y*(dstWidth)+x)*texelSize+2]=b.toByte()

                }
            }

            return dst

        }


        private fun genCheckImage(width: Int, height: Int, checkSize: Int): ByteArray {
            var pixels=ByteArray(width*height*3)
            for( y in 0 until height){
                for(x in 0 until width){
                    var rColor:Byte
                    var bColor:Byte

                    if((x/checkSize)%2==0){
                        rColor= ((127*((y/checkSize)%2)).toByte())
                        bColor=(127*(1-((y/checkSize)%2))).toByte()
                    }else{
                        bColor=(127*((y/checkSize)%2)).toByte()
                        rColor=(127*(1-((y/checkSize)%2))).toByte()
                    }

                    pixels[(y*width+x)*3]=rColor
                    pixels[(y*width+x)*3+1]=0
                    pixels[(y*width+x)*3+2]=bColor
                }
            }

            return pixels
        }

    }

    companion object{
        private val mVerticesData= floatArrayOf(
            -0.5f,0.5f,0.0f,1.5f,
            0.0f,0.0f,
            -0.5f,-0.5f,0.0f,0.75f,
            0.0f,1.0f,
            0.5f,-0.5f,0.0f,0.75f,
            1.0f,1.0f,
            0.5f,0.5f,0.0f,1.5f,
            1.0f,0.0f
        )

        private val mIndicesData= shortArrayOf(
            0,1,2,0,2,3
        )
    }

}