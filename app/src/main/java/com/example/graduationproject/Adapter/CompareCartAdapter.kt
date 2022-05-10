package com.example.graduationproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.CartComparisonItem
import com.example.graduationproject.R

class CompareCartAdapter(val itemList: ArrayList<CartComparisonItem>): RecyclerView.Adapter<CompareCartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_compare_cartlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        if (!itemList[position].martImg.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(itemList[position].martImg).into(holder.image)
        } else {
            Glide.with(holder.itemView.context).load(R.drawable.exampleofmart).into(holder.image)
        }

        holder.martname.text = itemList[position].martName
        if (itemList[position].notExistCnt==0){
            holder.notexist.text = "모든 장바구니 재고 있음"
        }
        else{
            holder.notexist.text = "물품 품목 ${itemList[position].notExistCnt}개, 총 ${itemList[position].notExistTotalCnt}개 재고 없음"
        }
        holder.totalprice.text = itemList[position].totalPrice.toString() + "원"
        holder.distance.text = itemList[position].distance.toInt().toString() + "m"
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageView18)
        val martname: TextView = itemView.findViewById(R.id.compare_cartlist_martname)
        val notexist: TextView = itemView.findViewById(R.id.compare_cartlist_itempresence)
        val totalprice: TextView = itemView.findViewById(R.id.compare_cartlist_totalprice)
        val distance: TextView = itemView.findViewById(R.id.distance)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener

}