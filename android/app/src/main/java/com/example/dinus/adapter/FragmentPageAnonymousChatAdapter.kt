package com.example.dinus.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dinus.feature.anonymousChat.AnonymousChatFragment
import com.example.dinus.feature.anonymousChat.FindAnonymousChatFragment

class FragmentPageAnonymousChatAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
         FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FindAnonymousChatFragment()
            1 -> AnonymousChatFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }

}