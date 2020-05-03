package com.vijay.androidutils.onboarding

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.vijay.androidutils.R
import java.lang.IllegalStateException

class TourFragment : Fragment() {
    private var selected: Int = -1
    private var unselected: Int = -1
    lateinit var layoutIDs: IntArray
    private var tourListener: TourListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TourListener) {
            tourListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutIDs = arguments?.getIntArray(LAYOUT_IDS)!!
        selected = Color.BLACK
        unselected = resources.getColor(R.color.tour_indicator_unselected)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tour_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val introPager = view.findViewById<ViewPager>(R.id.tour_pager)
        val adapter = TourPagerAdapter(activity!!.supportFragmentManager, layoutIDs)
        introPager.adapter = adapter

        val layout = view.findViewById<LinearLayout>(R.id.tour_indicator_layout)
        val size = introPager.adapter!!.count

        for (i in 0 until size) {
            val inflatedView = layoutInflater.inflate(R.layout.tour_indicator, null)
            layout.addView(inflatedView)
        }

        introPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                setIndicator(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        view.findViewById<TextView>(R.id.tour_get_started).setOnClickListener {
            tourListener?.onGetStarted()
        }
        setIndicator(0)
    }

    private fun setIndicator(position: Int) {
        val layout = view!!.findViewById<LinearLayout>(R.id.tour_indicator_layout)
        for (i in 0 until layout.childCount) {
            var view: View? = layout.getChildAt(i)
            if (view != null) {
                view = view.findViewById(R.id.tour_indicator)
                val drawable = view!!.background
                if (position == i) {
                    drawable.colorFilter = PorterDuffColorFilter(selected, PorterDuff.Mode.MULTIPLY)
                } else {
                    drawable.colorFilter =
                        PorterDuffColorFilter(unselected, PorterDuff.Mode.MULTIPLY)
                }
                view.background = drawable
            }
        }
    }

    interface TourListener {
        fun onGetStarted()
    }

    companion object {
        const val LAYOUT_IDS = "layoutIDs"

        fun openFragment(
            activity: AppCompatActivity,
            layoutIDs: IntArray
        ) {
            val fm = activity.supportFragmentManager
            val ft = fm.beginTransaction()
            val fragment = TourFragment()
            fragment.arguments = Bundle().apply {
                putIntArray(LAYOUT_IDS, layoutIDs)
            }
            ft.replace(android.R.id.content, fragment, "TOUR_FRAGMENT").commit()
        }

        fun removeFragment(activity: AppCompatActivity) {
            val fm = activity.supportFragmentManager
            val fragment = fm.findFragmentByTag("TOUR_FRAGMENT")
            if (fragment != null) {
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
            }
        }
    }
}