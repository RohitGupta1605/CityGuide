package com.example.cityexplorer

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.cityexplorer.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var actionbar: ActionBar

    private lateinit var progressDialog:ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth
    private var email=""
    private var password=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionbar = supportActionBar!!
        actionbar.title = "Login"

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        checkuser()
        binding.signup.setOnClickListener {
        startActivity(Intent(this, signupactivity::class.java))
    }
        binding.Login.setOnClickListener{
            validateData()
        }


    }

    private fun validateData() {
        email=binding.Emailid.text.toString().trim()
        password=binding.Password.text.toString().trim()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.Emailid.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(password)){
            binding.Password.error="Please enter password"
        }
        else{
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val firebaseUser =firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Logged In as $email",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,Dashboard::class.java))
                finish()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,"Login Failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkuser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser?.email!=null){
            startActivity(Intent(this,Dashboard::class.java))
        }
    }
}


