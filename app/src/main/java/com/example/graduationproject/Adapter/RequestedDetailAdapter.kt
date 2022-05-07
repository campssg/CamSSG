package com.example.graduationproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.Api.Response.RequestedProductList
import com.example.graduationproject.Api.Response.orderlist
import com.example.graduationproject.R

class RequestedDetailAdapter(val itemList:ArrayList<RequestedProductList>): RecyclerView.Adapter<RequestedDetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_requested_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.count.text = itemList[position].requestedProductCount.toString()
        holder.name.text = itemList[position].requestedProductName
        holder.price.text = itemList[position].requestedProductPrice.toString()
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val name:TextView = itemView.findViewById(R.id.detailorderlist_itemname)
        val count:TextView = itemView.findViewById(R.id.detailorderlist_itemcount)
        val price:TextView = itemView.findViewById(R.id.detailorderlist_itemprice)
    }
}