package com.example.kkon

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.activity_image_big.*
import kotlinx.android.synthetic.main.listlayout.*

class imageBig : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_big)
        val i=intent
        val name=i.getStringExtra("greenjoa").toString()
        Log.d("account_cnt","newactivity")
        Ion.with(imageView5).load(name)
        imageBig_btn.setOnClickListener {
            finish()
        }
    }
}
