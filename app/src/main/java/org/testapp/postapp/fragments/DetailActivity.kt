package org.testapp.postapp.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.testapp.postapp.R
import org.testapp.postapp.data.State
import org.testapp.postapp.data.hasConnection
import org.testapp.postapp.databinding.ActivityDetailBinding
import org.testapp.postapp.viewmodel.MainViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val mViewModel: MainViewModel by viewModel()
    private var count = 0
    private val mListId = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPostsID()

        binding.btnNext.setOnClickListener {
            if (hasConnection(this)){
                count++
                if (mListId.size > count) {
                    mViewModel.getChosenPost(mListId[count])
                } else {
                    count = 0
                    mViewModel.getChosenPost(mListId[count])
                }
            }
        }

        binding.btnPrev.setOnClickListener {
            if (hasConnection(this)){
                count -= 1
                if (count != -1) {
                    mViewModel.getChosenPost(mListId[count])
                } else {
                    count = 0
                }
            }
        }
    }

    private fun initPosts() {
        mViewModel.post.observe(this, { state ->
            when (state) {
                is State.Loading -> {

                }
                is State.Success -> {
                    state.data.let {
                        if (it != null) {
                            if (!it.payload.text.isNullOrEmpty()) {
                                replace(DetailTextFragment(), it.payload.text)
                            }
                            if (!it.payload.url.isNullOrEmpty()) {
                                replace(DetailWebFragment(), it.payload.url)
                            }
                        } else {
                            mViewModel.getChosenPost(mListId[0])
                        }
                    }
                }
            }
        })
    }


    private fun initPostsID() {

        mViewModel.id.observe(this, { state ->
            when (state) {
                is State.Success -> {
                    state.data.let {
                        if (it != null) {
                            mListId.addAll(it)
                            mViewModel.getChosenPost(mListId[0])
                        }
                    }
                }
            }
        })
        initPosts()
    }


    private fun replace(fragment: Fragment, str: String) {
        val bundle = Bundle()
        bundle.putSerializable("data", str)
        fragment.arguments = bundle
        supportFragmentManager.commit {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            setReorderingAllowed(true)
            replace(R.id.container, fragment)

        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getAllPosts()
    }

}