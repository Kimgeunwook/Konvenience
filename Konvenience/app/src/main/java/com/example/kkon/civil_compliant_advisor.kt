package com.example.kkon
import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.AdapterView
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_civil_compliant_advisor.*


class civil_compliant_advisor : AppCompatActivity() { //로그인해서 들어왔을때 화면
    var data:ArrayList<Data> = ArrayList()
    lateinit var adapter:Myadater
    var data_cnt=0
    var flag=0
    var civil_email=""
    val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_civil_compliant_advisor)
        val myRef_user_cnt : DatabaseReference = database.getReference("user_cnt")
        spin_advisor.onItemSelectedListener=SpinnerSelectedListener()
        myRef_user_cnt.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                data_cnt=p0.child("cnt").value.toString().toInt()
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val myRef = database.getReference("user")
        val i=intent
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                data.clear()
                for (snapshot in p0.children) {
                    if(spin_advisor.selectedItem.toString()!="전체"){
                        if(spin_advisor.selectedItem.toString()==snapshot.child("email").value.toString()) {
                            data.add(
                                Data(
                                    snapshot.child("email").value.toString(),
                                    snapshot.child("status").value.toString(),
                                    0,snapshot.child("img").value.toString()
                                )
                            )
                        }
                    }else{
                        data.add(
                            Data(
                                snapshot.child("email").value.toString(),
                                snapshot.child("status").value.toString(),
                                0,snapshot.child("img").value.toString()
                            )
                        )
                    }
                }
                init()
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun init(){
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        listview2.layoutManager = layoutManager
        adapter = Myadater(data)
        listview2.adapter = adapter
        val myRef = database.getReference("user")
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@civil_compliant_advisor, R.style.Theme_AppCompat_Light_Dialog))
        adapter.itemClickListener=object:Myadater.OnItemClickListener{
            override fun OnimageClick(holder: Myadater.ViewHolder, view: View, data: Data, position: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

               val i=Intent(applicationContext,imageBig::class.java)
               i.putExtra("greenjoa",data.img)
               startActivity(i)
            }
            override fun OnLikeClick(holder: Myadater.ViewHolder, view: View, data: Data, position: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

            }

            override fun OnItemClick(holder: Myadater.ViewHolder, view: View, data: Data, position: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                var data_id=data.Id.toString()
                var data_sta=data.sta.toString()
                //////////////////////////////////////

                builder.setTitle("진짜 진짜 ")
                builder.setMessage("진짜로 지우시겠습니까?")
                builder.setPositiveButton("확인") { _, _ ->
                    myRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            for (snapshot in dataSnapshot.children) {
                                if(data_id==snapshot.child("email").value.toString()&&data_sta==snapshot.child("status").value.toString()) //어댑터 아이템의 장소와 디비테이블의 장소를 비교
                                {
                                    myRef.child(snapshot.key.toString()).removeValue()

                                    val myRef_user_cnt : DatabaseReference = database.getReference("user_cnt")
                                    val myRef_complete : DatabaseReference = database.getReference("complete")

                                    myRef_user_cnt.addValueEventListener(object: ValueEventListener {
                                        override fun onDataChange(p0: DataSnapshot) {
                                            data_cnt=p0.child("cnt").value.toString().toInt()
                                        }
                                        override fun onCancelled(p0: DatabaseError) {
                                        }
                                    })
                                    for(snapshot2 in snapshot.child("likepeople").children) {
                                        data_cnt++
                                        myRef_complete.child("complete_id$data_cnt").child("email")
                                            .setValue(snapshot.child("email").value.toString())
                                        myRef_complete.child("complete_id$data_cnt").child("status")
                                            .setValue(snapshot.child("status").value.toString())
                                        myRef_complete.child("complete_id$data_cnt").child("writer")
                                            .setValue(snapshot2.value.toString())
                                        myRef_user_cnt.child("cnt").setValue(data_cnt)
                                    }
                                    ///////////////
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                        }
                    })
                }
                builder.setNegativeButton("취소") { _, _ ->
                }
                builder.show()
                //////////////////////////////////////

            }

        }
    }
    inner class SpinnerSelectedListener: AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            val myRef = database.getReference("user")
            myRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    data.clear()
                    for (snapshot in p0.children) {
                        if(spin_advisor.selectedItem.toString()!="전체"){
                            if(spin_advisor.selectedItem.toString()==snapshot.child("email").value.toString()) {
                                data.add(
                                    Data(
                                        snapshot.child("email").value.toString(),
                                        snapshot.child("status").value.toString(),
                                        0,snapshot.child("img").value.toString()
                                    )
                                )
                            }
                        }else{
                            data.add(
                                Data(
                                    snapshot.child("email").value.toString(),
                                    snapshot.child("status").value.toString(),
                                    0,snapshot.child("img").value.toString()
                                )
                            )
                        }
                    }
                    init()
                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })
        }
    }

}
