package ru.mainwtein.testcitymobil.views

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import ru.mainwtein.testcitymobil.utils.showShortToast


class SignpostView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        const val DEFAULT_RADIUS = 100f
    }

    private var path: Path? = null
    private var circleTapPaint = Paint()
    private var pathPaint = Paint()
    private var newX = -1f
    private var newY = -1f
    private var radius = DEFAULT_RADIUS
    private var isDebugMode = false


    init {
        circleTapPaint.color = Color.GREEN
        circleTapPaint.style = Paint.Style.FILL
        pathPaint.color = Color.BLACK
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeWidth = 3f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (newX != -1f && newY != -1f) {
            canvas.drawCircle(newX, newY, radius, circleTapPaint)
            if (radius == DEFAULT_RADIUS) {
                circleTapPaint.alpha = 0
            }
        }
        path?.let {
            if (isDebugMode) {
                canvas.drawPath(it, pathPaint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            newX = event.x
            newY = event.y
            val anim = ValueAnimator.ofFloat(0f, 10f)
            anim.duration = 300
            anim.interpolator = LinearInterpolator()
            anim.addUpdateListener {
                radius = DEFAULT_RADIUS - DEFAULT_RADIUS / 10 * (10f - it.animatedValue as Float)
                circleTapPaint.alpha = (255f - 25 * (it.animatedValue as Float) - 5).toInt()
                invalidate()
            }
            anim.start()
        }
        return false
    }

    fun drawPath(path: Path) {
        this.path = path
    }

    fun switchDebugMode() {
        isDebugMode = !isDebugMode

        showShortToast("Debug mode ${if (isDebugMode) "enabled" else "disabled"}")
        invalidate()
    }
}
