package com.example.mypokedex.screen.home

import androidx.fragment.app.viewModels
import com.example.mypokedex.R
import com.example.mypokedex.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment() : BaseFragment(R.layout.fragment_home) {

    override val model: HomeViewModel by viewModels()
}