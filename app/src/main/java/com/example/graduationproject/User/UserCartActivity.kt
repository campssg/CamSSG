package com.example.graduationproject.User

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.CartListAdapter
import com.example.graduationproject.databinding.Activity1cartlistBinding
import kotlinx.android.synthetic.main.activity_1cartlist.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class UserCartActivity : AppCompatActivity() {
    private lateinit var binding: Activity1cartlistBinding

     val listItems_Cart = arrayListOf<CartItem>()
     val cartlistAdapter = CartListAdapter(listItems_Cart)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1cartlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvCartListItem.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.rvCartListItem.adapter = cartlistAdapter

        //타이틀 숨기기

        var cartName = "1"
        var cartPrice = "1"
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()




        cartlistAdapter.setItemClickListener(object: CartListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Toast.makeText(view.context,
                "${listItems_Cart[position].cartItemName}\n" +
                        "${listItems_Cart[position].cartItemPrice}",
                Toast.LENGTH_SHORT).show()

            }
        })






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

        val sharedPreferences2 = getSharedPreferences("userInfo", MODE_PRIVATE)
        val userName = sharedPreferences2.getString("userName","")

        binding.textView38.setText(userName)

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
//                        Toast.makeText(
//                            this@UserCartActivity,
//                            "총 결과"+result?.data,
//                            Toast.LENGTH_SHORT
//                        ).show()
                        if (result != null) {
                            AddItemToList_cart(result)
                        }

                    } else {
                        Log.d("사용자 카트 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<UserCartInfoResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })
//        val Cartlist = arrayListOf<Cart>()
//        binding.rvCartListItem.layoutManager = LinearLayoutManager(this)
////        binding.rvCartListItem.setHasFixedSize(true)
//
//        //어댑터 설정
//        binding.rvCartListItem.adapter = CartlistAdapter
    }



    private fun AddItemToList_cart(cartlistResult: UserCartInfoResponse?) {
        for(cartlist in cartlistResult!!.data.cartItemList){
            listItems_Cart.add(cartlist)
        }

        cartlistAdapter.notifyDataSetChanged()

    }





    interface UserCart {
        @GET("cart/info")
        fun get_userCart(): Call<UserCartInfoResponse>

    }



}

private fun <T> Call<T>.enqueue(callback: Callback<Cart>) {
}




