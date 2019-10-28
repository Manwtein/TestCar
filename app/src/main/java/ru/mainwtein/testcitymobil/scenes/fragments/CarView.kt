package ru.mainwtein.testcitymobil.scenes.fragments

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.mainwtein.testcitymobil.models.Car

interface CarView: MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun startMoving(car: Car, endX: Float, endY: Float)
}