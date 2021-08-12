package com.example.mypokedex.screen.home

import android.view.View
import androidx.fragment.app.viewModels
import com.example.mypokedex.R
import com.example.mypokedex.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment() : BaseFragment() {

    override val model: HomeViewModel by viewModels()
    override val layoutId: Int = R.layout.fragment_home

    override fun initialiseViews(view: View) {
        //
    }
}