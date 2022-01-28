package org.testapp.postapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.testapp.postapp.databinding.FragmentDetailTextBinding
import org.testapp.postapp.viewmodel.MainViewModel

class DetailTextFragment : Fragment() {

    private var mId = 0
    private val postsIdList = ArrayList<Int>()
    private var _binding: FragmentDetailTextBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: MainViewModel by viewModel()
    private val mListId = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailTextBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getSerializable("data") as String
        binding.tvPost.text = data

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}