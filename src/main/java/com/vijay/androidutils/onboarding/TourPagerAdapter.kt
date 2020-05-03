/* $Id$ */
package com.vijay.androidutils.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TourPagerAdapter(fm: FragmentManager, val layoutIds: IntArray) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val fragment = TourPage()
        val bundle = Bundle()
        bundle.putInt(LAYOUT, layoutIds[position])
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return layoutIds.size
    }

    class TourPage : Fragment() {
        var layout: Int = -1
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            layout = arguments!!.getInt(LAYOUT)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(layout, container, false)
        }
    }

    companion object {
        const val LAYOUT = "layout"
    }

}

