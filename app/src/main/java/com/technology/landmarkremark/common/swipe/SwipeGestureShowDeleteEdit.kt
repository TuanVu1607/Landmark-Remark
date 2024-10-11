package com.technology.landmarkremark.common.swipe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.view.MotionEvent
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technology.landmarkremark.R
import com.technology.landmarkremark.common.extensions.toast
import com.technology.landmarkremark.utils.BitmapUtils
import kotlin.math.max

abstract class SwipeGestureShowDeleteEdit(
    val context: Context,
    val listener: SwipeActionDeleteEditListener
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private var deleteButtonVisible = false
    private var editButtonVisible = false

    private var deleteButtonRectF = RectF()
    private var editButtonRectF = RectF()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {//vẽ nền và biểu tượng cho hành động vuốt
            //Tính toán width & height của item xuất hiện khi vuốt
            val itemView = viewHolder.itemView
            val itemSwipeWidth = (itemView.right.toFloat() - itemView.left.toFloat()) / 5
            /*
             * Swipe left: dX < 0
             * Swipe right: dX > 0
             */
            if (dX < 0) {
                val swipeLeftWidth = itemView.right + dX
                deleteButtonRectF = RectF(
                    max(swipeLeftWidth, itemView.right - itemSwipeWidth),
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat()
                )
                deleteButtonVisible = true
                drawActionButton(
                    canvas,
                    itemSwipeWidth,
                    deleteButtonRectF,
                    context.getColor(R.color.red_500),
                    R.drawable.ic_delete
                )
                if (swipeLeftWidth < itemView.right - itemSwipeWidth) {
                    editButtonRectF = RectF(
                        max(swipeLeftWidth, itemView.right - 2 * itemSwipeWidth),
                        itemView.top.toFloat(),
                        itemView.right.toFloat() - itemSwipeWidth,
                        itemView.bottom.toFloat()
                    )
                    editButtonVisible = true
                    drawActionButton(
                        canvas,
                        itemSwipeWidth,
                        editButtonRectF,
                        context.getColor(R.color.blue_700),
                        R.drawable.ic_edit
                    )
                } else {
                    editButtonVisible = false
                }
            }
        } else {
            deleteButtonVisible = false
            editButtonVisible = false
        }
        if (dX == 0f) {
            deleteButtonVisible = false
            editButtonVisible = false
        }
        super.onChildDraw(
            canvas,
            recyclerView,
            viewHolder,
            dX * 2 / 5,// Giới hạn khoảng cách vuốt để nhận sự kiện nhỏ nhất là chiều dài của các item
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    private fun drawActionButton(
        canvas: Canvas,
        itemWidth: Float,
        viewRectF: RectF,
        viewColor: Int,
        iconId: Int
    ) {
        val pView = Paint()
        pView.color = viewColor
        canvas.drawRect(viewRectF, pView)

        val drawable = ResourcesCompat.getDrawable(context.resources, iconId, null)
        drawable?.let {
            it.setTint(Color.WHITE)
            val icon: Bitmap = BitmapUtils.drawableToBitmap(it)
            val iconDest = RectF(
                viewRectF.left + itemWidth / 4,
                viewRectF.top + itemWidth / 4,
                viewRectF.right - itemWidth / 4,
                viewRectF.bottom - itemWidth / 4
            )
            canvas.drawBitmap(icon, null, iconDest, pView)
        }
    }

    fun handleActionButtonClick(recyclerView: RecyclerView) {
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_UP) {
                    when {
                        deleteButtonVisible && deleteButtonRectF.contains(e.x, e.y) -> {
                            val position = calculatePositionItemTouch(rv, e)
                            if (position != -1) listener.onDelete(position)
                            return true
                        }

                        editButtonVisible && editButtonRectF.contains(e.x, e.y) -> {
                            val position = calculatePositionItemTouch(rv, e)
                            if (position != -1) listener.onEdit(position)
                            return true
                        }
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun calculatePositionItemTouch(recyclerView: RecyclerView, e: MotionEvent): Int {
        val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
        val firstVisibleItemPos = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()

        return (firstVisibleItemPos..lastVisibleItemPos).find { i ->
            val childView = layoutManager.findViewByPosition(i)
            childView != null &&
                    e.x in childView.left.toFloat()..childView.right.toFloat() &&
                    e.y in childView.top.toFloat()..childView.bottom.toFloat()
        } ?: -1
    }
}