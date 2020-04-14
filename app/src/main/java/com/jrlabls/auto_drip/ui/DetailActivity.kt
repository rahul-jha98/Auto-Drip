package com.jrlabls.auto_drip.ui

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.cardview.widget.CardView
import com.google.firebase.database.FirebaseDatabase
import com.jrlabls.auto_drip.R
import com.jrlabls.auto_drip.Utils
import com.jrlabls.auto_drip.models.BedEntry
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var bedEntry: BedEntry

    private var elevation = 0f

    private lateinit var mSetRightOut : AnimatorSet
    private lateinit var mSetLeftIn : AnimatorSet

    private lateinit var cardA : CardView
    private lateinit var cardB : CardView
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        bedEntry = intent.getParcelableExtra<BedEntry>("entry")!!

        changeCameraDistance()
        loadAnimations()

        turnButton1.setOnClickListener {
            flipCard()
        }

        stopButton.setOnClickListener {
            sendStop()
        }

        regulateButton.setOnClickListener {
            sendStart()
        }



        imageView3.setImageDrawable(getDrawable(Utils.getDrawableFor(bedEntry.Level)))

        leftTextView.text = "${bedEntry.Level} %"
        rateTextView.text = "${bedEntry.rate} ml/min"
        if (bedEntry.Switch1 == 0)
            rateTextView.text = "0 ml/min"
        timeToEmptyTextView.text = "${bedEntry.timeleft} min"

        nameTextView.text = bedEntry.patientname
        bedIDTextView.text = bedEntry.Id.toString()
        doctorNameTextView.text = bedEntry.doctorname
        medicineTextView.text = bedEntry.medicine

    }

    private fun sendStop() {
        val ref = FirebaseDatabase.getInstance().getReference("/Beds/Bed" + bedEntry.Id + "/Switch1")
        ref.setValue(0)
        rateTextView.text = "0 ml/min"
    }

    private fun sendStart() {
        val ref = FirebaseDatabase.getInstance().getReference("/Beds/Bed" + bedEntry.Id + "/Switch1")
        ref.setValue(1)
        rateTextView.text = "${bedEntry.rate} ml/min"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun flipCard() {
        if (count % 2 == 0) {
            cardA = frontCardView
            cardB = backCardView
        } else {
            cardA = backCardView
            cardB = frontCardView
        }
        mSetRightOut.setTarget(cardA)
        mSetLeftIn.setTarget(cardB)

        cardA.elevation = 0f

        mSetLeftIn.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                cardB.elevation = elevation
            }
        })

        mSetRightOut.start()
        mSetLeftIn.start()

        count++

        if (count % 2 == 0) {
            regulateButton.setOnClickListener {
                sendStart()
            }
            stopButton.setOnClickListener {
                sendStop()
            }
            deleteButton.setOnClickListener {

            }
            backButton.setOnClickListener {

            }
            turnButton1.setOnClickListener {
                flipCard()
            }
        }
        else {
            regulateButton.setOnClickListener {

            }
            stopButton.setOnClickListener {
            }
            deleteButton.setOnClickListener {

            }
            backButton.setOnClickListener {
                flipCard()
            }
            turnButton1.setOnClickListener {

            }
        }
    }


    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        elevation = resources.displayMetrics.density * 2
        frontCardView.cameraDistance = scale
        backCardView.cameraDistance = scale
    }

    private fun loadAnimations() {
        mSetRightOut = AnimatorInflater.loadAnimator(this, R.animator.out_anim) as AnimatorSet
        mSetLeftIn = AnimatorInflater.loadAnimator(this, R.animator.in_anim) as AnimatorSet
    }
}
