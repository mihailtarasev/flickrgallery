package com.mihailtarasev.flickrgallery.extension

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WrapContentLinearLayoutManager(context: Context, rotation: Int, portraitCountRows: Int, landscapeCountRows: Int) :
    GridLayoutManager(context, getSpanCountBy(rotation, portraitCountRows, landscapeCountRows)) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) { }
    }

    companion object {
        private const val DEGREES0 = 0
        private const val DEGREES90 = 1
        private const val DEGREES180 = 2
        private const val DEGREES270 = 3

        fun getSpanCountBy(rotation: Int, portraitCountRows: Int, landscapeCountRows: Int): Int {
            return when (rotation) {
                DEGREES0 -> portraitCountRows
                DEGREES90 -> landscapeCountRows
                DEGREES180 -> portraitCountRows
                DEGREES270 -> landscapeCountRows
                else -> portraitCountRows
            }
        }
    }
}