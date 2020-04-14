package com.jrlabls.auto_drip.customuielements.animatedelements

import android.animation.*
import android.content.Context
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.ViewAnimationUtils
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

class FabToBottomSheetAnimator(private val context: Context,
                               private val fabView: FloatingActionButton, private val bottomSheet : View,
                               private val shadowView: View,
                               private val bottomSheetBehavior: BottomSheetBehavior<View>) {
    private val ANIM_CURVED_PATH_DURATION = 150L
    private val ANIM_CIRCULAR_REVEAL_DURATION = 250L
    private val ANIM_CIRCULAR_REVEAL_DELAY = 100L
    private val ANIM_REVERSE_CIRCULAR_REVEAL_DURATION = 200L
    private val ANIM_REVERSE_PATH_DELAY = 200L
    private val ANIM_REVERSE_PATH_TIME = 150L

    private val density = context.resources.displayMetrics.density

    private var showAnimatorSet: AnimatorSet? = null
    private var hideAnimatorSet: AnimatorSet? = null

    private var fabTranslationY = 0f
    fun showBottomSheet() {
        showAnimatorSet?.let{showAnimatorSetNotNull->
            if(showAnimatorSetNotNull.isStarted) {
                return
            }
        }

        hideAnimatorSet?.let {
            if(it.isStarted) {
                return
            }
        }
        bottomSheet.visibility = View.INVISIBLE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        val curvedAnim = createCurvedPath(fabView, bottomSheet)
        val fadeAnim = createFadeOutAnimation()
        val circularRevealAnim = createCircularRevealAnim(bottomSheet)
        curvedAnim?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)

                fabView.hide()
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                bottomSheet.visibility = View.VISIBLE
            }
        })

        circularRevealAnim?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                shadowView.visibility = View.VISIBLE
            }
        })
        showAnimatorSet = AnimatorSet()
        showAnimatorSet?.playTogether(curvedAnim, circularRevealAnim)
        showAnimatorSet?.start()

    }
    fun hideBottomSheet() {

        //fabView.alpha = 0f


        showAnimatorSet?.let{showAnimatorSetNotNull->
            if(showAnimatorSetNotNull.isStarted) {
                return
            }
        }

        hideAnimatorSet?.let{
            if(it.isStarted) {
                return
            }
        }

        val circularRevealAnim = createReverseCircularRevealAnim(bottomSheet)
        val reversePathAnim = createReverseCurvePath()
        hideAnimatorSet = AnimatorSet()
        hideAnimatorSet?.playTogether( circularRevealAnim, reversePathAnim)
        hideAnimatorSet?.start()
    }

    private fun createFadeOutAnimation(): Animator? {
        val anim = ObjectAnimator.ofFloat(fabView, "alpha", 1f, 0f)
        anim.duration = ANIM_CURVED_PATH_DURATION
        anim.startDelay = ANIM_CURVED_PATH_DURATION
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                bottomSheet.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                fabView.hide()
            }
        })
        return anim
    }

    private fun createCircularRevealAnim(myView: View): Animator? {
        val cx = (myView.width / 2)
        val cy = myView.height / 2

        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val animator = ViewAnimationUtils.createCircularReveal(bottomSheet, cx, cy, 0f, finalRadius)
        animator.duration = ANIM_CIRCULAR_REVEAL_DURATION
        animator.startDelay = ANIM_CURVED_PATH_DURATION
        return animator
    }

    private fun createCurvedPath(fabView: FloatingActionButton, navigationView: View): Animator? {
        fabTranslationY = (navigationView.y + navigationView.height/2) - (fabView.y + fabView.height/2)
        fabTranslationY /= density
        val anim = ObjectAnimator.ofFloat(
            fabView, "translationY",
            0f, -1 * fabTranslationY)

        anim.duration = ANIM_CURVED_PATH_DURATION

        return anim
    }

    private fun createReverseCircularRevealAnim(myView : View): Animator? {

        val cx = (myView.width / 2)
        val cy = myView.height / 2

        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val animation = ViewAnimationUtils.createCircularReveal(bottomSheet, cx, cy, finalRadius, 0f)
        animation.duration = ANIM_REVERSE_CIRCULAR_REVEAL_DURATION
        animation.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                shadowView.visibility = View.GONE
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                bottomSheet.visibility = View.INVISIBLE
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                fabView.show()
            }
        })

        return animation
    }

    private fun createFadeInAnimation(): Animator? {
        val anim = ObjectAnimator.ofFloat(fabView, "alpha", 0f, 1f)
        anim.duration = ANIM_REVERSE_PATH_TIME
        anim.startDelay = ANIM_REVERSE_PATH_DELAY
        return anim
    }

    private fun createReverseCurvePath() : Animator?{
        val anim = ObjectAnimator.ofFloat(
            fabView, "translationY",
            -1 * fabTranslationY, 0f)

        anim.duration = ANIM_REVERSE_PATH_TIME
        anim.startDelay = ANIM_REVERSE_PATH_DELAY
        return anim
    }
}