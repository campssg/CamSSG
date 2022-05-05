package com.example.graduationproject.User

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.DetailOrderListAdapter
import com.example.graduationproject.Api.Response.UserDetailOrderListResponse
import com.example.graduationproject.Api.Response.orderlist
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1detailOrderListBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.ArrayList
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =Activity1detailOrderListBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.rvDetailorderlist.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.rvDetailorderlist.adapter = detailOrderListAdapter



        val orderId = intent.getLongExtra("orderId",0)

        binding.rvDetailorderlist.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDetailorderlist.adapter = detailOrderListAdapter


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


        val service = retrofit.create(DetailOrderList::class.java)






        service.ShowDetailOrderList(orderId)
            .enqueue(object:Callback<UserDetailOrderListResponse>{



                override fun onResponse(
                    call: Call<UserDetailOrderListResponse>,
                    response: Response<UserDetailOrderListResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")
//                        AddItemToList(result)
                    } else {
                        Log.d("조회", "실패")
                    }
                }
                override fun onFailure(call: Call<UserDetailOrderListResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())

                }
            })






        //스피너
        val spinner = binding.OrderedTypeSpinner
        spinner.adapter = ArrayAdapter.createFromResource(this, R.array.item_ordered_type,android.R.layout.simple_spinner_item)



        //주문 QR이미지 발급 페이지로 이동
        binding.OrderQRBtn.setOnClickListener{
            val intent = Intent(this,OrderQRImageActivity::class.java)
            startActivity(intent)

        }

    }

    // 조회 결과 리사이클러뷰에 추가
//    private fun AddItemToList(searchResult:UserDetailOrderListResponse) {
//        listItems.clear()
//        for (orderList in searchResult!!) {
//            listItems.add(orderList)
//        }
//        DetailOrderListAdapter.notifyDataSetChanged()
//    }
}

private fun <E> ArrayList<E>.add(element: UserDetailOrderListResponse) {

}

private fun <T> Call<T>.enqueue(callback: Callback<List<UserDetailOrderListResponse>>) {

}


interface DetailOrderList{
    @GET("/order/{orderId}")
    fun ShowDetailOrderList(
        @Path("orderId") orderId:Long
    ): Call<UserDetailOrderListResponse>
}