package ru.test.playerexo.ui.main.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> ViewPagerItem.newInstance(ViewPagerItem.FAV)
            else -> ViewPagerItem.newInstance(ViewPagerItem.ALL)
        }
    }
}
