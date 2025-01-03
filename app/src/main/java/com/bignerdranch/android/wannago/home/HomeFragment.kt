package com.bignerdranch.android.wannago.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wannago.GoViewModel
import com.bignerdranch.android.WannaGo2.R
import com.bignerdranch.android.WannaGo2.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "binding cannot be created. Is view created?"
        }
    private val goVM: GoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.restaurantsButton.setOnClickListener {
            findNavController().navigate(R.id.restaurantsFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}