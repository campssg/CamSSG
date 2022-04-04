package com.example.graduationproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.User.Cart
import com.example.graduationproject.User.CartItem
import com.example.graduationproject.databinding.RecyclerviewCartListItemBinding
import java.lang.Exception

class CartlistAdapter(Cartlist: ArrayList<CartItem>) : RecyclerView.Adapter<CartlistAdapter.ViewHolder>() {
    val datas: ArrayList<CartItem> = ArrayList()
    var clickEvent: (CartItem) -> Unit = {}


    fun setDatas(arrayList: List<CartItem>) {
        datas.clear()
        datas.addAll(arrayList)
        notifyItemRangeInserted(0, datas.size)
    }

    // 데이터 add
    fun addData(data: CartItem) {
        datas.add(data)

        notifyItemInserted(datas.size - 1) // 마지막에 데이터 넣기
    }

    fun removeData(data: CartItem) {
        var position = -1
        for ((i, d) in datas.withIndex()) {
            if (d.cartItemId == data.cartItemId) {
                position = i
                break
            }
        }

        try {
            datas.removeAt(position)
            notifyItemRemoved(position)
        } catch (e: Exception) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //뷰홀더 리턴해야함
        var binding = RecyclerviewCartListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    inner class ViewHolder(val binding: RecyclerviewCartListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CartItem) {
            binding.root.setOnClickListener {
                clickEvent(data)
            }


            //데이터 부어주기

            //상품명
            binding.cartlistItemName.text = data.cartItemName
            //상품가격
            binding.cartlistItemPrice.text = data.cartItemPrice.toString()


        }
    }
}