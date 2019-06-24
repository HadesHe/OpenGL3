package com.example.zhanghehe.util

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class ESShapes{


    var mNumIndices: Int=0
    lateinit var mIndices: ShortBuffer
    lateinit var mTexCoords: FloatBuffer
    lateinit var mNormals: FloatBuffer
    lateinit var mVertices: FloatBuffer

    /**
     * 绘制球体
     */

    fun getSphere(numSlices:Int,radius:Float): Int {
        var numParallels=numSlices
        var numVertices=(numParallels+1)*(numSlices+1)

        var numIndices=numParallels*numSlices*6

        var angleStep=((2.0f*(Math.PI as  Float))/numSlices)


        mVertices=ByteBuffer.allocateDirect(numVertices*3*4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mNormals=ByteBuffer.allocateDirect(numVertices*3*4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTexCoords=ByteBuffer.allocateDirect(numVertices*2*4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mIndices=ByteBuffer.allocateDirect(numIndices*2)
            .order(ByteOrder.nativeOrder()).asShortBuffer()

        for (i in 0..numParallels){
            for (j in 0..numSlices){
                var vertex=(i*(numSlices+1)+j)*3

                mVertices
                    .put(
                        vertex + 0,
                        (radius.toDouble()
                                * Math.sin((angleStep * i).toDouble()) * Math
                            .sin((angleStep * j).toDouble())).toFloat()
                    )

                mVertices.put(
                    vertex + 1,
                    (radius * Math.cos((angleStep * i).toDouble())).toFloat()
                )
                mVertices
                    .put(
                        vertex + 2,
                        (radius.toDouble()
                                * Math.sin((angleStep * i).toDouble()) * Math
                            .cos((angleStep * j).toDouble())).toFloat()
                    )

                mNormals.put(vertex+0,mVertices.get(vertex+0)/radius)
                mNormals.put(vertex+1,mVertices.get(vertex+1)/radius)
                mNormals.put(vertex+2,mVertices.get(vertex+2)/radius)

                var texIndex=(i*(numSlices+1)+j)*2
                mTexCoords.put(texIndex+0,j.toFloat()/numSlices.toFloat())
                mTexCoords.put(texIndex+1,(1.0f-i.toFloat())/((numParallels-1).toFloat()))
            }
        }


        var index = 0

        var i = 0
        while (i < numParallels) {
            var j = 0
            while (j < numSlices) {
                mIndices.put(index++, (i * (numSlices + 1) + j) as Short)
                mIndices.put(index++, ((i + 1) * (numSlices + 1) + j) as Short)
                mIndices.put(
                    index++,
                    ((i + 1) * (numSlices + 1) + (j + 1)) as Short
                )

                mIndices.put(index++, (i * (numSlices + 1) + j) as Short)
                mIndices.put(
                    index++,
                    ((i + 1) * (numSlices + 1) + (j + 1)) as Short
                )
                mIndices.put(index++, (i * (numSlices + 1) + (j + 1)) as Short)
                j++

            }
            i++
        }

        mNumIndices = numIndices

        return numIndices
    }

    fun genCube(scale:Float):Int{
        var i: Int
        val numVertices = 24
        val numIndices = 36

        val cubeVerts = floatArrayOf(
            -0.5f,
            -0.5f,
            -0.5f,
            -0.5f,
            -0.5f,
            0.5f,
            0.5f,
            -0.5f,
            0.5f,
            0.5f,
            -0.5f,
            -0.5f,
            -0.5f,
            0.5f,
            -0.5f,
            -0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            -0.5f,
            -0.5f,
            -0.5f,
            -0.5f,
            -0.5f,
            0.5f,
            -0.5f,
            0.5f,
            0.5f,
            -0.5f,
            0.5f,
            -0.5f,
            -0.5f,
            -0.5f,
            -0.5f,
            0.5f,
            -0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            -0.5f,
            0.5f,
            -0.5f,
            -0.5f,
            -0.5f,
            -0.5f,
            -0.5f,
            0.5f,
            -0.5f,
            0.5f,
            0.5f,
            -0.5f,
            0.5f,
            -0.5f,
            0.5f,
            -0.5f,
            -0.5f,
            0.5f,
            -0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            -0.5f
        )

        val cubeNormals = floatArrayOf(
            0.0f,
            -1.0f,
            0.0f,
            0.0f,
            -1.0f,
            0.0f,
            0.0f,
            -1.0f,
            0.0f,
            0.0f,
            -1.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f,
            0.0f,
            -1.0f,
            0.0f,
            0.0f,
            -1.0f,
            0.0f,
            0.0f,
            -1.0f,
            0.0f,
            0.0f,
            -1.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f,
            1.0f,
            -1.0f,
            0.0f,
            0.0f,
            -1.0f,
            0.0f,
            0.0f,
            -1.0f,
            0.0f,
            0.0f,
            -1.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f
        )

        val cubeTex = floatArrayOf(
            0.0f,
            0.0f,
            0.0f,
            1.0f,
            1.0f,
            1.0f,
            1.0f,
            0.0f,
            1.0f,
            0.0f,
            1.0f,
            1.0f,
            0.0f,
            1.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            1.0f,
            1.0f,
            1.0f,
            1.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            1.0f,
            1.0f,
            1.0f,
            1.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            1.0f,
            1.0f,
            1.0f,
            1.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            1.0f,
            1.0f,
            1.0f,
            1.0f,
            0.0f
        )

        // Allocate memory for buffers
        mVertices = ByteBuffer.allocateDirect(numVertices * 3 * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mNormals = ByteBuffer.allocateDirect(numVertices * 3 * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTexCoords = ByteBuffer.allocateDirect(numVertices * 2 * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mIndices = ByteBuffer.allocateDirect(numIndices * 2)
            .order(ByteOrder.nativeOrder()).asShortBuffer()

        mVertices.put(cubeVerts).position(0)

        i = 0
        while (i < numVertices * 3) {
            mVertices.put(i, mVertices.get(i) * scale)
            i++
        }

        mNormals.put(cubeNormals).position(0)
        mTexCoords.put(cubeTex).position(0)

        val cubeIndices = shortArrayOf(
            0,
            2,
            1,
            0,
            3,
            2,
            4,
            5,
            6,
            4,
            6,
            7,
            8,
            9,
            10,
            8,
            10,
            11,
            12,
            15,
            14,
            12,
            14,
            13,
            16,
            17,
            18,
            16,
            18,
            19,
            20,
            23,
            22,
            20,
            22,
            21
        )

        mIndices.put(cubeIndices).position(0)
        mNumIndices = numIndices
        return numIndices

    }
}