package com.example.kkon

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main2.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase



class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG: String = "MainActivity"
    var main_status=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        init()
        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.et_email0)
        val password = findViewById<EditText>(R.id.et_password0)

        //로그인
        bt_login.setOnClickListener {
            bt_login.setBackgroundResource(R.drawable.bg_btn3)
            bt_create.setBackgroundResource(R.drawable.bg_btn2)
            bt_logout.setBackgroundResource(R.drawable.bg_btn2)
            if (email.text.toString().length == 0 || password.text.toString().length == 0) {
                Toast.makeText(this, "email 혹은 password를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            val i=Intent(applicationContext,civil_compliant::class.java) //민원화면으로 전환환
                            val i2=Intent(applicationContext,civil_compliant_advisor::class.java)
                            //////////////////////////////////////////////////////
                            val _database_firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
                            val myRef_account : DatabaseReference = _database_firebase.getReference("account")
                            myRef_account.addValueEventListener(object: ValueEventListener {
                                override fun onDataChange(p0: DataSnapshot) {
                                    for (snapshot in p0.children) {
                                     if(email.text.toString()==snapshot.child("email").value.toString())
                                     {
                                         main_status=snapshot.child("status").value.toString()
                                         i.putExtra("user_email",email.text.toString())
                                         i.putExtra("user_status",main_status)
                                         updateUI(user)
                                         if(main_status=="student"){
                                             startActivity(i)
                                         }
                                         else{
                                             startActivity(i2)
                                         }

                                     }
                                    }
                                }
                                override fun onCancelled(p0: DatabaseError) {
                                }
                            })

                            //////////////////////////////////////////




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(null)
                        }

                        // ...
                    }
            }

        }
        bt_create.setOnClickListener {
            bt_login.setBackgroundResource(R.drawable.bg_btn2)
            bt_create.setBackgroundResource(R.drawable.bg_btn3)
            bt_logout.setBackgroundResource(R.drawable.bg_btn2)
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        bt_logout.setOnClickListener {
            bt_login.setBackgroundResource(R.drawable.bg_btn2)
            bt_create.setBackgroundResource(R.drawable.bg_btn2)
            bt_logout.setBackgroundResource(R.drawable.bg_btn3)
            auth.signOut()
            //로그인 활성화 - 이걸 더 효율적으로 하는 방법이 있을것 같은데 일일히 적어 줘야 해?
            tv_message.setText("로그인이 필요합니다..")
            bt_logout.isEnabled = false
            bt_login.isEnabled = true
            bt_create.isEnabled = true
        }
    }
    fun init(){

        val _database_main : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef_account : DatabaseReference = _database_main.getReference("account")
        val _database_user : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef_user : DatabaseReference = _database_user.getReference("user")



    }
    override fun onResume() {
        super.onResume()
        val currentUser = auth?.currentUser
        updateUI(currentUser)
    }
    override fun onStart() {
        super.onStart()
        //앱 시작 단계에서 사용자가 현재 로그인 되어 있는지 확인하고 처리 해 준다.
        val currentUser = auth?.currentUser
        updateUI(currentUser) //이건 원하는대로 사용자 설정해 주는 부분인듯 하다.
    }



    fun updateUI(cUser : FirebaseUser? = null){
        if(cUser != null) {
            tv_message.setText("로그인 되었습니다.")
            //로그인 버튼과 기타 등등을 사용할 수 없게 함(일괄 묶어서 처리 하는 방법?)
            bt_login.isEnabled = false
            bt_create.isEnabled = false
            bt_logout.isEnabled = true
        } else {
            tv_message.setText("로그인이 필요합니다..")
            bt_logout.isEnabled = false
        }
        et_email0.setText("")
        et_password0.setText("")
        hideKeyboard(et_email0)
        //Toast.makeText(this, "유저: "+cUser.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard(view: View) {
        view?.apply {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}
