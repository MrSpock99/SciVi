package itis.ru.scivi.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.scivi.R
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.utils.dpToPx
import kotlinx.android.synthetic.main.fragment_search.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


class SearchFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        KeyboardVisibilityEvent.setEventListener(
            activity
        ) { visible ->
            if (visible) {
                animateUp()
            } else {
                animateDown()
            }
        }
    }

    private fun animateUp() {
        tv_app_name.apply {
            val metrics = resources.displayMetrics
            animate()
                .translationYBy(-(metrics.heightPixels - (metrics.heightPixels.toFloat() - height)))
                .setListener(null)
                .duration =
                resources.getInteger(android.R.integer.config_shortAnimTime)
                    .toLong()

        }
        et_search.apply {
            (layoutParams as ViewGroup.MarginLayoutParams).marginStart = dpToPx(8)
            (layoutParams as ViewGroup.MarginLayoutParams).marginEnd = dpToPx(8)
            val metrics = resources.displayMetrics
            animate()
                .translationYBy(
                    -(metrics.heightPixels - (metrics.heightPixels.toFloat() - dpToPx(
                        100
                    )))
                )
                .setListener(null)
                .duration =
                resources.getInteger(android.R.integer.config_shortAnimTime)
                    .toLong()
        }

    }

    private fun animateDown() {
        tv_app_name.apply {
            val metrics = resources.displayMetrics
            animate()
                .translationYBy((metrics.heightPixels - (metrics.heightPixels.toFloat() - height)))
                .setListener(null)
                .duration =
                resources.getInteger(android.R.integer.config_shortAnimTime)
                    .toLong()

        }
        et_search.apply {
            val metrics = resources.displayMetrics
            animate()
                .translationYBy((metrics.heightPixels - (metrics.heightPixels.toFloat() - dpToPx(100))))
                .setListener(null)
                .duration =
                resources.getInteger(android.R.integer.config_shortAnimTime)
                    .toLong()

        }
    }

    private fun setOnClickListeners() {

    }
}