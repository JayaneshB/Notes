package com.project.notes.swipegestures

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.project.notes.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlin.math.min

abstract class SwipeGestures(context: Context) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
    ItemTouchHelper.LEFT
) {

    private val deleteColor = ContextCompat.getColor(context, R.color.orangish_yellow)
    private val deleteIcon = R.drawable.ic_delete


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val swipeLimit = 1f

        val limit  = min(dX, swipeLimit)

        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        ).addActionIcon(deleteIcon).addSwipeLeftBackgroundColor(deleteColor).create().decorate()

        super.onChildDraw(c, recyclerView, viewHolder, limit, dY, actionState, isCurrentlyActive)
    }


}