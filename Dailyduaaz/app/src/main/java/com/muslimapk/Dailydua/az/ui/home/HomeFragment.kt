package com.muslimapk.Dailydua.az.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.muslimapk.Dailydua.az.databinding.FragmentHomeBinding
import com.muslimapk.Dailydua.az.ui.Adapters.HomeAdapter

class HomeFragment() : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        homeViewModel.text.observe(viewLifecycleOwner) {

        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRc()
    }

    private fun initRc() {
        adapter = HomeAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter

        val fakeList = listOf("HEllo","Q324234")



        val list = mutableListOf<Any>()
        list.add(fakeList)
        adapter.addData(list)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}