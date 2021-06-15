package com.alvaro.finalApp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.finalApp.R
import com.alvaro.finalApp.inflate
import com.alvaro.finalApp.models.Message
import com.alvaro.finalApp.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat_item_left.view.*
import kotlinx.android.synthetic.main.fragment_chat_item_rigth.view.*
import java.text.SimpleDateFormat

class ChatAdapter(val item:List<Message>, val userId:String) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val GLOBAL_MESSAGE = 1
    private val MY_MESSAGE = 2

    private val layoutRight = R.layout.fragment_chat_item_rigth
    private val layoutLeft = R.layout.fragment_chat_item_left

    override fun getItemViewType(position: Int): Int {
        return if (item[position].authorId == userId) MY_MESSAGE else GLOBAL_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            MY_MESSAGE -> ViewHolderR(parent.inflate(layoutRight))
            else -> ViewHolderL(parent.inflate(layoutLeft))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType){
            MY_MESSAGE -> (holder as ViewHolderR).bind(item[position])
            GLOBAL_MESSAGE -> (holder as ViewHolderL).bind(item[position])
        }

    }

    override fun getItemCount(): Int {
        return item.size
    }

    class ViewHolderR(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(message:Message) = with(itemView){
            textViewMessageRight.text = message.message
            textViewTimeRight.text = SimpleDateFormat("hh:mm").format(message.SendAt)

            if (message.profileImageUrl.isEmpty()){
                //Picasso
               Picasso.get().load(R.mipmap.ic_person)
                    .resize(100,100)
                    .centerCrop()
                    .transform(CircleTransform())
                    .into(imageViewProfileRight)
            }else{
                //Picasso
                Picasso.get().load(message.profileImageUrl)
                    .resize(100,100)
                    .centerCrop()
                    .transform(CircleTransform())
                    .into(imageViewProfileRight)
            }
        }

    }

    class ViewHolderL(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(message:Message) = with(itemView){
            textViewMessageLeft.text = message.message
            textViewTimeLeft.text = SimpleDateFormat("hh:mm").format(message.SendAt)

            if (message.profileImageUrl.isEmpty()){
                //Picasso
                Picasso.get().load(R.mipmap.ic_person)
                    .resize(100,100)
                    .centerCrop()
                    .transform(CircleTransform())
                    .into(imageViewProfileLeft)
            }else{
                //Picasso
                Picasso.get().load(message.profileImageUrl)
                    .resize(100,100)
                    .centerCrop()
                    .transform(CircleTransform())
                    .into(imageViewProfileLeft)
            }

            }
        }

    }
