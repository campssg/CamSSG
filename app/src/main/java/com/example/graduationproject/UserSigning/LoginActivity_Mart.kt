package com.example.graduationproject.UserSigning

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.graduationproject.databinding.ActivityLoginBinding

class LoginActivity_Mart : Fragment() {
    private var binding1: ActivityLoginBinding? = null
    private val binding get()= binding1!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding1 = ActivityLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)



        binding.LoginButton.setOnClickListener{

        }
    }

}