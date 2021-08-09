package com.example.mypokedex.screen.trainerprofile

import androidx.fragment.app.viewModels
import com.example.mypokedex.R
import com.example.mypokedex.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainerProfileFragment : BaseFragment(R.layout.fragment_trainer_profile) {

    override val model: TrainerProfileViewModel by viewModels()

}
