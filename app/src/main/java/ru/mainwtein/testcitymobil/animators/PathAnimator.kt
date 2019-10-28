package ru.mainwtein.testcitymobil.animators

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Path
import android.graphics.PathMeasure
import android.view.animation.AccelerateDecelerateInterpolator
import ru.mainwtein.testcitymobil.models.Car
import java.lang.Math.toDegrees
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sign

class PathAnimator(private val movementListener: OnMovementListener) {

    companion object {
        private const val CAR_MOVEMENT_SPEED = 450f
    }


    fun getMovingAnimation(path: Path, car: Car): Animator {
        val startX = car.location.x
        val startY = car.location.y
        movementListener.onDrawPath(path)

        val pathMeasure = PathMeasure(path, false)
        val pathLength = pathMeasure.length
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = (pathLength / CAR_MOVEMENT_SPEED * 1000f).toLong()
        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {

            private val startingDeviation = 0.005f
            private val nextPositions = FloatArray(2)
            private val lastPositions = floatArrayOf(startX, startY)

            override fun onAnimationUpdate(animation: ValueAnimator) {
                pathMeasure.getPosTan(
                    pathLength * animation.animatedFraction,
                    nextPositions,
                    null
                )
                if (abs(nextPositions[0] - lastPositions[0]) > startingDeviation
                    || abs(nextPositions[1] - lastPositions[1]) > startingDeviation) {
                    val thetaAngle = atan2(
                        (lastPositions[0] - nextPositions[0]).toDouble(),
                        (lastPositions[1] - nextPositions[1]).toDouble()
                    )

                    lastPositions[0] = nextPositions[0]
                    lastPositions[1] = nextPositions[1]

                    var angle = toDegrees(thetaAngle) * -1f
                    if (abs(angle) > 180) {
                        angle = (360 - abs(angle)) * sign(angle) * -1f
                    }

                    movementListener.onPositionChanged(
                        nextPositions[0],
                        nextPositions[1],
                        angle
                    )
                }
            }
        })

        return animator
    }
}