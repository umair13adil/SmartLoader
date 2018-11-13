package com.umairadil.smartdownloaderapp.ui.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.*
import androidx.fragment.app.Fragment
import com.umairadil.smartdownloaderapp.R
import kotlinx.android.synthetic.main.layout_progress.*

abstract class BaseFragment : Fragment() {

    fun showLoading() {
        card_progress?.visibility = View.VISIBLE
    }

    fun hideLoading() {

        //Hide Progress Layout
        card_progress?.animate()
                ?.translationY(card_progress.height.toFloat())
                ?.alpha(0.0f)
                ?.setDuration(resources.getInteger(R.integer.anim_duration_long).toLong())
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        card_progress.visibility = View.GONE
                    }
                })
    }

    fun hideViewWithAnimation(view: View?) {

        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.startOffset = resources.getInteger(R.integer.anim_duration_long).toLong()
        fadeOut.duration = resources.getInteger(R.integer.anim_duration_long).toLong()

        val animation = AnimationSet(false)
        animation.addAnimation(fadeOut)
        view?.animation = animation

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                view?.visibility = View.GONE
            }
        })
    }

    fun showViewWithAnimation(view: View?) {

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = resources.getInteger(R.integer.anim_duration_long).toLong()

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        view?.animation = animation

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationStart(p0: Animation?) {
                view?.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(p0: Animation?) {}
        })
    }
}
