package com.vishnu.whatsappclone.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vishnu.whatsappclone.fragments.ChatFragment
import com.vishnu.whatsappclone.fragments.UsersFragment

class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    //Override method to get the number of fragments
    override fun getCount(): Int {
       return 2
    }

    //Override method to get a particular fragment
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return UsersFragment()
            }
            1 -> {
                return ChatFragment()
            }
        }
        return null!!
    }

}