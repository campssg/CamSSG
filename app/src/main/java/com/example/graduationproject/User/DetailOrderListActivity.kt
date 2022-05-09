package com.example.graduationproject.User

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.DetailOrderListAdapter
import com.example.graduationproject.Adapter.RequestProductAdapter
import com.example.graduationproject.Adapter.RequestedDetailAdapter
import com.example.graduationproject.Api.Response.ProductList
import com.example.graduationproject.Api.Response.RequestedProductList
import com.example.graduationproject.Api.Response.UserDetailOrderListResponse
import com.example.graduationproject.Api.Response.orderlist
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1detailOrderListBinding
import kotlinx.android.synthetic.main.activity_1detail_order_list.*
import kotlinx.android.synthetic.main.recyclerview_order_item_mart.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit


//주문 상세내역 조회


//1. 스피너 등록(전체 주문, 요청했던 상품)
//2. QR발급 버튼 누르면 페이지 이동
//3. recyclerview 등록 (itemclicklistener )
//4. retrofit 작성 (recyclerview 에 item 을 출력하기, 총 주문금액 출력하기)
//5. 스피너에 따라 recyclerview 내용 바뀌기-selectMartItem의 스피너 참고하기




class DetailOrderListActivity : AppCompatActivity() {

    private lateinit var binding: Activity1detailOrderListBinding

    val listItems = arrayListOf<orderlist>()
    val detailOrderListAdapter = DetailOrderListAdapter(listItems)

    val listItems2 = arrayListOf<RequestedProductList>()
    val requestedDetailAdapter = RequestedDetailAdapter(listItems2)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1detailOrderListBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //액션바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()


        val UserName = binding.tvUsername
        val martName = binding.tvMartName
        val extraFee = binding.extraFee

        val orderState = binding.tvOrderstate
        val TotalPrice = binding.tvTotalprice
        val OrderId = binding.tvOrdernumber
        val PickUpDate = binding.tvPickupdate
        val PickUpTime = binding.tvPickuptime

        binding.rvOrderItemList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvOrderItemList.adapter = detailOrderListAdapter


        val orderId = intent.getLongExtra("orderId", 0)

        binding.rvRequestedItemList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvRequestedItemList.adapter = requestedDetailAdapter

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

        val service = retrofit.create(DetailOrderList::class.java)

        binding.MoveToPay.setOnClickListener {
            val outintent = Intent(this, HJ_UserPaymentActivity::class.java)
            outintent.putExtra("orderId", orderId)
            outintent.putExtra("phoneNumber", "010-1234-1234")
            startActivity(outintent)
        }

        //주문 상세 내역 조회

        service.ShowDetailOrderList(orderId)
            .enqueue(object:Callback<UserDetailOrderListResponse>{

                override fun onResponse(
                    call: Call<UserDetailOrderListResponse>,
                    response: retrofit2.Response<UserDetailOrderListResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")

                        martName.setText("가게명 : " +result!!.martName)
                        UserName.setText("사용자명 : " +result!!.userName)
                        orderState.setText("주문상태 : " +result!!.orderState)
                        TotalPrice.setText(result!!.totalPrice.toString())
                        extraFee.setText(result!!.charge.toString())
                        PickUpDate.setText("픽업날짜 : " +result!!.pickup_day)
                        PickUpTime.setText("픽업시간 : " +result!!.pickup_time)
                        OrderId.setText("주문번호 : " +result!!.orderId.toString())

                        AddItemToList(result)

                        if(result.orderState=="결제대기중"){
                            binding.MoveToPay.isVisible = true
                        }

                        if(!result.requestedProductList.isNullOrEmpty()) {
                            binding.requestedProduct.isVisible = true
                            AddItemToList2(result.requestedProductList)
                        }
                    } else {
                        Log.d("조회", "실패")
                    }
                }
                override fun onFailure(call: Call<UserDetailOrderListResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())

                }
            })

        //주문 QR이미지 발급 페이지로 이동
        binding.OrderQRBtn.setOnClickListener{
            val intent = Intent(this,OrderQRImageActivity::class.java)
            intent.putExtra("orderId",orderId)

            startActivity(intent)

        }
    }

    // 조회 결과 리사이클러뷰에 추가
    private fun AddItemToList(searchResult: UserDetailOrderListResponse?) {
        listItems.clear()
        for (orderList in searchResult!!.orderItemList) {
            listItems.add(orderList)
        }
        detailOrderListAdapter.notifyDataSetChanged()
    }

    private fun AddItemToList2(searchResult: List<RequestedProductList>) {
        listItems2.clear()
        for (requestedList in searchResult) {
            listItems2.add(requestedList)
        }
        requestedDetailAdapter.notifyDataSetChanged()
    }
}


interface DetailOrderList{
    @GET("/order/{orderId}")
    fun ShowDetailOrderList(
        @Path("orderId") orderId:Long
    ): Call<UserDetailOrderListResponse>
}