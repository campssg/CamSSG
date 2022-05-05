package com.example.graduationproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.Api.Response.UserOrderListResponse
import com.example.graduationproject.R

class UserOrderListAdapter(val itemList: ArrayList<UserOrderListResponse>):RecyclerView.Adapter<UserOrderListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserOrderListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_order_item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: UserOrderListAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.martName.text = itemList[position].martName
        holder.orderId.text = itemList[position].orderId.toString()
        holder.orderState.text = itemList[position].orderState
        holder.totalPrice.text = itemList[position].totalPrice.toString()
        holder.pickup_day.text = itemList[position].pickup_day
        holder.pickup_time.text = itemList[position].pickup_time
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val martName: TextView = itemView.findViewById(R.id.martname_orderItem)
        val orderId : TextView = itemView.findViewById(R.id.order_number)
        val orderState : TextView = itemView.findViewById(R.id.order_state)
        val pickup_day : TextView = itemView.findViewById(R.id.textView30)
        val pickup_time : TextView = itemView.findViewById(R.id.pickuptime_orderItem)
        val totalPrice : TextView = itemView.findViewById(R.id.price_orderItem)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}