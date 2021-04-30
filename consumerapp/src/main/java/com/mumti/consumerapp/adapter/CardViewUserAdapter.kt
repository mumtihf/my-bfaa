package com.mumti.consumerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mumti.consumerapp.databinding.ItemCardviewUserBinding
import com.mumti.consumerapp.entity.User
import java.util.ArrayList

class CardViewUserAdapter : RecyclerView.Adapter<CardViewUserAdapter.CardViewHolder>() {

    var mData = ArrayList<User>()
        set(mData) {
            if (mData.size >= 0) {
                field.clear()
            }
            field.addAll(mData)

            notifyDataSetChanged()
        }

    fun setData(items: ArrayList<User>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val mView = ItemCardviewUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(mView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class CardViewHolder(private val binding: ItemCardviewUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User){
            with(binding){
                Glide.with(itemView.context)
                    .load(user.photo)
                    .apply(RequestOptions().override(120, 200))
                    .into(imgUserPhoto)

                tvUsername.text = user.username

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: User)
    }
}