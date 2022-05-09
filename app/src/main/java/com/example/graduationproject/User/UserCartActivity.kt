package com.example.graduationproject.User

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.CartListAdapter
import com.example.graduationproject.databinding.Activity1cartlistBinding
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.http.GET

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

class UserCartActivity : AppCompatActivity() {
    private lateinit var binding: Activity1cartlistBinding

     val listItems_Cart = arrayListOf<CartItem>()
     val cartlistAdapter = CartListAdapter(listItems_Cart)
     var martId: Long? = null

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

        val sharedPreferences2 = getSharedPreferences("token", MODE_PRIVATE)
        val userName = sharedPreferences2.getString("userName","")

        binding.textView38.setText(userName)

        // 리사이클러뷰 클릭 이벤트
        cartlistAdapter.setItemClickListener(object: CartListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Toast.makeText(this@UserCartActivity, "길게 클릭하면 상품이 삭제됩니다", Toast.LENGTH_SHORT).show()
            }
        })

        // 롱클릭 이벤트
        cartlistAdapter.setItemLongClickListener(object : CartListAdapter.OnItemLongClickListener {
            override fun onLongClick(v: View, position: Int) {
                // 리사이클러뷰 롱클릭 -> 바로 삭제
                // 다이얼로그 띄우기
                AlertDialog.Builder(this@UserCartActivity)
                    .setTitle("장바구니 물품 삭제")
                    .setMessage("${listItems_Cart[position].cartItemName} ${listItems_Cart[position].cartItemCount}개를 장바구니에서 삭제하시겠습니까?")
                    .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            service2.delete_item(listItems_Cart[position].cartItemId)
                                .enqueue(object : Callback<UserCartInfoResponse> {
                                    override fun onResponse(
                                        call: Call<UserCartInfoResponse>,
                                        response: Response<UserCartInfoResponse>
                                    ) {
                                        if (response.isSuccessful) {
                                            val result = response.body()
                                            Log.e("성공", "${result}")

                                            // 장바구니 갱신
                                            itemNum.setText(result?.data?.totalCount.toString())
                                            totalprice.setText(result?.data?.totalPrice.toString())
                                            listItems_Cart.removeAt(position) // 리사이클러뷰에서도 삭제
                                            cartlistAdapter.notifyDataSetChanged() // 리사이클러뷰 갱신
                                        } else {
                                            Log.d("장바구니 삭제", "실패")
                                        }
                                    }
                                    override fun onFailure(
                                        call: Call<UserCartInfoResponse>,
                                        t: Throwable
                                    ) {
                                        Log.e("연결실패", t.message.toString())
                                    }
                                })
                        }
                    })
                    .create()
                    .show()
            }
        })

        // 주문하기 버튼
        binding.orderCartlistBtn.setOnClickListener() {
            if (totalprice.equals(0)) {
                Toast.makeText(this, "장바구니가 비어있습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PickupTimeActivity::class.java)
                intent.putExtra("martId", martId)
                startActivity(intent)
            }
        }

        // 사용자 장바구니 API 호출
        service.get_userCart()
            .enqueue(object : Callback<UserCartInfoResponse> {
                override fun onResponse(
                    call: Call<UserCartInfoResponse>,
                    response: Response<UserCartInfoResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")

                        martId = result?.data?.martId
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
        ): Call<UserCartInfoResponse>
    }
}

private fun <T> Call<T>.enqueue(callback: Callback<Cart>) {
}




