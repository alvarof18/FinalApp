package com.alvaro.finalApp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alvaro.finalApp.goToActivity
import com.google.firebase.auth.FirebaseAuth

// Login And Main Activity flow
class MainEmptyActivity : AppCompatActivity() {

    private val mAuth : FirebaseAuth =  FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            // Usuario no Logeado
        //mAuth.signOut()
        if (mAuth.currentUser == null){
            goToActivity<LoginActivity> {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }else{
           goToActivity<MainActivity>{
               flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
           }
        }
        finish()
    }
}