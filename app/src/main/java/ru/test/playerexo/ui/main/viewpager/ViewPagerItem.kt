package ru.test.playerexo.ui.main.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.test.playerexo.R
import ru.test.playerexo.databinding.FragmentViewPagerItemBinding
import ru.test.playerexo.model.ui.ChannelUI
import ru.test.playerexo.ui.main.adapter.OnRecycleClickListener
import ru.test.playerexo.ui.main.adapter.ChannelsAdapter
import ru.test.playerexo.ui.player.PlayerFragment
import ru.test.playerexo.viewmodel.MainViewModel
import ru.test.playerexo.viewmodel.MainViewModelFactory

class ViewPagerItem : Fragment() {

    private var _binding: FragmentViewPagerItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ChannelsAdapter
    private lateinit var args: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragment?.let {
            viewModel = ViewModelProvider(
                it, MainViewModelFactory(requireActivity().application)
            )[MainViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = setAdapter()
        binding.recycler.adapter = adapter
        setViewModel()
    }

    private fun refreshAdapter(it: List<ChannelUI>?) {
        adapter.submitList(it)
        adapter.notifyDataSetChanged()
    }

    private val fullObserver = Observer<List<ChannelUI>> { refreshAdapter(it) }

    private val finderObserver = Observer<List<ChannelUI>?> {
        when {
            it != null -> refreshAdapter(it)
            args == ALL -> refreshAdapter(viewModel.allData.value)
            args == FAV -> refreshAdapter(viewModel.favData.value)
        }
    }

    private fun setViewModel() {
        args = arguments?.getString(KEY).toString()
        viewModel.finderData.observe(viewLifecycleOwner, finderObserver)
        when (args) {
            ALL -> viewModel.allData.observe(viewLifecycleOwner, fullObserver)
            FAV -> viewModel.favData.observe(viewLifecycleOwner, fullObserver)
        }
    }

    private fun setAdapter() = ChannelsAdapter(object : OnRecycleClickListener {
        override fun channelClick(channel: ChannelUI) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, PlayerFragment.newInstance(channel))
                .addToBackStack(null)
                .commit()
        }

        override fun favoriteClick(channel: ChannelUI) = viewModel.updateFavorite(channel)
    })

    private fun progressBarVisibility(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

    private fun handleError(message: String?) {
        progressBarVisibility(false)
        binding.reload.apply {
            isVisible = true
            setOnClickListener {
                viewModel.getData()
                isVisible = false
            }
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ALL = "ALL"
        const val FAV = "FAV"
        const val KEY = "KEY"
        fun newInstance(str: String): ViewPagerItem {
            val b = Bundle().also { it.putString(KEY, str) }
            return ViewPagerItem().also { it.arguments = b }
        }
    }
}
