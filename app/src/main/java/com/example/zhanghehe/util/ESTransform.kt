package com.example.zhanghehe.util

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import android.R.attr.y
import android.R.attr.x
import android.R.attr.angle



class ESTransform{

    private var mMatrixFloatBuffer: FloatBuffer

    var mMatrix:FloatArray= FloatArray(16)

    init {
        mMatrixFloatBuffer=ByteBuffer.allocateDirect(16*4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
    }

    fun getAsFloatBuffer(): FloatBuffer {
        mMatrixFloatBuffer.put(mMatrix).position(0)
        return mMatrixFloatBuffer

    }

    fun matrixLoadIdentity() {
        for (i in 0..15) {
            mMatrix[i] = 0.0f
        }

        mMatrix[0 * 4 + 0] = 1.0f
        mMatrix[1 * 4 + 1] = 1.0f
        mMatrix[2 * 4 + 2] = 1.0f
        mMatrix[3 * 4 + 3] = 1.0f
    }

    fun perspective(fovy: Float, aspect: Float, nearZ: Float, farZ: Float) {
        val frustumW: Float
        val frustumH: Float

        frustumH = Math.tan(fovy / 360.0 * Math.PI).toFloat() * nearZ
        frustumW = frustumH * aspect

        frustum(-frustumW, frustumW, -frustumH, frustumH, nearZ, farZ)

    }

    private fun frustum(left: Float, right: Float, bottom: Float,top: Float, nearZ: Float, farZ: Float) {

        val deltaX = right - left
        val deltaY = top - bottom
        val deltaZ = farZ - nearZ
        val frust = FloatArray(16)

        if (nearZ <= 0.0f || farZ <= 0.0f || deltaX <= 0.0f
            || deltaY <= 0.0f || deltaZ <= 0.0f
        ) {
            return
        }

        frust[0 * 4 + 0] = 2.0f * nearZ / deltaX
        frust[0 * 4 + 3] = 0.0f
        frust[0 * 4 + 2] = frust[0 * 4 + 3]
        frust[0 * 4 + 1] = frust[0 * 4 + 2]

        frust[1 * 4 + 1] = 2.0f * nearZ / deltaY
        frust[1 * 4 + 3] = 0.0f
        frust[1 * 4 + 2] = frust[1 * 4 + 3]
        frust[1 * 4 + 0] = frust[1 * 4 + 2]

        frust[2 * 4 + 0] = (right + left) / deltaX
        frust[2 * 4 + 1] = (top + bottom) / deltaY
        frust[2 * 4 + 2] = -(nearZ + farZ) / deltaZ
        frust[2 * 4 + 3] = -1.0f

        frust[3 * 4 + 2] = -2.0f * nearZ * farZ / deltaZ
        frust[3 * 4 + 3] = 0.0f
        frust[3 * 4 + 1] = frust[3 * 4 + 3]
        frust[3 * 4 + 0] = frust[3 * 4 + 1]

        matrixMultiply(frust, mMatrix)
    }

    fun translate(tx: Float, ty: Float, tz: Float) {
        mMatrix[3 * 4 + 0] += ( mMatrix[0 * 4 + 0] * tx + mMatrix[1 * 4 + 0]
                * ty + mMatrix[2 * 4 + 0] * tz );
        mMatrix[3 * 4 + 1] += ( mMatrix[0 * 4 + 1] * tx + mMatrix[1 * 4 + 1]
                * ty + mMatrix[2 * 4 + 1] * tz );
        mMatrix[3 * 4 + 2] += ( mMatrix[0 * 4 + 2] * tx + mMatrix[1 * 4 + 2]
                * ty + mMatrix[2 * 4 + 2] * tz );
        mMatrix[3 * 4 + 3] += ( mMatrix[0 * 4 + 3] * tx + mMatrix[1 * 4 + 3]
                * ty + mMatrix[2 * 4 + 3] * tz );
    }

    fun rotate(angel: Float, x: Float, y: Float, z: Float) {
        val sinAngle: Float
        val cosAngle: Float
        val mag = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

        sinAngle = Math.sin(angle * Math.PI / 180.0).toFloat()
        cosAngle = Math.cos(angle * Math.PI / 180.0).toFloat()

        if (mag > 0.0f) {
            val xx: Float
            val yy: Float
            val zz: Float
            val xy: Float
            val yz: Float
            val zx: Float
            val xs: Float
            val ys: Float
            val zs: Float
            val oneMinusCos: Float
            val rotMat = FloatArray(16)

            var copyx=x / mag.toInt()
            var copyy=y / mag.toInt()
            var copyz=z / mag

            xx = (copyx * copyx).toFloat()
            yy = (copyy * copyy).toFloat()
            zz = copyz * copyz
            xy = (copyx * copyy).toFloat()
            yz = copyy * copyz
            zx = copyz * copyx
            xs = copyx * sinAngle
            ys = copyy * sinAngle
            zs = copyz * sinAngle
            oneMinusCos = 1.0f - cosAngle

            rotMat[0 * 4 + 0] = oneMinusCos * xx + cosAngle
            rotMat[0 * 4 + 1] = oneMinusCos * xy - zs
            rotMat[0 * 4 + 2] = oneMinusCos * zx + ys
            rotMat[0 * 4 + 3] = 0.0f

            rotMat[1 * 4 + 0] = oneMinusCos * xy + zs
            rotMat[1 * 4 + 1] = oneMinusCos * yy + cosAngle
            rotMat[1 * 4 + 2] = oneMinusCos * yz - xs
            rotMat[1 * 4 + 3] = 0.0f

            rotMat[2 * 4 + 0] = oneMinusCos * zx - ys
            rotMat[2 * 4 + 1] = oneMinusCos * yz + xs
            rotMat[2 * 4 + 2] = oneMinusCos * zz + cosAngle
            rotMat[2 * 4 + 3] = 0.0f

            rotMat[3 * 4 + 0] = 0.0f
            rotMat[3 * 4 + 1] = 0.0f
            rotMat[3 * 4 + 2] = 0.0f
            rotMat[3 * 4 + 3] = 1.0f

            matrixMultiply(rotMat, mMatrix)
        }
    }

    fun matrixMultiply(srcA: FloatArray,srcB: FloatArray) {
        val tmp = FloatArray(16)
        var i: Int

        i = 0
        while (i < 4) {
            tmp[i * 4 + 0] = (srcA[i * 4 + 0] * srcB[0 * 4 + 0]
                    + srcA[i * 4 + 1] * srcB[1 * 4 + 0]
                    + srcA[i * 4 + 2] * srcB[2 * 4 + 0]
                    + srcA[i * 4 + 3] * srcB[3 * 4 + 0])

            tmp[i * 4 + 1] = (srcA[i * 4 + 0] * srcB[0 * 4 + 1]
                    + srcA[i * 4 + 1] * srcB[1 * 4 + 1]
                    + srcA[i * 4 + 2] * srcB[2 * 4 + 1]
                    + srcA[i * 4 + 3] * srcB[3 * 4 + 1])

            tmp[i * 4 + 2] = (srcA[i * 4 + 0] * srcB[0 * 4 + 2]
                    + srcA[i * 4 + 1] * srcB[1 * 4 + 2]
                    + srcA[i * 4 + 2] * srcB[2 * 4 + 2]
                    + srcA[i * 4 + 3] * srcB[3 * 4 + 2])

            tmp[i * 4 + 3] = (srcA[i * 4 + 0] * srcB[0 * 4 + 3]
                    + srcA[i * 4 + 1] * srcB[1 * 4 + 3]
                    + srcA[i * 4 + 2] * srcB[2 * 4 + 3]
                    + srcA[i * 4 + 3] * srcB[3 * 4 + 3])
            i++
        }

        mMatrix = tmp
    }
}