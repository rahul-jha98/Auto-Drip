package com.jrlabls.auto_drip.customuielements

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner

class ReselectableSpinner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatSpinner(context, attrs, defStyleAttr) {

    var onAnyItemSelectedListener : AdapterView.OnItemSelectedListener? = null
    var onCloseListener : OnSpinnerCancelledListener? = null

    var somethingSelected = false
    override fun setSelection(position: Int, animate: Boolean) {
        super.setSelection(position, animate)
        somethingSelected = true
        onAnyItemSelectedListener?.onItemSelected(null, this, position, 0)
    }

    override fun setSelection(position: Int) {
        super.setSelection(position)
        somethingSelected = true
        onAnyItemSelectedListener?.onItemSelected(null, this, position, 0)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if(hasFocus) {
            if(!somethingSelected)
                onCloseListener?.onSpinnerCancelled()
        } else {
           somethingSelected = false
        }

    }

    interface OnSpinnerCancelledListener {
        fun onSpinnerCancelled()
    }
}