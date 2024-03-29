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
            R.id.btnVerBuf->{
                val intent=Intent(this,VertexBufferActivity::class.java)
                startActivity(intent)
            }
            R.id.btnVerArray->{
                val intent=Intent(this,VertexArrayActivity::class.java)
                startActivity(intent)
            }
            R.id.btnMaoBuffer ->{
                val intent=Intent(this,MapBufferActivity::class.java)
                startActivity(intent)
            }
            R.id.btnSimpleVertex ->{
                val intent=Intent(this,SimpleVertextActivity::class.java)
                startActivity(intent)
            }
            R.id.btnTexWrap ->{
                val intent=Intent(this,TextureWrapActivity::class.java)
                startActivity(intent)
            }
            R.id.btnMipMap2D ->{
                val intent=Intent(this,MipMap2DActivity::class.java)
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

    private val btnVerBuf :Button  by lazy{
        findViewById(R.id.btnVerBuf) as Button
    }
    private val btnVerArray:Button by lazy{
        findViewById(R.id.btnVerArray) as Button
    }
    private val btnMapBuffer:Button by lazy {
        findViewById(R.id.btnMaoBuffer) as Button
    }

    private val btnSimpleVertext by lazy {
        findViewById(R.id.btnSimpleVertex) as Button
    }

    private val btnTexWrap by lazy {
        findViewById(R.id.btnTexWrap) as Button
    }

    private val btnMipMap2D by lazy {
        findViewById(R.id.btnMipMap2D) as Button
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTri.setOnClickListener(this)
        btnLsn1.setOnClickListener(this)
        btnVerBuf.setOnClickListener(this)
        btnVerArray.setOnClickListener(this)
        btnMapBuffer.setOnClickListener(this)
        btnSimpleVertext.setOnClickListener(this)
        btnTexWrap.setOnClickListener(this)
        btnMipMap2D.setOnClickListener(this)
    }


}