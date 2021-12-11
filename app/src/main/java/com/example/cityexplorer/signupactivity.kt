package com.example.cityexplorer

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.cityexplorer.databinding.ActivityLoginBinding
import com.example.cityexplorer.databinding.ActivitySignupactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signupactivity.*

class signupactivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupactivityBinding
    private lateinit var actionbar: ActionBar
    lateinit var database: SharedPreferences

    private lateinit var progressDialog: ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth
    private var firedata=FirebaseDatabase.getInstance().getReference("User")
    data class User(val username: String? = null, val ugender: String? = null,val udob: String? = null,val phnum: String? = null) {

    }
    private var email=""
    private var password=""
    private var phonenum=""
    private var gender=""
    private var dob=""
    private var name=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signupactivity)

        database = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        binding = ActivitySignupactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionbar = supportActionBar!!
        actionbar.title = "Sign Up"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayShowHomeEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth= FirebaseAuth.getInstance()
        button2.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        binding.Regist.setOnClickListener {
            validateData()
        }

    }

    private fun validateData() {
        email=binding.EntEmail.text.toString().trim()
        password=binding.entpassword.text.toString().trim()
        name=binding.name.text.toString().trim()
        dob=DOB.text.toString().trim()
        phonenum=binding.phno.text.toString().trim()

        if(binding.Malegen.isChecked){
            gender="Male"
        }
        else if(binding.Femalegen.isChecked){
            gender="Female"
        }
        else if(binding.Othersgen.isChecked){
            gender="Other"
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.EntEmail.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(password) or TextUtils.isEmpty(name) or TextUtils.isEmpty(phonenum) or TextUtils.isEmpty(dob) or TextUtils.isEmpty(gender)){
            Toast.makeText(this,"please fill required fields", Toast.LENGTH_SHORT).show()
        }
        else if (password.length<6){
            binding.entpassword.error="password must be atleast 6 characters long"
        }
        else{

            firebaseSignUp()
        }
    }

    fun setuserdata(email:String,name: String, phonenum: String, dob: String, gender: String) {
        val dataeditor: SharedPreferences.Editor = database.edit()
        dataeditor.putString(email.toString(),name+ "&" +phonenum+"&"+gender+"&"+dob)
        ProfileActivity().setdata(name,phonenum,dob,gender)
    }


    private fun firebaseSignUp() {
        val user=User(name,gender,dob,phonenum)
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            progressDialog.dismiss()
            val firebaseUser =firebaseAuth.currentUser
            val email=firebaseUser!!.email

            firedata.child(email.toString()).setValue(user)

            setuserdata(email.toString(),name,phonenum,dob,gender)
            startActivity(Intent(this,Dashboard::class.java))
        }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,"SignUp Failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }
}