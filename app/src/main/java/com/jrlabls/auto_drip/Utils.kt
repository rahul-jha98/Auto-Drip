package com.jrlabls.auto_drip

object Utils {
    fun getDrawableFor(percent: Long) : Int {
        return when {
            percent >= 95 -> R.drawable.ic_group_9
            percent >= 85 -> R.drawable.ic_group_10
            percent >= 75 -> R.drawable.ic_group_11
            percent >= 50 -> R.drawable.ic_group_12
            percent >= 30 -> R.drawable.ic_group_13
            percent >= 10 -> R.drawable.ic_group_14
            else -> R.drawable.ic_group_15
        }
    }
}