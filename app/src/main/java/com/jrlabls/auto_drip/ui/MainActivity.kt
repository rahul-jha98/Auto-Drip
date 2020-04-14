package com.jrlabls.auto_drip.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.messaging.FirebaseMessaging
import com.jrlabls.auto_drip.R
import com.jrlabls.auto_drip.customuielements.animatedelements.FabToBottomSheetAnimator
import com.jrlabls.auto_drip.services.MyNotificationManager
import com.jrlabls.auto_drip.ui.fragments.BedsFragment
import com.jrlabls.auto_drip.ui.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationDrawerFragment.MenuInteractionCallback {

    private var bottomNavigationDrawerFragment = BottomNavigationDrawerFragment()
    private lateinit var bottomAppBarManager: BottomAppBarManager
    private lateinit var fabToBottomSheetAnimator : FabToBottomSheetAnimator


    private var sheetFragment: Fragment? = null
    private lateinit var persitentBottomSheetBehaviour : BottomSheetBehavior<View>


    private var fragment: Fragment? = null
    private var fragmentClass = arrayOf(BedsFragment::class.java, HomeFragment::class.java)
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpBottomNavBar()

        if(fragment == null) {
            try {
                fragment = fragmentClass[currentPage - 1].newInstance()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        supportFragmentManager.beginTransaction().replace(R.id.mainContentSpace, fragment!!).commit()

        bottomAppBarManager = BottomAppBarManager(bottomAppBar, bottomAppBarFab)
        persitentBottomSheetBehaviour = BottomSheetBehavior.from(persistent_bottom_sheet)
        persitentBottomSheetBehaviour.skipCollapsed = true
        fabToBottomSheetAnimator = FabToBottomSheetAnimator(this, bottomAppBarFab, persistent_bottom_sheet, shadowView, persitentBottomSheetBehaviour)
        sheetFragment = PersistentBottomSheet()
        supportFragmentManager.beginTransaction().replace(R.id.persistent_bottom_sheet, sheetFragment!!).commit()

        persitentBottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN
        shadowView.visibility = View.GONE
        setUpBottomSheet()
        bottomAppBarFab.setOnClickListener {
            fabToBottomSheetAnimator.showBottomSheet()
        }

        FirebaseMessaging.getInstance().subscribeToTopic("news")

        createNotificationChannel()

    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName ="Emergency"
            val channelDescription = "In case of level falls to 10%"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                MyNotificationManager.NOTIFICATION_ID.toString(),
                channelName, importance)
            channel.description = channelDescription

            val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setUpBottomSheet() {
        persitentBottomSheetBehaviour.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if(slideOffset <= 0) {
                    snackBarParent.alpha = (slideOffset + 1) * .5f
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        if(!bottomAppBarFab.isShown) {
                            bottomAppBarFab.translationY = 0f
                            bottomAppBarFab.show()
                        }
                        shadowView.visibility = View.GONE
                    }
                }
            }

        })
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (persitentBottomSheetBehaviour.state == BottomSheetBehavior.STATE_EXPANDED) {

                val outRect = Rect()
                persistent_bottom_sheet.getGlobalVisibleRect(outRect)

                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    fabToBottomSheetAnimator.hideBottomSheet()

                    return true
                }

            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun setUpBottomNavBar() {
        bottomAppBar.replaceMenu(R.menu.fleet_bottom_nav_menu)

        bottomAppBar.setNavigationOnClickListener {
            if (!bottomNavigationDrawerFragment.isAdded)
                bottomNavigationDrawerFragment.show(supportFragmentManager, bottomNavigationDrawerFragment.tag)
        }
    }

    override fun switchToPageNo(pageNo: Int) {
        var newFragment: Fragment? = null
        if (pageNo % 2 == 0) {
            bottomAppBarManager.switchToPage(0)
            try {
                newFragment = fragmentClass[1].newInstance() as Fragment
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            bottomAppBarManager.switchToPage(1)
            try {
                newFragment = fragmentClass[0].newInstance() as Fragment
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (currentPage != pageNo) {
//            if (pageNo % 2 == 0) {
//                AnimationSetter.setSlideAnimations(newFragment, fragment, pageNo, currentPage)
//            } else {
//                AnimationSetter.setFadeAnimations(newFragment, fragment, pageNo, currentPage)
//            }
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_transition_enter, R.anim.fragment_transition_exit)
                .replace(R.id.mainContentSpace, newFragment!!)
                .setReorderingAllowed(true)
                .commit()
            fragment = newFragment
            currentPage = pageNo
        }
    }

    override fun onBackPressed() {
        if(persitentBottomSheetBehaviour.state != BottomSheetBehavior.STATE_HIDDEN && !bottomAppBarFab.isOrWillBeShown) {
            fabToBottomSheetAnimator.hideBottomSheet()
        } else {
            super.onBackPressed()
        }
    }
}
