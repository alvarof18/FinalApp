package com.alvaro.finalApp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alvaro.finalApp.R
import com.alvaro.finalApp.models.Message
import com.alvaro.finalApp.models.totalMessageEvent
import com.alvaro.finalApp.toast
import com.alvaro.finalApp.utils.CircleTransform
import com.alvaro.finalApp.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.fragment_chat_item_rigth.view.*
import kotlinx.android.synthetic.main.fragment_info.view.*


class InfoFragment : Fragment() {

    private lateinit var _view: View

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatdbRef: CollectionReference

    private var chatSubcription: ListenerRegistration? = null
    private lateinit var infoBusListener : Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_info, container, false)
        setUpchatDB()
        setUpcurrentUser()
        setCurrentUserInfoUI()

//        suscribeToTotalMessagesFirebaseStyle()
        suscribeToTotalMessagesEventBusReactiveStyle()

        return _view
    }

    private fun setUpchatDB() {
        chatdbRef = store.collection("chat")
    }

    private fun setUpcurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setCurrentUserInfoUI() {
        _view.textViewInfoEmail.text = currentUser.email
        _view.textViewInfoName.text = currentUser.displayName?.let { currentUser.displayName }
            ?: run { getString(R.string.info_no_name) }

        currentUser.photoUrl?.let {
            //Picasso
            Picasso.get().load(currentUser.photoUrl)
                .resize(300, 300)
                .centerCrop()
                .transform(CircleTransform())
                .into(_view.imageViewInfoAvatar)
        } ?: run {
            Picasso.get().load(R.mipmap.ic_person)
                .resize(300, 300)
                .centerCrop()
                .transform(CircleTransform())
                .into(_view.imageViewInfoAvatar)
        }
    }

    private fun suscribeToTotalMessagesFirebaseStyle() {
        chatSubcription = chatdbRef.addSnapshotListener(object :
            java.util.EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
            override fun onEvent(snapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
                error?.let {
                    activity!!.toast("Exception")
                    return
                }
                snapshot?.let {_view.textViewInfoTotalMessages.text = "${it.size()}"}
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun suscribeToTotalMessagesEventBusReactiveStyle(){
        infoBusListener =  RxBus.listen(totalMessageEvent::class.java).subscribe {
            _view.textViewInfoTotalMessages.text = "${it.total}"
        }
    }
    override fun onDestroyView() {
        infoBusListener.dispose()
        chatSubcription?.remove()
        super.onDestroyView()
    }
}
