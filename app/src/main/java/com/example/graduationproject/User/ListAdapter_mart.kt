package com.example.graduationproject.User

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R

class ListAdapter_mart(val itemList: ArrayList<CartItem>): RecyclerView.Adapter<ListAdapter_mart.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_cart_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cartlistItem_name.text = itemList[position].cartItemName
        holder.cartlistItem_price.text = itemList[position].cartItemPrice.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartlistItem_name: TextView = itemView.findViewById(R.id.cartlistItem_name)
        val cartlistItem_price: TextView = itemView.findViewById(R.id.cartlistItem_price)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener
}