package com.example.mypokedex.screen.trainerprofile

import android.view.View
import androidx.fragment.app.viewModels
import com.example.mypokedex.R
import com.example.mypokedex.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainerProfileFragment : BaseFragment() {

    override val model: TrainerProfileViewModel by viewModels()
    override val layoutId: Int = R.layout.fragment_trainer_profile

    override fun initialiseViews(view: View) {
        //
    }
}
