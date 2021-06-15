package com.alvaro.finalApp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.finalApp.R
import com.alvaro.finalApp.adapters.RatesAdapter
import com.alvaro.finalApp.dialogues.RateDialog
import com.alvaro.finalApp.models.NewRateEvent
import com.alvaro.finalApp.models.rates
import com.alvaro.finalApp.toast
import com.alvaro.finalApp.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_rates.view.*
import kotlinx.android.synthetic.main.fragment_rates.view.recyclerView
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RatesFragment : Fragment() {

    private lateinit var _view : View
    private lateinit var adapter: RatesAdapter
    private  val ratesList : ArrayList<rates> = ArrayList()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var ratedbRef: CollectionReference
    private var rateSubcription: ListenerRegistration? = null

    private lateinit var rateBusListener : Disposable
    private lateinit var scrollListener : RecyclerView.OnScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_rates, container, false)

        setUpRateDB()
        setUpcurrentUser()

        setUpRecyclerView()
        setUpFab()

        subcribeToRatings()
        subcribeToNewRating()

        return _view
    }

    private fun setUpRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        adapter = RatesAdapter(ratesList)

        _view.recyclerView.setHasFixedSize(true)
        _view.recyclerView.layoutManager = layoutManager
        _view.recyclerView.itemAnimator = DefaultItemAnimator()
        _view.recyclerView.adapter = adapter

        scrollListener = object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0 || dy < 0 && _view.fabRating.isShown){
                    _view.fabRating.hide()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    _view.fabRating.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        }
        _view.recyclerView.addOnScrollListener(scrollListener)
    }

    private fun setUpFab(){
        _view.fabRating.setOnClickListener { RateDialog().show(fragmentManager!!,"") }

    }

    private fun setUpRateDB(){
        ratedbRef = store.collection("rates")
    }

    private fun setUpcurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    private fun saveRate(rate:rates){
        val newRate= HashMap<String,Any>()
        newRate["userId"] = rate.userId
        newRate["text"] = rate.text
        newRate["rate"] = rate.rate
        newRate["createdAt"] = rate.createdAt
        newRate["imageUrl"] = rate.profileImageUrl

        ratedbRef.add(newRate).addOnCompleteListener {
            activity!!.toast("Rate saved")
        }
            .addOnFailureListener {
                activity!!.toast("Rate can't be saved")
            }
    }

    private fun subcribeToRatings(){
        rateSubcription = ratedbRef
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener (object:
                EventListener,com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(snapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    error?.let {
                        activity!!.toast("Exception")
                        return
                    }
                    snapshot?.let {
                        ratesList.clear()
                        val rates = it.toObjects(rates::class.java)
                        ratesList.addAll(rates)
                        hideFabRated(hasUserRate(ratesList))
                        adapter.notifyDataSetChanged()
                        _view.recyclerView.smoothScrollToPosition(0)
                       // RxBus.publish(totalMessageEvent(ratesList.size))
                    }
                }

            })
    }

    private fun subcribeToNewRating(){
       rateBusListener =  RxBus.listen(NewRateEvent::class.java).subscribe {
            saveRate(it.rate)
        }
    }

    private fun hasUserRate(rates:ArrayList<rates>): Boolean{
        var result = false
        rates.forEach{
            if(it.userId == currentUser.uid) {
                result = true
            }
        }
        return result
    }

    private fun hideFabRated(rated:Boolean){
        if (rated){
            _view.fabRating.hide()
            _view.recyclerView.removeOnScrollListener(scrollListener)
        }
    }

    override fun onDestroyView() {
        _view.recyclerView.removeOnScrollListener(scrollListener)
        rateBusListener.dispose()
        rateSubcription?.remove()
        super.onDestroyView()
    }
}