package com.example.osagosravni.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CharacterItemDecoration(space: Int) : RecyclerView.ItemDecoration() {
    private var mSpace = space

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = mSpace.dp2px(view.context)
        }

    }

    private fun Int.dp2px(context: Context): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }
}
