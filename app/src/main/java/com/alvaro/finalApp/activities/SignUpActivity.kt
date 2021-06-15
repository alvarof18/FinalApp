package com.alvaro.finalApp.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.alvaro.finalApp.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*



class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Boton ir atras
        buttonGoLogIn.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        buttonLogUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (isValidEmailAndPassword(email,password)){
                signUpByEmail(email,password)
            }else{
                Toast.makeText(this,"please check your email or password fields",Toast.LENGTH_SHORT).show()
            }
        }

        editTextEmail.validate {
            if(isValidEmail(it)){
               null
            }else{
                editTextEmail.error = "The email is not valid"
            }
        }

        editTextPassword.validate {
            if (isValidPasswrod(it)){
                null
            }else{
                editTextPassword.error = "Password is not valid"
            }
        }

        editTextConfirmPassword.validate {
            if (isValidConfirmPassword(it,editTextPassword.text.toString())){
                null
            }else{
                editTextConfirmPassword.error = "Password is not valid"
            }
        }

    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this) {
                        toast("An email has been send to you. Please, confirm before sign in")
                        val intent = Intent(this,LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                        toast("An error has occurred, please try again")
                 }
            }
    }

    private fun isValidEmailAndPassword(email:String, password: String):Boolean{
        return !email.isNullOrEmpty() &&
                !password.isNullOrEmpty() &&
                password == editTextConfirmPassword.text.toString()
    }

}