package itis.ru.scivi.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import itis.ru.scivi.R
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.utils.dpToPx
import kotlinx.android.synthetic.main.fragment_search.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


class SearchFragment : BaseFragment() {
    private lateinit var transitionsContainer: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transitionsContainer = view.findViewById(R.id.search_container) as ViewGroup
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
        TransitionManager.beginDelayedTransition(transitionsContainer, getTransition())
        tv_app_name.visibility = View.GONE

        val marginParams = (et_search.layoutParams as ViewGroup.MarginLayoutParams)
        marginParams.marginStart = dpToPx(2)
        marginParams.marginEnd = dpToPx(2)
        et_search.layoutParams = marginParams

        val constraintSet = ConstraintSet()
        constraintSet.clone(search_container)
        constraintSet.connect(
            R.id.et_search, ConstraintSet.TOP, R.id.search_container, ConstraintSet.TOP, dpToPx(8)
        )
        constraintSet.applyTo(search_container)
    }

    private fun animateDown() {
        TransitionManager.beginDelayedTransition(transitionsContainer, getTransition())
        tv_app_name.visibility = View.VISIBLE

        val params = (et_search.layoutParams as ViewGroup.MarginLayoutParams)
        params.marginStart = dpToPx(16)
        params.marginEnd = dpToPx(16)
        et_search.layoutParams = params

        val constraintSet = ConstraintSet()
        constraintSet.clone(search_container)
        constraintSet.connect(
            R.id.et_search,
            ConstraintSet.TOP,
            R.id.tv_app_name,
            ConstraintSet.BOTTOM,
            dpToPx(48)
        )
        constraintSet.applyTo(search_container)
    }

    private fun getTransition(): Transition {
        val changeBounds: Transition = ChangeBounds()
        changeBounds.duration = 3000
        changeBounds.interpolator = LinearInterpolator()
        return changeBounds
    }

    private fun setOnClickListeners() {

    }
}