package com.example.kkon

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu.*

class Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu2)
        init()
    }
    fun init(){
        button1.setOnClickListener {
            val i= Intent(applicationContext,Board::class.java) //공지사항화면으로 전환
            startActivity(i)
        }
        button2.setOnClickListener {
            val i=Intent(applicationContext,MainActivity::class.java) //민원화면으로 전환
            startActivity(i)
        }
    }
}
