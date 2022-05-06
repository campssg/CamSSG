package com.example.graduationproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.Api.Response.MartListResponse
import com.example.graduationproject.R
import org.w3c.dom.Text

class MartListAdapter(val itemList: ArrayList<MartListResponse>): RecyclerView.Adapter<MartListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MartListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_martlist, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MartListAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener.onLongClick(it, position)
            return@setOnLongClickListener true
        }
        holder.name.text = itemList[position].martName
        holder.time.text = "${itemList[position].openTime}~${itemList[position].closeTime}"
        holder.address.text = itemList[position].martAddress
        holder.distance.text = itemList[position].distance.toInt().toString()+"m"
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_list_name)
        val time: TextView = itemView.findViewById(R.id.tv_list_time)
        val address: TextView = itemView.findViewById(R.id.tv_list_address)
        val distance: TextView = itemView.findViewById(R.id.distance)
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

    fun setItemLongClickListener(onItemLongClickListener: MartListAdapter.OnItemLongClickListener) {
        this.itemLongClickListener = onItemLongClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
    private lateinit var itemLongClickListener: MartListAdapter.OnItemLongClickListener
}