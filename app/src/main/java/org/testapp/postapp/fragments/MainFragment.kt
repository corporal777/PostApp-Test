package org.testapp.postapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.testapp.postapp.adapter.PostsAdapter
import org.testapp.postapp.data.State
import org.testapp.postapp.data.hasConnection
import org.testapp.postapp.data.replaceFragment
import org.testapp.postapp.databinding.FragmentMainBinding
import org.testapp.postapp.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: MainViewModel by viewModel()
    private lateinit var mAdapter: PostsAdapter
    private val postsList = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initData()
    }


    private fun initRecyclerView() {
        mAdapter = PostsAdapter()
        binding.rvPostsList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPostsList.setHasFixedSize(true)

    }

    private fun initData() {
        val itemClickListener: PostsAdapter.OnItemClickListener =
            object : PostsAdapter.OnItemClickListener {
                override fun onItemClick(id: Int, list: List<Int>) {
                    val bundle = Bundle()
                    bundle.putIntegerArrayList("list", postsList)
                    bundle.putInt("id", id)
                    val fragment = DetailFragment()
                    fragment.arguments = bundle
                    replaceFragment(fragment)
                }
            }
        mViewModel.id.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Success -> {
                    state.data.let {
                        if (it != null) {
                            binding.progressbar.isVisible = false
                            postsList.addAll(it)
                            mAdapter.addPosts(it, itemClickListener)
                            binding.rvPostsList.adapter = mAdapter
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (hasConnection(requireContext())) {
            mViewModel.getAllPosts()
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