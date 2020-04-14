package com.jrlabls.auto_drip.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

import com.jrlabls.auto_drip.R
import com.jrlabls.auto_drip.adapters.BedAdapter
import com.jrlabls.auto_drip.models.BedEntry
import com.jrlabls.auto_drip.ui.DetailActivity
import kotlinx.android.synthetic.main.fragment_beds.*

class BedsFragment : Fragment() {

    private lateinit var bedAdapter: BedAdapter
    var reference = FirebaseDatabase.getInstance().getReference("/Beds")

    var listener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {

        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            val bedData = p0.getValue(BedEntry::class.java)!!
            bedAdapter.updateBedData(bedData)
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val bedData = p0.getValue(BedEntry::class.java)!!
            bedAdapter.addBedData(bedData)

            bedsProgressBar.visibility = View.GONE
        }

        override fun onChildRemoved(p0: DataSnapshot) {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bedAdapter = BedAdapter(context!!) {bedEntry ->
            var intent = Intent(activity!!, DetailActivity::class.java)
            intent.putExtra("entry", bedEntry)
            startActivity(intent)
        }

        bedsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
            adapter = bedAdapter
        }

        reference.addChildEventListener(listener)
    }
}
