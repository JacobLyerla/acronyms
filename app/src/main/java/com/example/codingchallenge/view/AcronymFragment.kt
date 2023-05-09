package com.example.codingchallenge.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.acronym.data.util.AcronymResult
import com.example.codingchallenge.databinding.FragmentAcronymBinding
import com.example.codingchallenge.viewmodel.AcronymViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AcronymFragment : Fragment() {

    private val acronymViewModel by viewModels<AcronymViewModel>()
    private lateinit var binding: FragmentAcronymBinding
    private val longformAdapter by lazy { LongformAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAcronymBinding.inflate(inflater, container, false).apply {
            acronymViewModel = this@AcronymFragment.acronymViewModel
            adapter = longformAdapter
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        acronymViewModel.state.observe(viewLifecycleOwner) { state ->
            with(binding) {
                mainProgress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                mainTextCount.visibility = if (state.showResultText) View.VISIBLE else View.GONE

                if (state.errorMessage != null) {
                    Snackbar.make(view, state.errorMessage, Snackbar.LENGTH_LONG)
                        .setTextColor(Color.RED)
                        .setBackgroundTint(Color.WHITE)
                        .setText(state.errorMessage)
                        .show()
                }
            }

            if (state.response is AcronymResult.Success) {
                val longforms = state.response.data.map { it.lf }
                longformAdapter.submitList(longforms)
            }
        }
    }

}
