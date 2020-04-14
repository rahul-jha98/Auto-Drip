package com.jrlabls.auto_drip.customuielements.animatedelements

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.transition.Transition
import android.transition.TransitionValues
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import com.google.android.material.card.MaterialCardView

class TranstionLiftOff @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : Transition(context, attrs) {
    override fun captureStartValues(transitionValues: TransitionValues) {
        if(transitionValues.view !is MaterialCardView) {
           return
        }
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        if(transitionValues.view !is MaterialCardView) {
            return
        }
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if(startValues == null || endValues == null) {
            return null
        }

        return ObjectAnimator.ofFloat(endValues.view, View.TRANSLATION_Z, 10f, 0f)
    }
}