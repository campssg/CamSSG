package com.example.graduationproject.User

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.ActivityHjUserPaymentBinding
import kr.co.bootpay.Bootpay
import kr.co.bootpay.BootpayAnalytics
import kr.co.bootpay.enums.Method
import kr.co.bootpay.enums.PG
import kr.co.bootpay.enums.UX
import kr.co.bootpay.model.BootExtra
import kr.co.bootpay.model.BootUser

class HJ_UserPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHjUserPaymentBinding

    val application_id = "6277defe270180001ef6a3fb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHjUserPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BootpayAnalytics.init(this, application_id)

        val orderId = intent.getLongExtra("orderId", 0).toString()
        val phoneNumber = intent.getStringExtra("phoneNumber").toString()

        binding.buttonFirst.setOnClickListener {
            goBootpayRequest(orderId, phoneNumber)
        }

    }

    fun goBootpayRequest(orderId:String, phoneNumber:String) {
        val bootUser = BootUser().setPhone(phoneNumber)
        val bootExtra = BootExtra().setQuotas(intArrayOf(0, 2, 3))

        val stuck = 1 //재고 있음

        Bootpay.init(this)
            .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
            .setContext(this)
            .setBootUser(bootUser)
            .setBootExtra(bootExtra)
            .setUX(UX.PG_DIALOG)
            .setPG(PG.KCP)
            .setMethod(Method.KAKAO)
            .setName("캠쓱 픽업 상품") // 결제할 상품명
            .setOrderId(orderId) // 결제 고유번호
            .setPrice(100) // 결제할 금액
            .onConfirm { message ->
                if (0 < stuck) Bootpay.confirm(message); // 재고 있음
                else Bootpay.removePaymentWindow(); // 재고 없음 -> 결제창 중간에 캔슬
                Log.d("confirm", message);
            }
            .onDone { message ->
                Log.d("done", message)
            }
            .onReady { message ->
                Log.d("ready", message)
            }
            .onCancel { message ->
                Log.d("cancel", message)
            }
            .onError{ message ->
                Log.d("error", message)
            }
            .onClose { message ->
                Log.d("close", "close")
            }
            .request();
    }
}