package com.example.zhanghehe.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

class ItemActivity: AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnTri ->{
                val intent =Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            R.id.btnLsn1 ->{

                val intent =Intent(this,Lesson1Activity::class.java)
                startActivity(intent)
            }
        }
    }

    private val btnTri: Button by lazy {
        findViewById(R.id.btnTri) as Button
    }

    private val btnLsn1 :Button  by lazy{
        findViewById(R.id.btnLsn1) as Button
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTri.setOnClickListener(this)
        btnLsn1.setOnClickListener(this)


    }


}