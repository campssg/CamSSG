package com.example.graduationproject.Owner

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.ChecklistAdapter
import com.example.graduationproject.Api.Request.ChecklistRequest
import com.example.graduationproject.Api.Response.CategoryCheckListResponse
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.databinding.ActivityChecklistBinding
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

class ChecklistActivity:AppCompatActivity() {
    private lateinit var binding: ActivityChecklistBinding

    val listItems = arrayListOf<CategoryCheckListResponse>()
    val ItemAdapter = ChecklistAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityChecklistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val TAG:String = "ChecklistActivity"
        Log.e(TAG,"Log Start :         ")


        binding.checklistRecycelerView.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.checklistRecycelerView.adapter = ItemAdapter


        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

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


        val service = retrofit.create(ChecklistCategory::class.java)
        //카테고리 아이디 intent로 넘겨오기
        val categoryId = intent.getStringExtra("categoryId")
        Toast.makeText(this,categoryId,Toast.LENGTH_SHORT).show()





        if (categoryId != null) {
            service.Checklist(categoryId.toLong())
                .enqueue(object : Callback<List<CategoryCheckListResponse>> {

                    override fun onResponse(
                        call: Call<List<CategoryCheckListResponse>>,
                        response: Response<List<CategoryCheckListResponse>>
                    ) {
                        println(response)
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.e("조회 완료", "${result}")
                            //리사이클러뷰에 결과 출력
                            AddItemToList(result)
                        }
                    }

                    override fun onFailure(
                        call: Call<List<CategoryCheckListResponse>>,
                        t: Throwable
                    ) {
                        Log.e("연결실패", t.message.toString())

                    }


                })
        }



        //상품 등록
        ItemAdapter.setItemClickListener(object:ChecklistAdapter.OnItemClicklistener{
            override fun onClick(v: View, position: Int) {
                val editText = EditText(this@ChecklistActivity)
//                editText = Gravity.CENTER
//                editText = InputType.TYPE_CLASS_NUMBER
//                editText.hint= "수량 입력"


//                AlertDialog.Builder(this@ChecklistActivity)
//                    .setTitle("물품 등록")
//                    .setMessage("등록하실 수량을 입력해주세요")
//                    .setView(editText)
//                    .setPositiveButton("추가",object:DialogInterface.OnClickListener{
//                        override fun onClick(p0: DialogInterface?, p1: Int) {
//                            val count = editText.text.toString()
//                            val data = ChecklistRequest(count.toInt())
//
//                        }
//                    })
            }
        })

    }

    //이게 리스트로 해야되는 건가?
    private fun AddItemToList(searchResult: List<CategoryCheckListResponse>?) {
        listItems.clear()
        if (searchResult != null) {
            for(productList in searchResult){
                listItems.add(productList)
            }
        }
        ItemAdapter.notifyDataSetChanged()
    }




}

private fun <E> ArrayList<E>.add(productList: Char) {

}


interface ChecklistCategory{

    @GET("checklist/{categoryId}")
    fun Checklist(
        @Path("categoryId") categoryId:Long
    ): Call<List<CategoryCheckListResponse>>
}