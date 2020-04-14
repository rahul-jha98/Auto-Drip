package com.jrlabls.auto_drip.ui

import android.os.Handler
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jrlabls.auto_drip.R

class BottomAppBarManager(private val bottomAppBar: BottomAppBar, private val bottomAppBarFab: FloatingActionButton) {
    fun switchToPage(pageNo : Int) {
        if(pageNo % 2 == 0) {
            bottomAppBar.replaceMenu(R.menu.fleet_bottom_nav_menu)
            bottomAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {

                }
                true
            }
        }
        else {
            bottomAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {

                }
                true
            }
        }
    }
}