package ru.mainwtein.testcitymobil.utils

import android.graphics.Path
import ru.mainwtein.testcitymobil.models.Car

interface RouteCalculation {

    fun calculatePath(car: Car, endX: Float, endY: Float): Path
}