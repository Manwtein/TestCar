package ru.mainwtein.testcitymobil.utils

import android.graphics.Path
import android.graphics.PointF
import androidx.core.graphics.minus
import androidx.core.graphics.plus
import ru.mainwtein.testcitymobil.models.Car
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.sqrt

class PathCalculator : RouteCalculation {

    companion object {
        private const val TURN_SPEED = 0.01f
        private const val RADIUS = TURN_SPEED * 10_000
        private const val DIAMETER = RADIUS * 2
        private const val MIN_DEVIATION = 0.05f
        private const val MIN_DISTANCE_DESTINATION = 3f
    }

    private var currentPosition = PointF(0f, 0f)
    private var speedVector = PointF(1f, 0f)
    private var shiftedPosition = currentPosition + speedVector
    private val endPosition = PointF(0f, 0f)
    private var isMoving = false


    override fun calculatePath(car: Car, endX: Float, endY: Float): Path {
        val startX = car.location.x
        val startY = car.location.y
        val path = Path()
        path.moveTo(startX, startY)
        isMoving = true

        currentPosition.x = startX
        currentPosition.y = startY
        endPosition.x = endX
        endPosition.y = endY

        checkDeadRadius(path)

        while (isMoving) {
            shiftedPosition = currentPosition + speedVector

            val difference = getDifferenceBetweenPoints()
            if (abs(difference) < MIN_DEVIATION) {
                path.lineTo(endX, endY)
                break
            } else {
                val turnDirection = if (difference > 0) 1 else -1
                addTurn(turnDirection)
                val vectorDistance = PointF(speedVector.x, speedVector.y)
                currentPosition += vectorDistance
            }
 
            if ((currentPosition - endPosition).length() < MIN_DISTANCE_DESTINATION) {
                isMoving = false
            }
            path.lineTo(currentPosition.x, currentPosition.y)
        }

        return path
    }

    private fun checkDeadRadius(path: Path) {
        val startingDifference = getDifferenceBetweenPoints()
        val turnDirection2 = if (startingDifference > 0) 1 else -1

        var x2 = -speedVector.y
        var y2 = speedVector.x
        val length = sqrt(x2 * x2 + y2 * y2)
        x2 /= length
        y2 /= length
        var perpendicular = PointF(x2, y2)
        perpendicular.x /= perpendicular.length() * turnDirection2
        perpendicular.x *= RADIUS
        perpendicular.y /= perpendicular.length() * turnDirection2
        perpendicular.y *= RADIUS

        perpendicular += currentPosition

        if ((perpendicular - endPosition).length() <= DIAMETER) {
            currentPosition.x += speedVector.x / speedVector.length() * DIAMETER
            currentPosition.y += speedVector.y / speedVector.length() * DIAMETER
            path.lineTo(currentPosition.x, currentPosition.y)
        }
    }

    private fun getDifferenceBetweenPoints(): Float {
        return (shiftedPosition.x - currentPosition.x) * (endPosition.y - currentPosition.y) - (shiftedPosition.y - currentPosition.y) * (endPosition.x - currentPosition.x)
    }

    private fun addTurn(turnDirection: Int) {
        if (speedVector.x > 0f && speedVector.y >= 0f) {
            if (speedVector.y == 0f) {
                speedVector = PointF(
                    round(speedVector.x - TURN_SPEED, 2),
                    round(speedVector.y + TURN_SPEED * turnDirection, 2)
                )
            } else {
                speedVector = PointF(
                    round(speedVector.x - TURN_SPEED * turnDirection, 2),
                    round(speedVector.y + TURN_SPEED * turnDirection, 2)
                )
            }
            return
        }

        if (speedVector.x <= 0f && speedVector.y > 0f) {
            if (speedVector.x == 0f) {
                speedVector = PointF(
                    round(speedVector.x - TURN_SPEED * turnDirection, 2),
                    round(speedVector.y - TURN_SPEED, 2)
                )
            } else {
                speedVector = PointF(
                    round(speedVector.x - TURN_SPEED * turnDirection, 2),
                    round(speedVector.y - TURN_SPEED * turnDirection, 2)
                )
            }
            return
        }

        if (speedVector.x < 0f && speedVector.y <= 0f) {
            if (speedVector.y == 0f) {
                speedVector = PointF(
                    round(speedVector.x + TURN_SPEED, 2),
                    round(speedVector.y - TURN_SPEED * turnDirection, 2)
                )
            } else {
                speedVector = PointF(
                    round(speedVector.x + TURN_SPEED * turnDirection, 2),
                    round(speedVector.y - TURN_SPEED * turnDirection, 2)
                )
            }
            return
        }

        if (speedVector.x >= 0f && speedVector.y < 0f) {

            if (speedVector.x == 0f) {
                speedVector = PointF(
                    round(speedVector.x + TURN_SPEED * turnDirection, 2),
                    round(speedVector.y + TURN_SPEED, 2)
                )
            } else {
                speedVector = PointF(
                    round(speedVector.x + TURN_SPEED * turnDirection, 2),
                    round(speedVector.y + TURN_SPEED * turnDirection, 2)
                )
            }
        }
    }

    private fun round(
            value: Float,
            @Suppress("SameParameterValue") numbers: Int
    ): Float {
        val countOfNumber = Array(numbers) { 10 }.reduce { total, next -> total * next }
        return round(value * countOfNumber) / (countOfNumber)
    }
}