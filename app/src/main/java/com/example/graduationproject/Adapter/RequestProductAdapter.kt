package com.example.graduationproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.Api.Response.RequestProductResponse
import com.example.graduationproject.R
import org.w3c.dom.Text

class RequestProductAdapter(val itemList: ArrayList<RequestProductResponse>): RecyclerView.Adapter<RequestProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_1requested_products, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RequestProductAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.productName.text = itemList[position].requestedProductName
        holder.price.text = itemList[position].requestedProductPrice.toString()
        holder.count.text = itemList[position].requestedProductCount.toString()
        holder.reference.text = itemList[position].requestedProductReference
        holder.state.text = itemList[position].requestedProductState
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val martName: TextView = itemView.findViewById(R.id.mart_name)
        val productName: TextView = itemView.findViewById(R.id.name_requestedItem)
        val price: TextView = itemView.findViewById(R.id.price_requestedItem)
        val count: TextView = itemView.findViewById(R.id.num_requestedItem)
        val reference: TextView = itemView.findViewById(R.id.reference)
        val state: TextView = itemView.findViewById(R.id.order_state)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener
}