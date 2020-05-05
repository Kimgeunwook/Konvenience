package com.example.kkon


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_board_detail.*

class Board_detail : AppCompatActivity() {

    val linkStr = "http://home.konkuk.ac.kr:80/cms/Common/MessageBoard/ArticleRead.do?forum=11914&id=" // 뒤에 아이디값 추가하면 접속됨

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        init()
    }

    fun init() {
        btn_board_back.setOnClickListener {
            val intent = Intent(this@Board_detail, Board::class.java)
            startActivity(intent)
        }

        val i = intent
        val menuId = i.getStringExtra("menuId")
//        Toast.makeText(this, menuId, Toast.LENGTH_SHORT).show()

        val link = linkStr + menuId
//        Log.v("url link", link)

        webView_board.loadUrl(link)
    }

}