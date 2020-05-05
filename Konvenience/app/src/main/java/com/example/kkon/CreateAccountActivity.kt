package com.example.kkon

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_create_account2.*

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG : String = "CreateAccount"

    var account_cnt=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account2)
        auth = FirebaseAuth.getInstance()
        val email = findViewById<EditText>(R.id.et_email)
        val password = findViewById<EditText>(R.id.et_password)
        val _database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef_user_cnt : DatabaseReference = _database.getReference("user_cnt")
        myRef_user_cnt.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                account_cnt=p0.child("cnt").value.toString().toInt()
//                        for (snapshot in p0.children) {
//                            data_change_flag= snapshot.value.toString().toInt()
//                        }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
        //새로운 계정을 생성한다.
        bt_CreateAccount.setOnClickListener {
            //Log.d(TAG, "Data: " + email.text + password.text)

            if (email.text.toString().length == 0 || password.text.toString().length == 0){
                Toast.makeText(this, "email 혹은 password를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            //updateUI(user)
                            // 아니면 액티비티를 닫아 버린다.
                            ///////////////////////////////////////////
                            val _database : FirebaseDatabase = FirebaseDatabase.getInstance()
                            val myRef_user_cnt : DatabaseReference = _database.getReference("user_cnt")
                            myRef_user_cnt.addValueEventListener(object: ValueEventListener {
                                override fun onDataChange(p0: DataSnapshot) {
                                    account_cnt=p0.child("cnt").value.toString().toInt()
//                        for (snapshot in p0.children) {
//                            data_change_flag= snapshot.value.toString().toInt()
//                        }
                                }
                                override fun onCancelled(p0: DatabaseError) {
                                }
                            })
                            account_cnt++
                            myRef_user_cnt.child("cnt").setValue(account_cnt)

                            if(spinner.selectedItem.toString()=="학생")
                            {
                                val _database_student : FirebaseDatabase = FirebaseDatabase.getInstance()
                                val myRef_account : DatabaseReference = _database_student.getReference("account")
                                myRef_account.child("user$account_cnt").child("email").setValue(email.text.toString())
                                myRef_account.child("user$account_cnt").child("status").setValue("student")
                            }
                            else
                            {
                                val _database_not_student : FirebaseDatabase = FirebaseDatabase.getInstance()
                                val myRef_account : DatabaseReference = _database_not_student.getReference("account")
                                myRef_account.child("user$account_cnt").child("email").setValue(email.text.toString())
                                myRef_account.child("user$account_cnt").child("status").setValue("administrator")
                            }
                            /////////////////////////////////////
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            //updateUI(null)
                            //입력필드 초기화
                            email?.setText("")
                            password?.setText("")
                            email.requestFocus()
                        }
                    }
            }
        }


        bt_cancel.setOnClickListener {
            finish()
        }





    }
}
