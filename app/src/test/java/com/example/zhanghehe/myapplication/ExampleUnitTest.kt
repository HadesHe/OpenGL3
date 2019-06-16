package com.example.zhanghehe.myapplication

import android.opengl.GLES30
import org.junit.Test

import org.junit.Assert.*
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var a=1


        /**
         * index
         */
        println(a)

    }



    data class User(var name:String)

    @Test
    fun testRevesed(){

        val s="aaefr b cc d"
        val resultSplit=s.split(" ")
//        println(resultSplit.size)
        println(reverseWords(s))
    }

    @Test
    fun testMoveZoroes(){
        val nums= intArrayOf(0,1,0,3,12)
        moveZeroes(nums)
    }

    fun moveZeroes(nums: IntArray): Unit {


        var currentPos=0
        nums.forEach {
            if(it!=0){
                nums[currentPos]=it
                currentPos++
            }
        }

        while (currentPos<nums.size){
            nums[currentPos]=0
            currentPos++
        }

        nums.forEach {
            println(it)
        }

    }

//    fun reverseWords(s: String): String {
//
//        var result=s.trim()
//        var resultList= listOf<String>()
//        while(result.contains("  ")){
//            result=result.replace("  "," ")
//        }
//        if(result.contains(" ")){
//           resultList= result.split(" ")
//        }else{
//            return result
//        }
//
//        var sb=StringBuffer()
//        resultList.reversed().forEach {
//            sb.append(it)
//            sb.append(" ")
//        }
//        return sb.toString()
//
//    }

    fun reverseWords(s: String): String {
        if(s.contains(" ")){
            var splitList=s.split(" ")

            var sb=StringBuilder()


            splitList.forEach {
                sb.append(it.reversed())
                sb.append(" ")
            }
            return sb.trim().toString()

        }else{
            return s.reversed()
        }

    }

    fun minSubArrayLen(s: Int, nums: IntArray): Int {
        if(nums.isEmpty()){
            return 0
        }


        var start=0
        var end=0
        var sum=0
        var minLength=Int.MAX_VALUE
        nums?.let {
            while(end<it.size){
                sum+=nums[end++]

                while (sum>=s){
                    minLength=Math.min(minLength,end-start)
                    sum-=nums[start++]
                }
            }
        }
        if(minLength== Int.MAX_VALUE){
            return 0
        }else{
            return minLength
        }

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
