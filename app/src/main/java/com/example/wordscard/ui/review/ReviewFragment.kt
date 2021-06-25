package com.example.wordscard.ui.review

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wordscard.R
import com.example.wordscard.databinding.FragmentReviewBinding

class ReviewFragment: Fragment() {
    private val reviewViewModel: ReviewViewModel by viewModels()

    private lateinit var binding: FragmentReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reviewViewModel = reviewViewModel

        //Fragment與Fragment中的View的生命週期並不一致，
        // 需要讓observer感知Fragment中的View的生命週期而非Fragment，
        // 因此Android專門構造了Fragment中的View的LifecycleOwner，即viewLifecycleOwner
        binding.lifecycleOwner = viewLifecycleOwner

        //set scrollable text view for word detail
        binding.scrollText.movementMethod = ScrollingMovementMethod()


        initial()

        binding.clickText.setOnClickListener {
            reviewViewModel.setClick(true)
        }
        binding.forgetButton.setOnClickListener {
            reviewViewModel.switchWord(false)
        }
        binding.knowButton.setOnClickListener {
            reviewViewModel.switchWord(true)
        }
    }

    fun initial() = reviewViewModel.initial()
}