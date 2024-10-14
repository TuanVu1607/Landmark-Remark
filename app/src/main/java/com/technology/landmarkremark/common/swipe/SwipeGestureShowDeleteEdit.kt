package com.technology.landmarkremark.common.swipe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technology.landmarkremark.R
import com.technology.landmarkremark.utils.BitmapUtils
import kotlin.math.max
import kotlin.math.min

abstract class SwipeGestureShowDeleteEdit(
    val context: Context,
    val listener: SwipeActionDeleteEditListener
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

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

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        deleteButtonVisible = false
        editButtonVisible = false
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
            val itemSwipeHeight = (itemView.bottom - itemView.top).toFloat()
            val itemSwipeWidth = ((itemView.right - itemView.left) / 5).toFloat()
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
                    itemSwipeHeight,
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
                        itemSwipeHeight,
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
        itemHeight: Float,
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

            val iconSize = context.resources.getDimension(R.dimen.base_icon_size)
            val iconDest = RectF(
                calculateLeftOffset(viewRectF, itemWidth, iconSize),
                calculateTopOffset(viewRectF, itemHeight, iconSize),
                calculateRightOffset(viewRectF, itemWidth, iconSize),
                calculateBottomOffset(viewRectF, itemHeight, iconSize)
            )
            canvas.drawBitmap(icon, null, iconDest, pView)
        }
    }

    private fun calculateLeftOffset(viewRectF: RectF, itemWidth: Float, iconSize: Float): Float {
        return max(
            viewRectF.left + context.resources.getDimension(R.dimen.base_m_p_s),
            viewRectF.right - (itemWidth / 2) - (iconSize / 2)
        )
    }

    private fun calculateRightOffset(viewRectF: RectF, itemWidth: Float, iconSize: Float): Float {
        return min(
            viewRectF.right - context.resources.getDimension(R.dimen.base_m_p_s),
            viewRectF.left + (itemWidth / 2) + (iconSize / 2)
        )
    }

    private fun calculateTopOffset(viewRectF: RectF, itemHeight: Float, iconSize: Float): Float {
        return max(
            viewRectF.top + context.resources.getDimension(R.dimen.base_m_p_s),
            viewRectF.bottom - (itemHeight / 2) - (iconSize / 2)
        )
    }

    private fun calculateBottomOffset(viewRectF: RectF, itemHeight: Float, iconSize: Float): Float {
        return min(
            viewRectF.bottom - context.resources.getDimension(R.dimen.base_m_p_s),
            viewRectF.top + (itemHeight / 2) + (iconSize / 2)
        )
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