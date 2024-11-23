package com.exal.grocerease.view.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exal.grocerease.R
import com.exal.grocerease.viewmodel.PlanViewModel

class PlanFragment : Fragment() {

    companion object {
        fun newInstance() = PlanFragment()
    }

    private val viewModel: PlanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }
}