package com.example.kkon

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_complian_add.*

class complian_add : AppCompatActivity() {
    val SELECT_IMAGE = 100
    var _path = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complian_add)
        initPermission()
        val i =intent
        compliant_add_btn.setOnClickListener {
            val _intent= Intent()
            var _cnt=i.getIntExtra("cntt",-1)
            var civil_emaill=i.getStringExtra("user_emaill")
            _intent.putExtra("pass1",compliant_add_spot.selectedItem.toString())
            _intent.putExtra("pass2",compliant_add_content.text.toString())
            _intent.putExtra("pass3",_cnt)
            _intent.putExtra("pass4",_path)
            setResult(Activity.RESULT_OK,_intent)
            if (compliant_add_spot.selectedItem.toString().length == 0 || compliant_add_content.text.toString().length == 0) {
                Toast.makeText(applicationContext, "장소와 내용이 모두 적혀있는지 확인하세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                finish()//여기서 피니시하면 호출한 메인으로 넘어간다
            }
        }

    }
    fun btnClick(view: View) {
        // TODO

        val intent = Intent(Intent.ACTION_PICK)
        intent.type= android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data= android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, SELECT_IMAGE)
    }
    fun initPermission(){
        if(!checkAppPermission (arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))){
            val builder = AlertDialog.Builder(this)
            builder.setMessage("반드시 이미지 데이터에 대한 권한이 허용되어야 합니다.")
                .setTitle("권한 허용")
                .setIcon(R.drawable.abc_ic_star_black_48dp)
            builder.setPositiveButton("OK") { _, _ ->
                askPermission (arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), SELECT_IMAGE );
            }
            val dialog = builder.create()
            dialog.show()
        }else{
            Toast . makeText ( getApplicationContext (),
                "권한이 승인되었습니다." , Toast . LENGTH_SHORT ). show ();
        }
    }


    fun checkAppPermission(requestPermission: Array<String>): Boolean {
        val requestResult = BooleanArray(requestPermission.size)
        for (i in requestResult.indices) {
            requestResult[i] = ContextCompat.checkSelfPermission(
                this,
                requestPermission[i]
            ) == PackageManager.PERMISSION_GRANTED
            if (!requestResult[i]) {
                return false
            }
        }
        return true
    } // checkAppPermission

    fun askPermission(requestPermission: Array<String>, REQ_PERMISSION: Int) {
        ActivityCompat.requestPermissions(
            this, requestPermission,
            REQ_PERMISSION
        )
    } // askPermission

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SELECT_IMAGE -> if (checkAppPermission(permissions)) { //퍼미션 동의했을 때 할 일
                Toast.makeText(this, "권한이 승인됨", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한이 거절됨", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    } // onRequestPermissionsResult

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO
                photozon.setImageURI(data!!.data)
                //Toast.makeText(applicationContext,data!!.data.path.toString(),Toast.LENGTH_LONG).show()
                //_path = data!!.data.toString()
                _path = getPath(data!!.data)
//                Toast.makeText(applicationContext,_path,Toast.LENGTH_LONG).show()
            }
        }
    }
    fun getPath(uri : Uri) :String {
        val proj : Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor :Cursor = getContentResolver().query(uri, proj, null, null, null)
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

}