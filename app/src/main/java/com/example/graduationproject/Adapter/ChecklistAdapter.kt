package com.example.graduationproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.Api.Response.CategoryCheckListResponse
import com.example.graduationproject.R
import com.example.graduationproject.User.ChecklistCategory

class ChecklistAdapter(val itemList:ArrayList<CategoryCheckListResponse>):RecyclerView.Adapter<CategoryCheckListResponse.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ChecklistAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_checklist_item)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val itemImg:ImageView = itemView.findViewById(R.id.itemIMG)
        val checklistItemName:TextView = itemView.findViewById(R.id.name_checklistItem)
        val itemPriceEdit:EditText = itemView.findViewById(R.id.itemPRICEedit)
        val itemNum :EditText = itemView.findViewById(R.id.itemNum)


    }

    override fun onBindViewHolder(holder: ChecklistAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it,position)
        }
        holder.checklistItemName.text = itemList[position].categoryName
        holder.itemPriceEdit.editableText = itemList[position].productPrice

    }

    override fun getItemCount(): Int {
        return itemList.size

    }


}