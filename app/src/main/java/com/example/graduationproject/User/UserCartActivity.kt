package com.example.graduationproject.User

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.CartlistAdapter
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1cartlistBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class UserCartActivity : AppCompatActivity() {
    private lateinit var binding: Activity1cartlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1cartlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

//        setRV()
//        setData("전체")

        // 로그인 후 저장해둔 JWT 토큰 가져오기
        val sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
        val jwt = sharedPreferences.getString("jwt", "")

        val client = OkHttpClient.Builder()
            .addInterceptor(AddHeaderJWT(jwt.toString())) // JWT header 달아주는 interceptor 추가
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.13.202:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()

        val service = retrofit.create(UserCart::class.java)

        // 사용자 장바구니 API 호출
        // API 호출
        service.get_userCart()
            .enqueue(object : Callback<UserCartInfoResponse> {
                override fun onResponse(
                    call: Call<UserCartInfoResponse>,
                    response: retrofit2.Response<UserCartInfoResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")
                    } else {
                        Log.d("사용자 카트 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<UserCartInfoResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })
        val Cartlist = arrayListOf<CartItem>()
        binding.rvCartListItem.layoutManager = LinearLayoutManager(this)
        binding.rvCartListItem.setHasFixedSize(true)

        //어댑터 설정
        binding.rvCartListItem.adapter = CartlistAdapter(Cartlist)
    }





//     override fun setData(status:String) {
//        { UserCartActivity().list->
//            list?.let {
//                binding.rvCartListItem.adapter as CartlistAdapter.setDatas(list)
//            }
//        }
//    }
//
//    private fun setRV() {
//        binding.rvCartListItem.apply{
//            adapter = CartlistAdapter()
//                .apply {
//                    clickEvent = { data ->
////                        intent.putExtra("complainId",data.cartItemId)
//                        Log.e("cartItemId",data.cartItemId.toString())
////                        this@UserCartActivity.startActivity(intent)
//                    }
//                }
//        }
//    }

    interface UserCart {
        @GET("api/v1/cart/info")
        fun get_userCart(): Call<UserCartInfoResponse>
    }

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