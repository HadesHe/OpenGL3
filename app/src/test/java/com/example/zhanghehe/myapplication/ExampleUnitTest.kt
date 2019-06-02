package com.example.zhanghehe.myapplication

import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val nums= intArrayOf(1,2,3,45,5,78,100,90)
        arrayPairSum(nums)
    }

    @Test
    fun testGlEsVersion(){
        val result=196610-0x30000
        println("result $result")
    }


    fun reverseString(s: CharArray): Unit {
        if(s.isEmpty()){
            return
        }
        var start=0
        var end=s.size-1
        var extra='a'

        while(start<end){
            extra=s[start]
            s[start]=s[end]
            s[end]=extra
            start++
            end--
        }
    }

    fun arrayPairSum(nums: IntArray): Int {
        if(nums.isEmpty()){
            return 0
        }

        Arrays.sort(nums)

        nums.forEach {
            println("num $it")
        }

        var result=0

        for (i in 0 until nums.size step 2){
            result+=nums[i]
        }
        return result

    }
}
