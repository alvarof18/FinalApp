package com.alvaro.finalApp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.finalApp.R
import com.alvaro.finalApp.inflate
import com.alvaro.finalApp.models.rates
import com.alvaro.finalApp.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_rates_item.view.*
import java.text.SimpleDateFormat

class RatesAdapter(private val items: List<rates>) : RecyclerView.Adapter<RatesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.fragment_rates_item))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() =  items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bind(rate:rates) = with(itemView) {
        textViewRates.text = rate.text
        textViewStar.text = rate.rate.toString()
        textViewCalendar.text = SimpleDateFormat("dd MM, yyyy").format(rate.createdAt)

        if(rate.profileImageUrl.isEmpty()){
            Picasso
                .get()
                .load(R.mipmap.ic_person)
                .resize(100,100)
                .centerCrop()
                .transform(CircleTransform())
                .into(imageViewProfile)

        }else{
            Picasso
                .get()
                .load(rate.profileImageUrl)
                .resize(100,100)
                .centerCrop()
                .transform(CircleTransform())
                .into(imageViewProfile)

        }


        }
    }
}