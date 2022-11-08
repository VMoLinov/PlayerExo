package ru.test.playerexo.ui.main

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import ru.test.playerexo.MainActivity
import ru.test.playerexo.R
import ru.test.playerexo.app.App
import ru.test.playerexo.databinding.FragmentMainBinding
import ru.test.playerexo.ui.main.viewpager.ViewPagerAdapter
import ru.test.playerexo.utils.AppTextWatcher
import ru.test.playerexo.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            this, App.getComponent().viewModelFactory()
        )[MainViewModel::class.java]
        (requireActivity() as MainActivity).apply {
            statusBarVisibility(true)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFinder()
        setTableLayout()
    }

    private fun setFinder() {
        binding.finder.addTextChangedListener(AppTextWatcher {
            viewModel.finderRefresh(it)
        })
    }

    private fun setTableLayout() {
        val tab = binding.tabLayout.tab
        val viewPager = binding.tabLayout.viewPager
        viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tab, viewPager) { title, position ->
            when (position) {
                0 -> title.text = resources.getString(R.string.tab_main)
                else -> title.text = resources.getString(R.string.tab_favorites)
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
