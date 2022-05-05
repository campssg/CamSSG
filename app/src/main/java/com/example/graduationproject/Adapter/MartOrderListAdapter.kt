package com.example.graduationproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.Api.Response.MartOrderListResponse
import com.example.graduationproject.R

class MartOrderListAdapter(val itemList: ArrayList<MartOrderListResponse>):RecyclerView.Adapter<MartOrderListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MartOrderListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_order_item_mart, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MartOrderListAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.userName.text = itemList[position].userName
        holder.orderId.text = itemList[position].orderId.toString()
        holder.orderState.text = itemList[position].orderState
        holder.order_phoneNumber.text = itemList[position].order_phoneNumber
        holder.pickup_day.text = itemList[position].pickup_day
        holder.pickup_time.text = itemList[position].pickup_time
        holder.totalPrice.text = itemList[position].totalPrice.toString()
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val userName : TextView = itemView.findViewById(R.id.user_name)
        val orderId : TextView = itemView.findViewById(R.id.order_number)
        val orderState : TextView = itemView.findViewById(R.id.order_state)
        val pickup_day : TextView = itemView.findViewById(R.id.textView30)
        val pickup_time : TextView = itemView.findViewById(R.id.pickuptime_orderItem)
        val totalPrice : TextView = itemView.findViewById(R.id.price_orderItem)
        val order_phoneNumber : TextView = itemView.findViewById(R.id.order_phoneNumber)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}