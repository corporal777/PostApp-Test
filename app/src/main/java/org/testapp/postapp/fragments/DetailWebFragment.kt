package org.testapp.postapp.fragments

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.testapp.postapp.databinding.FragmentDetailWebBinding
import org.testapp.postapp.viewmodel.MainViewModel

class DetailWebFragment : Fragment() {

    private var _binding: FragmentDetailWebBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getSerializable("url") as String

        showWebPage(url)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}