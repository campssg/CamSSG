package com.example.graduationproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.Api.Response.CategoryCheckListResponse
import com.example.graduationproject.R
import com.example.graduationproject.User.ChecklistCategory

class ChecklistAdapter(val itemList:ArrayList<CategoryCheckListResponse>):RecyclerView.Adapter<ChecklistAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ChecklistAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_checklist_item,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it,position)
        }
        holder.checklistItemName.text = itemList[position].categoryName

        if(!itemList[position].productImgUrl.isNullOrEmpty()){
            Glide.with(holder.itemView.context).load(itemList[position].productImgUrl).into(holder.itemImg)

        }
        else{
            holder.itemImg.setImageResource(R.drawable.alcohol)
        }
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val itemImg:ImageView = itemView.findViewById(R.id.itemIMG)
        val checklistItemName:TextView = itemView.findViewById(R.id.name_checklistItem)
//        val itemPriceEdit:EditText = itemView.findViewById(R.id.itemPRICEedit)
//        val itemNum :EditText = itemView.findViewById(R.id.itemNum)
    }




    interface OnItemClicklistener{
        fun onClick(v:View,position: Int)
    }
    fun setItemClickListener(onItemClicklistener: OnItemClicklistener){
        this.itemClickListener = onItemClicklistener
    }
    private lateinit var itemClickListener:OnItemClicklistener


}