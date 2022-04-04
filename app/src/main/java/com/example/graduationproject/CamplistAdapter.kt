package com.example.graduationproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.User.CampList
import com.example.graduationproject.User.CampSearchActivity
import com.example.graduationproject.databinding.RecyclerviewCamplistBinding
import retrofit2.http.GET

class CamplistAdapter : RecyclerView.Adapter<CamplistAdapter.ViewHolder>() {

    //    val datas:ArrayList<CampList> = ArrayList()
//    var clickEvent:(CampList) -> Unit = {}
    private var myList = emptyList<CampList>()

    class ViewHolder(val binding: RecyclerviewCamplistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //뷰홀더 리턴해야함
        var binding =
            RecyclerviewCamplistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //뷰홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvListName.text = myList[position].campName
        holder.binding.tvListTel.text = myList[position].tel
        holder.binding.tvListAddress.text = myList[position].address

    }

    override fun getItemCount(): Int {
        return myList.size
    }

    //데이터 변경시 리스트 다시 할당

    fun setData(newList: List<CampList>) {
        myList = newList
        notifyDataSetChanged()

    }


}




