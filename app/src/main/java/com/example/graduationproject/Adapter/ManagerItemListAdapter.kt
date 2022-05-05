package com.example.graduationproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.graduationproject.Api.Response.ProductList2
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.R

class ManagerItemListAdapter(val itemList: ArrayList<ProductList2>): RecyclerView.Adapter<ManagerItemListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_manage_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = itemList[position].productName
        holder.price.text = itemList[position].productPrice.toString()
        holder.stock.text = itemList[position].productStock.toString()
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener.onLongClick(it, position)
            return@setOnLongClickListener true
        }
        if (!itemList[position].productImgUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(itemList[position].productImgUrl).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.hamburger)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageView4)
        val name: TextView = itemView.findViewById(R.id.name_manageItem)
        val price: TextView = itemView.findViewById(R.id.price_manageItem)
        val stock: TextView = itemView.findViewById(R.id.stock_manageItem)
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