package com.example.graduationproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.Api.Response.orderlist
import com.example.graduationproject.R

class DetailOrderListAdapter(val itemList:ArrayList<orderlist>): RecyclerView.Adapter<DetailOrderListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_detail_order_list_user, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {


        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position)
        }

        holder.count.text = itemList[position].orderItemCount.toString()
        holder.name.text = itemList[position].orderItemName
        holder.price.text = itemList[position].orderItemPrice.toString()




    }



    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val name:TextView = itemView.findViewById(R.id.detailorderlist_itemname)
        val count:TextView = itemView.findViewById(R.id.detailorderlist_itemcount)
        val price:TextView = itemView.findViewById(R.id.detailorderlist_itemprice)

    }






    interface OnItemClickListener{
        fun onClick(v:View,position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
    private lateinit var itemClickListener:OnItemClickListener
}