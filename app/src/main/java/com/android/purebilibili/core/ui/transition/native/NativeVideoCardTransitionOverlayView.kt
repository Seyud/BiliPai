package com.android.purebilibili.core.ui.transition.native

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.max

internal class NativeVideoCardTransitionOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val scrimPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }
    private val coverPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val cardRect = RectF()
    private val bitmapSrcRect = Rect()
    private val coverClipPath = Path()

    private var frame: NativeVideoCardTransitionFrame? = null
    private var coverBitmap: Bitmap? = null

    init {
        visibility = GONE
        isClickable = false
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
    }

    fun setCoverBitmap(bitmap: Bitmap?) {
        coverBitmap = bitmap
        invalidate()
    }

    fun showFrame(frame: NativeVideoCardTransitionFrame) {
        this.frame = frame
        visibility = VISIBLE
        isClickable = true
        invalidate()
    }

    fun clearFrame() {
        frame = null
        coverBitmap = null
        visibility = GONE
        isClickable = false
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val currentFrame = frame ?: return

        if (currentFrame.scrimAlpha > 0f) {
            scrimPaint.color = Color.argb((currentFrame.scrimAlpha * 255).toInt(), 0, 0, 0)
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), scrimPaint)
        }

        val rect = currentFrame.cardRect
        cardRect.set(rect.left, rect.top, rect.right, rect.bottom)
        canvas.drawRoundRect(
            cardRect,
            currentFrame.cornerRadiusPx,
            currentFrame.cornerRadiusPx,
            cardPaint
        )
        drawCover(canvas, currentFrame)
    }

    private fun drawCover(canvas: Canvas, frame: NativeVideoCardTransitionFrame) {
        val bitmap = coverBitmap ?: return
        val coverAlpha = frame.coverAlpha.coerceIn(0f, 1f)
        if (coverAlpha <= 0f || cardRect.width() <= 1f || cardRect.height() <= 1f) return

        coverPaint.alpha = (coverAlpha * 255).toInt()
        resolveCenterCropSrcRect(bitmap, cardRect, bitmapSrcRect)

        coverClipPath.reset()
        coverClipPath.addRoundRect(
            cardRect,
            frame.cornerRadiusPx,
            frame.cornerRadiusPx,
            Path.Direction.CW
        )
        val checkpoint = canvas.save()
        canvas.clipPath(coverClipPath)
        canvas.drawBitmap(bitmap, bitmapSrcRect, cardRect, coverPaint)
        canvas.restoreToCount(checkpoint)
    }

    private fun resolveCenterCropSrcRect(bitmap: Bitmap, dstRect: RectF, outRect: Rect) {
        val scale = max(
            dstRect.width() / bitmap.width.toFloat(),
            dstRect.height() / bitmap.height.toFloat()
        )
        val scaledWidth = dstRect.width() / scale
        val scaledHeight = dstRect.height() / scale
        val left = ((bitmap.width - scaledWidth) / 2f).toInt().coerceAtLeast(0)
        val top = ((bitmap.height - scaledHeight) / 2f).toInt().coerceAtLeast(0)
        val right = (left + scaledWidth.toInt()).coerceAtMost(bitmap.width)
        val bottom = (top + scaledHeight.toInt()).coerceAtMost(bitmap.height)
        outRect.set(left, top, right, bottom)
    }
}
