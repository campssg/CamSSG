package com.example.graduationproject.User

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.CartListAdapter
import com.example.graduationproject.Api.Request.AddCartRequest
import com.example.graduationproject.Api.Response.AddCartResponse
import com.example.graduationproject.Api.Response.DeleteCartItemResponse
import com.example.graduationproject.databinding.Activity1cartlistBinding
import kotlinx.android.synthetic.main.activity_1cartlist.*
import kotlinx.android.synthetic.main.recyclerview_cart_list_item.view.*
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.http.GET

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
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


        val itemNum = binding.itemNum
        val totalprice = binding.cartlistTotalPrice



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
        val service2 = retrofit.create(DeleteCartItem::class.java)

        val sharedPreferences2 = getSharedPreferences("userInfo", MODE_PRIVATE)
        val userName = sharedPreferences2.getString("userName","")

        binding.textView38.setText(userName)


        // 리사이클러뷰 선택
        cartlistAdapter.setItemClickListener(object: CartListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Toast.makeText(view.context,
                    "${listItems_Cart[position].cartItemName}\n" +
                            "${listItems_Cart[position].cartItemPrice}",
                    Toast.LENGTH_SHORT).show()


                // x 누를시
                binding.rvCartListItem.cartlistitem_closeBtn.setOnClickListener {
                    service2.delete_item(listItems_Cart[position].cartItemId)
                        .enqueue(object : Callback<DeleteCartItemResponse> {
                            override fun onResponse(
                                call: Call<DeleteCartItemResponse>,
                                response: Response<DeleteCartItemResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val result = response.body()
                                    Log.e("성공", "${result}")
                                    startActivity(Intent(this@UserCartActivity, UserCartActivity::class.java))
                                } else {
                                    Log.d("장바구니 삭제", "실패")
                                }
                            }
                            override fun onFailure(
                                call: Call<DeleteCartItemResponse>,
                                t: Throwable
                            ) {
                                Log.e("연결실패", t.message.toString())
                            }
                        })
                }

            }
        })



        // 사용자 장바구니 API 호출
        // API 호출
        service.get_userCart()
            .enqueue(object : Callback<UserCartInfoResponse> {
                override fun onResponse(
                    call: Call<UserCartInfoResponse>,
                    response: Response<UserCartInfoResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")
//                        Toast.makeText(
//                            this@UserCartActivity,
//                            "총 결과"+result?.data,
//                            Toast.LENGTH_SHORT
//                        ).show()
                        itemNum.setText(result?.data?.totalCount.toString())
                        totalprice.setText(result?.data?.totalPrice.toString())
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

    interface DeleteCartItem {
        @POST("cart/delete/{cartItemId}")
        fun delete_item(
            @Path("cartItemId") cartItemId: Int,
        ): Call<DeleteCartItemResponse>
    }


}

private fun <T> Call<T>.enqueue(callback: Callback<Cart>) {
}




