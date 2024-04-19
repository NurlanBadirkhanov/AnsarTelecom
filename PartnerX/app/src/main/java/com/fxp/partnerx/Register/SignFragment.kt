package com.fxp.partnerx.Register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fxp.partnerx.R
import com.fxp.partnerx.databinding.FragmentSignBinding


class SignFragment : Fragment() {
    private lateinit var binding:FragmentSignBinding
    private lateinit var auth:Fire
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFirebase()
    }

    private fun initFirebase() {

    }


}