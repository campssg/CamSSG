package com.example.graduationproject.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.Api.Response.CheckListResponse
import com.example.graduationproject.R

class ChecklistAdapter(val itemList: ArrayList<CheckListResponse>):RecyclerView.Adapter<ChecklistAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ChecklistAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_checklist_item,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size

    }


    inner class Holder(itemView: View,itemClick:(CheckListResponse) -> Unit):RecyclerView.ViewHolder(itemView){



    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {




        holder.checkbox.setOnCheckedChangeListener(null)
        if(holder.checkbox.isChecked==true){
            Log.e("출력하세요","${holder.checklistItemName}")
        }

        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it,position)
        }
      holder.itemPriceEdit.text = itemList[position].productPrice.toString()

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
        val itemPriceEdit:TextView = itemView.findViewById(R.id.itemPRICEedit)
//        val itemNum :TextView = itemView.findViewById(R.id.itemNum)

        val checkbox:CheckBox = itemView.findViewById(R.id.checklist_checkbox)

    }




    interface OnItemClicklistener{
        fun onClick(v:View,position: Int)
    }
    fun setItemClickListener(onItemClicklistener: OnItemClicklistener){
        this.itemClickListener = onItemClicklistener
    }
    private lateinit var itemClickListener:OnItemClicklistener


}