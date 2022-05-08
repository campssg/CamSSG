package com.example.graduationproject.User.Payment_hj

import androidx.core.content.ContextCompat
import com.example.graduationproject.R
import com.example.graduationproject.User.Payment_hj.out.PaymentResultData
import com.example.graduationproject.databinding.ResultfragmentBinding
import com.google.gson.GsonBuilder

import com.iamport.sdk.data.sdk.IamPortResponse

class PaymentResultFragment : BaseFragment<ResultfragmentBinding>() {

    override val layoutResourceId: Int = R.layout.resultfragment

    override fun initStart() {
        super.onStart()
        val impResponse = PaymentResultData.result
        val resultText = if (isSuccess(impResponse)) "결제성공" else "결제실패"
        val color = if (isSuccess(impResponse)) R.color.main_purple_light else R.color.main_purple_light

        val tv = viewDataBinding.resultMessage

        tv.setTextColor(ContextCompat.getColor(requireContext(), color))
        tv.text = "$resultText\n${GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(impResponse)}"
    }

    private fun isSuccess(iamPortResponse: IamPortResponse?): Boolean {
        if (iamPortResponse == null) {
            return false
        }
        return iamPortResponse.success == true || iamPortResponse.imp_success == true
    }
}