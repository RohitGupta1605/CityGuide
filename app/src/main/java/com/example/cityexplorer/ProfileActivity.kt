package com.example.cityexplorer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var database: SharedPreferences
    private var phnum=""
    private var uname=""
    private var dob=""
    private var gender=""
    private var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        goback.setOnClickListener {
            startActivity(Intent(this,Dashboard::class.java))
        }
        firebaseAuth= FirebaseAuth.getInstance()
        var data:String
        database = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        email=firebaseAuth.currentUser?.email.toString()
        data = database.getString(email,"").toString()
        val strs = data.split("&").toTypedArray()
        uname=strs[0]
        phnum=strs[1]
        dob=strs[3]
        gender=strs[2]
        nameprofile.setText(uname)
        phoneprofile.setText(phnum)
        Dobprofile.setText(dob)
        genprofile.setText(gender)
        emailprofile.setText((email))
    }

    fun setdata(name: String, phonenum: String, db: String, gen: String) {
        uname=name
        phnum=phonenum
        dob=db
        gender=gen
        saveuserdata(email,name,phonenum,db,gen)
    }
    fun saveuserdata(email:String,name: String, phonenum: String, dob: String, gender: String) {
        val dataeditor: SharedPreferences.Editor = database.edit()
        dataeditor.putString(email,name+ "&" +phonenum+"&"+gender+"&"+dob)
    }
}