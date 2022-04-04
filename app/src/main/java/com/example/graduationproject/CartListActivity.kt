package com.example.graduationproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.Activity1cartlistBinding
import com.example.graduationproject.databinding.RecyclerviewCartListItemBinding

//CartlistActivity-> UserCartActivity로 수정


class CartListActivity:AppCompatActivity() {
    private lateinit var binding:Activity1cartlistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1cartlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar;
        actionBar?.hide()



        @SuppressLint("ResourceAsColor")
        fun onCheckChanged(compoundButton: CompoundButton){
            when(compoundButton.id){
                R.id.cartlist_totalcheckbox->{
                    if(binding.cartlistTotalcheckbox.isChecked){
                    }
                }
            }
        }



    }
}