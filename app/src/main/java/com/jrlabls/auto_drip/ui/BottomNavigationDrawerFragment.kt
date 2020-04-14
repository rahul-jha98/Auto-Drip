package com.jrlabls.auto_drip.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jrlabls.auto_drip.R
import kotlinx.android.synthetic.main.bottom_navigation_sheet.*


class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {
    private var callbackReceiver: MenuInteractionCallback? = null
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_navigation_sheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            val pageNo =
                when (menuItem.itemId) {
                    R.id.beds -> 1
                    R.id.patients -> 2

                    else -> 4
                }
            if(!menuItem.isChecked)
                callbackReceiver?.switchToPageNo(pageNo)
            dismiss()
            true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackReceiver = activity as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        callbackReceiver = null
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet as View)
        }

        return dialog
    }

    interface MenuInteractionCallback {
        fun switchToPageNo(pageNo: Int)
    }


}