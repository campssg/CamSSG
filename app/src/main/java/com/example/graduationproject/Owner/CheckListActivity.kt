package com.example.graduationproject.Owner

import android.content.ClipData
import android.content.DialogInterface
import android.content.Intent
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
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.ChecklistAdapter
import com.example.graduationproject.Api.Response.CategoryCheckListResponse
import com.example.graduationproject.Api.Response.CheckListResponse
import com.example.graduationproject.Api.Response.ResultResponse
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.User.ChecklistCategoryActivity
import com.example.graduationproject.databinding.ActivityAddItemAllBinding
import com.example.graduationproject.databinding.ActivityChecklistBinding
import kotlinx.android.synthetic.main.recyclerview_checklist_item.view.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.ArrayList
import java.util.concurrent.TimeUnit


//hj 물품 체크리스트 등록
class CheckListActivity:AppCompatActivity() {
    private lateinit var binding: ActivityChecklistBinding

    val listItems = arrayListOf<CheckListResponse>()
    val ItemAdapter = ChecklistAdapter(listItems)

    val checkedItems = arrayListOf<CheckListResponse>()

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
        val service2= retrofit.create(martChecklist::class.java)
        //카테고리 아이디 intent로 넘겨오기
        val categoryId = intent.getStringExtra("categoryId")
        //mart아이디 intent 로 넘겨오기
        val martId = intent.getLongExtra("martId",0)
        Toast.makeText(this,categoryId,Toast.LENGTH_SHORT).show()

        ItemAdapter.setItemClickListener(object : ChecklistAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val priceFirst = listItems[position].productPrice.toInt()
                // 가격 입력 받을 EditText 생성
                val priceEdit = EditText(this@CheckListActivity)
                priceEdit.gravity = Gravity.CENTER
                priceEdit.inputType = InputType.TYPE_CLASS_NUMBER
                priceEdit.hint = priceFirst.toString()
                AlertDialog.Builder(this@CheckListActivity)
                    .setTitle("상품 정보 등록")
                    .setMessage("가격을 입력하세요")
                    .setView(priceEdit)
                    .setPositiveButton("추가", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            val price = priceEdit.text.toString()
                            listItems[position].productPrice = price.toInt()
                            listItems[position].productStock = 0
                            checkedItems.add(listItems[position])
                            ItemAdapter.notifyItemChanged(position)
                        }
                    })
                    .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                        }
                    })
                    .create()
                    .show()
            }
        })

        binding.btnRegisterItem.setOnClickListener {
            service2.MartCheckItem(martId, checkedItems)
                .enqueue(object : Callback<ResultResponse> {
                    override fun onResponse(
                        call: Call<ResultResponse>,
                        response: Response<ResultResponse>
                    ) {
                        println(response)
                        println(checkedItems)
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.e("등록 완료", "${result}")

                            AlertDialog.Builder(this@CheckListActivity)
                                .setTitle("물품 등록 완료")
                                .setMessage("물품이 마트에 성공적으로 등록되었습니다.")
                                .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        startActivity(Intent(this@CheckListActivity, ChecklistCategoryActivity::class.java))
                                        finish()
                                    }
                                })
                                .create()
                                .show()
                        } else {
                            Log.d("등록", "실패")
                        }
                    }
                    override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                        Log.e("연결실패", t.message.toString())
                    }
                })
        }

        if (categoryId != null) {
            service.Checklist(categoryId.toLong())
                .enqueue(object : Callback<List<CheckListResponse>> {

                    override fun onResponse(
                        call: Call<List<CheckListResponse>>,
                        response: Response<List<CheckListResponse>>
                    ) {
                        println(response)
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.e("조회 완료", "${result}")
                            //리사이클러뷰에 결과 출력
                            AddItemToList(result)
                        } else {
                            Log.d("조회", "실패")
                        }
                    }

                    override fun onFailure(
                        call: Call<List<CheckListResponse>>,
                        t: Throwable
                    ) {
                        Log.e("연결실패", t.message.toString())

                    }


                })
        }
    }

    private fun AddItemToList(searchResult: List<CheckListResponse>?) {
        listItems.clear()
        if (searchResult != null) {
            for(productList in searchResult){
                listItems.add(productList)
            }
        }
        ItemAdapter.notifyDataSetChanged()
    }
}

interface ChecklistCategory{

    @GET("checklist/{categoryId}")
    fun Checklist(
        @Path("categoryId") categoryId:Long
    ): Call<List<CheckListResponse>>
}

//상품 일괄 등록
interface martChecklist{

    @POST("mart/{martId}/list")
    fun MartCheckItem(
        @Path("martId") martId:Long,
        @Body request: List<CheckListResponse>
    ): Call<ResultResponse>
}