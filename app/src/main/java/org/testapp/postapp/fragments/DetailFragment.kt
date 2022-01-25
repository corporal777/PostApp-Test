package org.testapp.postapp.fragments

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.testapp.postapp.data.State
import org.testapp.postapp.data.hasConnection
import org.testapp.postapp.databinding.FragmentDetailTextBinding
import org.testapp.postapp.viewmodel.MainViewModel

class DetailFragment : Fragment() {

    private var mId = 0
    private val postsIdList = ArrayList<Int>()
    private var _binding: FragmentDetailTextBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: MainViewModel by viewModel()


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
        val list = arguments?.getIntegerArrayList("list")
        val id = arguments?.getInt("id")
        if (id != null) {
            mId = id
        }
        if (!list.isNullOrEmpty()) {
            postsIdList.addAll(list)
        }
        var index = postsIdList.indexOf(mId)

        binding.btnNext.setOnClickListener {
            try {
                index++
                mId = postsIdList[index]
//                Toast.makeText(requireContext(), index.toString(), Toast.LENGTH_SHORT).show()
                mViewModel.getChosenPost(mId)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Больше нет!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPrev.setOnClickListener {
            try {
                index -= 1
                mId = postsIdList[index]
//                Toast.makeText(requireContext(), index.toString(), Toast.LENGTH_SHORT).show()
                mViewModel.getChosenPost(mId)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Больше нет!", Toast.LENGTH_SHORT).show()
            }

        }


        initData()
    }

    private fun initData() {
        mViewModel.post.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                    binding.webView.isVisible = false
                    binding.tvPost.isVisible = false
                    binding.progressbar.isVisible = true
                }
                is State.Success -> {
                    state.data.let {
                        if (it != null) {
                            binding.progressbar.isVisible = false
                            if (!it.payload.text.isNullOrEmpty()) {
                                binding.webView.isVisible = false
                                binding.tvPost.isVisible = true
                                binding.tvPost.text = it.payload.text
                            }
                            if (!it.payload.url.isNullOrEmpty()) {
                                binding.tvPost.isVisible = false
                                binding.webView.isVisible = true
                                showWebPage(it.payload.url)
                            }
                        }
                    }
                }
            }
        })
    }


    private fun showWebPage(str: String) {
        binding.webView.settings.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
            domStorageEnabled = true
            javaScriptEnabled = true
        }
        with(binding.webView) {
            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    if (!url.isNullOrEmpty()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            CookieManager.getInstance().flush()
                        }
                    }
                }
            }

            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (canGoBack()) goBack()
                    else requireActivity().onBackPressed()
                    return@setOnKeyListener true
                }
                false
            }
        }
        if (!str.isNullOrEmpty()) {
            binding.webView.loadUrl(str)
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasConnection(requireContext())) {
            mViewModel.getChosenPost(mId)
        } else {
            binding.progressbar.isVisible = true
            Toast.makeText(requireContext(), "Нет Интернет Соединения!", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}