package com.jrlabls.auto_drip.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jrlabls.auto_drip.R
import com.jrlabls.auto_drip.Utils
import com.jrlabls.auto_drip.models.BedEntry

class BedAdapter(val context: Context, private val itemClick: (BedEntry) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_TITLE = 0
    private val TYPE_HEADER = 1
    private val TYPE_ITEM = 2

    private val bedsList = ArrayList<BedEntry>()

    private val inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                BedViewHolder(inflater.inflate(R.layout.list_item_bed, parent, false), itemClick)
            }
            TYPE_HEADER -> {
                HeaderViewHolder(inflater.inflate(R.layout.list_item_header, parent, false))
            }
            else -> {
                HeaderViewHolder(inflater.inflate(R.layout.list_item_title, parent, false))
            }
        }

    }

    override fun getItemCount(): Int = when (bedsList.size) {
        0 -> 1
        else -> bedsList.size + 3
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BedViewHolder -> {
                if (position == bedsList.size + 2) {
                    holder.itemView.visibility = View.INVISIBLE
                } else {
                    holder.itemView.visibility = View.VISIBLE
                    holder.bind(bedsList[position - 2])
                }
            }
            is HeaderViewHolder -> {
                holder.setHeader(if (position == 0) context.resources.getString(R.string.app_name) else "All Beds")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_TITLE
            1 -> TYPE_HEADER
            else -> TYPE_ITEM
        }
    }

    fun swapList(newList: List<BedEntry>) {
        bedsList.clear()
        bedsList.addAll(newList)
        notifyDataSetChanged()
    }

    fun addBedData(bedEntry: BedEntry) {
        bedsList.add(bedEntry)
        notifyItemInserted(bedsList.size + 1)
    }

    fun updateBedData(bedData: BedEntry) {
        for (i in bedsList.indices) {
            if (bedsList[i].Id == bedData.Id) {
                bedsList[i] = bedData
                notifyItemChanged(i + 2)
                return
            }
        }
    }

    inner class BedViewHolder(itemView: View, private val itemClick: (BedEntry) -> Unit) :
            RecyclerView.ViewHolder(itemView) {
        private val idTextView = itemView.findViewById<TextView>(R.id.bedIDTextView)
        private val rateTextView = itemView.findViewById<TextView>(R.id.rateTextView)
        private val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        private val levelImageView = itemView.findViewById<ImageView>(R.id.levelImageView)
        fun bind(bedEntry: BedEntry) {
            idTextView.text = bedEntry.Id.toString()
            rateTextView.text = "${bedEntry.rate} ml/min"

            if (bedEntry.Switch1 == 0)
                rateTextView.text = "0 ml/min"
            nameTextView.text = bedEntry.patientname
            itemView.setOnClickListener { itemClick(bedEntry) }

            levelImageView.setImageDrawable(context!!.getDrawable(Utils.getDrawableFor(bedEntry.Level)))
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerTextView = itemView as TextView

        fun setHeader(title: String) {
            headerTextView.text = title
        }
    }
}
