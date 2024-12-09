package com.snakecase.pomodoro.utils
import com.snakecase.pomodoro.Pomodoro
import com.snakecase.pomodoro.TipoTimer
import org.junit.Test

import org.junit.Assert.*
class PomodoroTest{

    @Test
    fun pasaUnSegundo(){
        val pomodoro = Pomodoro(TipoTimer.ESTUDIO)
        val minutosEsperados = 24
        val segundosEsperados = 59
        pomodoro.pasa1Segundo()

        assertEquals(pomodoro.minutosRestantes , minutosEsperados)
        assertEquals(pomodoro.segundosRestantes,segundosEsperados)
    }
}