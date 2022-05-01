package com.example.graduationproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.User.CartItem


//장바구니 어댑터
class CartListAdapter(val itemList: ArrayList<CartItem>): RecyclerView.Adapter<CartListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_cart_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CartListAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position)
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener.onLongClick(it, position)
            return@setOnLongClickListener true
        }
        holder.cartlistItem_name.text = itemList[position].cartItemName
        holder.cartlistItem_price.text = itemList[position].cartItemPrice.toString()
        holder.cartlistItem_Num.text = itemList[position].cartItemCount.toString()
        if (!itemList[position].cartItemImgUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(itemList[position].cartItemImgUrl).into(holder.cartlistItem_Img)
        } else {
            holder.cartlistItem_Img.setImageResource(R.drawable.hamburger)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartlistItem_name: TextView = itemView.findViewById(R.id.cartlistItem_name)
        val cartlistItem_price: TextView = itemView.findViewById(R.id.cartlistItem_price)
        val cartlistItem_Num: TextView = itemView.findViewById(R.id.cartlistitem_amount)
        val cartlistItem_Img: ImageView = itemView.findViewById(R.id.imageView7)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    interface OnItemLongClickListener {
        fun onLongClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun setItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        this.itemLongClickListener = onItemLongClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var itemLongClickListener: OnItemLongClickListener
}