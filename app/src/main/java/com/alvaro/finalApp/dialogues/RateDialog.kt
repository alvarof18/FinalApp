package com.alvaro.finalApp.dialogues

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.alvaro.finalApp.R
import com.alvaro.finalApp.models.NewRateEvent
import com.alvaro.finalApp.models.rates
import com.alvaro.finalApp.toast
import com.alvaro.finalApp.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.dialog_rate.view.*
import java.util.*

class RateDialog : DialogFragment() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private fun setUpcurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setUpcurrentUser()
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_rate,null)
        val builder = AlertDialog.Builder(context!!)
        builder.apply {
            setPositiveButton(R.string.dialog_ok, DialogInterface.OnClickListener { _, _ ->
                val textRate = view.editTextFeedBack.text.toString()
                if(textRate.isNotEmpty()){
                    val imageUrl = currentUser!!.photoUrl?.toString() ?: run { "" }
                    val rate = rates(currentUser.uid,textRate,view.ratingBarFeedBack.rating,Date(),imageUrl)
                    RxBus.publish(NewRateEvent(rate))
                }else{
                    activity!!.toast("Fill all the fields")
                }
            })
            setNegativeButton(R.string.dialog_cancel, DialogInterface.OnClickListener { _, _ ->
                activity!!.toast("Press Cancel")
                })
                .setView(view)
                .setTitle(R.string.dialog_title)
            return builder.create()
        }
    }
}

/*
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return android.app.AlertDialog.Builder(context!!)
            .setTitle(R.string.dialog_title)
            .setView(R.layout.dialog_rate)
            .setPositiveButton(getString(R.string.dialog_ok)){ dialog, which ->
                activity!!.toast("Press Ok")

                }
            .setNegativeButton(getString(R.string.dialog_cancel)){ dialog, which ->
                activity!!.toast("Press Cancel")
            }
            .create()

    } */

