package com.alvaro.finalApp

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.regex.Pattern

//import com.squareup.picasso.Picasso


fun Activity.toast(message:CharSequence, duration: Int = Toast.LENGTH_SHORT) = android.widget.Toast.makeText(this,message,duration).show()

fun Activity.toast(resourceId: Int, duration: Int = Toast.LENGTH_SHORT) = android.widget.Toast.makeText(this,resourceId,duration).show()

fun ViewGroup.inflate(layaoutId: Int) = LayoutInflater.from(context).inflate(layaoutId,this, false)!!

//fun ImageView.loadByURL(url:String) = Picasso.get().load(url).into(this)

//fun ImageView.loadByResource(resource:Int) = Picasso.get().load(resource).fit().into(this)

inline fun <reified T:Activity>Activity.goToActivity(noinline init: Intent.() -> Unit = {}){
    val intent = Intent(this,T::class.java)
    intent.init()
    startActivity(intent)
}

fun EditText.validate(validation:(String) -> Unit){
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            validation(editable.toString())
        }
    })
}



fun Activity.isValidEmail(email:String):Boolean{
    val pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(email).matches()
}

fun Activity.isValidPasswrod(password:String):Boolean{
    // 1 lowcase 1 Uppercase 1 number min 4 caracteres
    val passwordPattern = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}"
    val pattern = Pattern.compile(passwordPattern)
    return pattern.matcher(password).matches()
}

fun Activity.isValidConfirmPassword(password: String, confirmPassword:String):Boolean{
    return password==confirmPassword
}




