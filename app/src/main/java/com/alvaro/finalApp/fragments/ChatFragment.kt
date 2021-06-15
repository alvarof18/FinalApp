package com.alvaro.finalApp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvaro.finalApp.R
import com.alvaro.finalApp.adapters.ChatAdapter
import com.alvaro.finalApp.models.Message
import com.alvaro.finalApp.models.totalMessageEvent
import com.alvaro.finalApp.toast
import com.alvaro.finalApp.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatFragment : Fragment() {

    private lateinit var _view: View
    private lateinit var adapter:ChatAdapter

    private val messageList:ArrayList<Message> = ArrayList()

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatdbRef: CollectionReference

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private var chatSubcription: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _view = inflater.inflate(R.layout.fragment_chat, container, false)

        setUpchatDB()
        setUpcurrentUser()
        setUpRecyclerView()
        setUpChatButton()

        subscribeToChatMessages()
        return _view
    }

    private fun setUpchatDB(){
        chatdbRef = store.collection("chat")
    }

    private fun setUpcurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    private fun setUpRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(messageList,currentUser.uid)
        _view.recyclerView.setHasFixedSize(true)
        _view.recyclerView.layoutManager =layoutManager
        _view.recyclerView.itemAnimator = DefaultItemAnimator()
        _view.recyclerView.adapter = adapter
    }

    private fun setUpChatButton(){
        _view.ButtonSend.setOnClickListener {
        val messageText = _view.editTextMessage.text.toString()
            if (messageText.isNotEmpty()){
                val photo = currentUser.photoUrl?.let { currentUser.photoUrl.toString() } ?: run {""}
                val message = Message(currentUser.uid,messageText,photo,Date())
                // guardamos el mensaje
                saveMessage(message)
                // Limpiamos
                _view.editTextMessage.setText("")
            }
        }

    }

    private fun saveMessage(message:Message){
        val newMessage= HashMap<String,Any>()
        newMessage["authorId"] = message.authorId
        newMessage["message"] = message.message
        newMessage["profileImageUrl"] = message.profileImageUrl
        newMessage["SendAt"] = message.SendAt

        chatdbRef.add(newMessage)
            .addOnCompleteListener {
        activity!!.toast("message added!")
        }
            .addOnFailureListener {
                activity!!.toast("message error, Try Again!")
            }
    }
    // Este se va a disparar cada vez que alguien edite, borre o envie un mensaje
    private fun subscribeToChatMessages(){
        chatSubcription = chatdbRef
                .orderBy("SendAt",Query.Direction.DESCENDING)
                .limit(100)
                .addSnapshotListener (object:EventListener,com.google.firebase.firestore.EventListener<QuerySnapshot>{
                override fun onEvent(snapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    error?.let {
                    activity!!.toast("Exception")
                    return
                }
                    snapshot?.let {
                        messageList.clear()
                        val messages = it.toObjects(Message::class.java)
                        messageList.addAll(messages.asReversed())
                        adapter.notifyDataSetChanged()
                        _view.recyclerView.smoothScrollToPosition(messageList.size)
                        RxBus.publish(totalMessageEvent(messageList.size))
                    }
                }
            })
    }
    override fun onDestroyView() {
        chatSubcription?.remove()
        super.onDestroyView()
    }

}