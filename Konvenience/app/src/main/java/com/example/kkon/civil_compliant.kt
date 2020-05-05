package com.example.kkon

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_civil_compliant.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class civil_compliant : AppCompatActivity() { //로그인해서 들어왔을때 화면
    var data:ArrayList<Data> = ArrayList()
    lateinit var adapter:Myadater
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    var data_change_flag=0
    var flag=0
    var civil_email=""
    var civil_status=""
    val database = FirebaseDatabase.getInstance()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                val pass1=data?.getStringExtra("pass1")
                val pass2=data?.getStringExtra("pass2")
                var pass3=data?.getIntExtra("pass3",-1)
                val pass4=data?.getStringExtra("pass4")
                val database : FirebaseDatabase = FirebaseDatabase.getInstance()
                val myRef : DatabaseReference = database.getReference("user")
                myRef.child("user$pass3").child("email").setValue(pass1) //user1 email status
                myRef.child("user$pass3").child("status").setValue(pass2)
                myRef.child("user$pass3").child("writer").setValue(civil_email)
                var file = Uri.fromFile(File(pass4.toString()))
                val riversRef = storageRef.child("images/"+"user$pass3")
                riversRef.putFile(file).addOnSuccessListener {
                    riversRef.downloadUrl.addOnCompleteListener {
                        var uri = it.result
                        myRef.child("user$pass3").child("img").setValue(uri.toString())
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_civil_compliant)
        val myRef_onCreate : DatabaseReference = database.getReference("user_cnt")
        myRef_onCreate.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                data_change_flag=p0.child("cnt").value.toString().toInt()
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val myRef = database.getReference("user")
        val i=intent
        civil_email=i.getStringExtra("user_email") //로그인한 이메일 받아오기
        /////////////////////////////////////////////////////컴플릿 명단에 있으면 알림오게하기
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@civil_compliant, R.style.Theme_AppCompat_Light_Dialog))
        val myRef_complete : DatabaseReference = database.getReference("complete")
        myRef_complete.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children) {
                    if(civil_email==snapshot.child("writer").value.toString())
                    {
                        val email = snapshot.child("email").value.toString()
                        val status = snapshot.child("status").value.toString()
                        //////////////////////////////////////
                        myRef_complete.child(snapshot.key.toString()).removeValue()
                        builder.setTitle("알림")
                        builder.setMessage("회원님dl 좋아요 한 (" + email + " , " + status + ") 민원이 처리되었습니다.")
                        builder.setPositiveButton("확인") { _, _ ->

                        }
//                        builder.setNegativeButton("취소") { _, _ ->
//                        }
                        if(civil_email!="") {
                            builder.show()
                        }
                        //////////////////////////////////////
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        ////////////////////////////////////////

        civil_compliant_add.setOnClickListener {
            //민원 추가하는 버튼튼

            val i= Intent(applicationContext,complian_add::class.java)
            //  val database77777 : FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef_user_cnt : DatabaseReference = database.getReference("user_cnt")
            myRef_user_cnt.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    data_change_flag=p0.child("cnt").value.toString().toInt()
                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })
            data_change_flag++
            myRef_user_cnt.child("cnt").setValue(data_change_flag)
            i.putExtra("cntt",data_change_flag)
            i.putExtra("user_emaill",civil_email)
            startActivityForResult(i,123)
        }
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                data.clear()
                for (snapshot in p0.children) {
                    /////////////////////////
                    flag=0
                    Log.d("data_change_flag","no77")
                    for(snapshot2 in snapshot.child("likepeople").children){
                        if(civil_email==snapshot2.value.toString())
                        {
                            flag=1
                        }
                    }

                    ////////////////////////
                    if(flag==1) {
                        Log.d("data_change_flag","no2")
                        data.add(
                            Data(
                                snapshot.child("email").value.toString(),
                                snapshot.child("status").value.toString(),
                                1,snapshot.child("img").value.toString()
                            )
                        )
                        flag=0
                    }else{
                        Log.d("data_change_flag","no9")
                        data.add(
                            Data(
                                snapshot.child("email").value.toString(),
                                snapshot.child("status").value.toString(),
                                0,snapshot.child("img").value.toString()
                            )
                        )
                        flag=0
                    }


                }
                init()
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        civil_email=""
    }
    fun init(){
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        listview.layoutManager = layoutManager
        adapter = Myadater(data)
        listview.adapter = adapter
        val myRef = database.getReference("user")
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@civil_compliant, R.style.Theme_AppCompat_Light_Dialog))
        adapter.itemClickListener=object:Myadater.OnItemClickListener{
            override fun OnimageClick(holder: Myadater.ViewHolder, view: View, data: Data, position: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                val i=Intent(applicationContext,imageBig::class.java)
                i.putExtra("greenjoa",data.img)
                startActivity(i)
            }

            override fun OnLikeClick(holder: Myadater.ViewHolder, view: View, data: Data, position: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                when(data.like){
                    0 -> {
                        Log.d("data_change_flag","no3")
                        data.like=1
                        var data_id=data.Id.toString()
                        var data_sta=data.sta.toString()
                        //////////////////////
                        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (snapshot in dataSnapshot.children) {
                                    if(data_id==snapshot.child("email").value.toString()&&data_sta==snapshot.child("status").value.toString()) //어댑터 아이템의 장소와 디비테이블의 장소를 비교
                                    {
                                        ///////////////////////////////////
                                        val myRef_user_cnt : DatabaseReference = database.getReference("user_cnt")
                                        myRef_user_cnt.addValueEventListener(object: ValueEventListener {
                                            override fun onDataChange(p0: DataSnapshot) {
                                                data_change_flag=p0.child("cnt").value.toString().toInt()
                                            }
                                            override fun onCancelled(p0: DatabaseError) {
                                            }
                                        })
                                        data_change_flag++
                                        ///////////////////////////////////
                                        myRef.child(snapshot.key.toString()).child("likepeople").child("people$data_change_flag").setValue(civil_email)
                                        myRef_user_cnt.child("cnt").setValue(data_change_flag)
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                            }
                        })
                        //////////////////////
                    }

                    1 -> {
                        Log.d("data_change_flag","no4")
                        data.like=0
                        var data_id=data.Id.toString()
                        //////////////////////
                        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (snapshot in dataSnapshot.children) {
                                    if(data_id==snapshot.child("email").value.toString()) //어댑터 아이템의 장소와 디비테이블의 장소를 비교
                                    {
                                        ///////////////////////////////////////////
                                        for(snapshot2 in snapshot.child("likepeople").children){
                                            if(civil_email==snapshot2.value.toString())
                                            {
                                                myRef.child(snapshot.key.toString()).child("likepeople").child(snapshot2.key.toString()).removeValue()

                                            }
                                        }
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                            }
                        })
                    }
                }
                //init()
            }

            override fun OnItemClick(holder: Myadater.ViewHolder, view: View, data: Data, position: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }

}