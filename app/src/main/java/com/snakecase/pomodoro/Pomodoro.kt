package com.snakecase.pomodoro

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import com.snakecase.DataBaseManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class Pomodoro(private var tipoTimer: TipoTimer = TipoTimer.ESTUDIO) {
    private var ciclos = 0
    var minutosRestantes = tipoTimer.minutos
    var segundosRestantes = 0
    private var pausa = true

    var cicloConteo by mutableIntStateOf(4)
    var estudioTime by mutableIntStateOf(25)
    var descansoTime by mutableIntStateOf(5)
    var descansoLargoTime by mutableIntStateOf(20)


    fun obtenerTipoTimer(): TipoTimer {
        return tipoTimer
    }

    fun actualizarTipoTimer(viewModel: LoginViewModel, context: Context) {
        if (ciclos < cicloConteo) {
            if (tipoTimer == TipoTimer.ESTUDIO) {
                incrementarCiclo(viewModel, context)
                tipoTimer = TipoTimer.DESCANSOCORTO
            } else {
                tipoTimer = TipoTimer.ESTUDIO
            }
        } else {
            tipoTimer = TipoTimer.DESCANSOLARGO
            ciclos = 0
        }
        actualizarMinutosRestantes()
    }

    fun incrementarCiclo(viewModel: LoginViewModel, context: Context) {
        val nombreUsuario = viewModel.obtenerUserName()
        val multiplicador = calcularMultiplicador(viewModel)
        ciclos = ciclos + 1
        if(nombreUsuario != "guest"){
            val DBManager = DataBaseManager(nombreUsuario)
            DBManager.incrementarCiclos(context, multiplicador)
        }
    }

    fun calcularMultiplicador(viewModel: LoginViewModel): Int{
        var posicionesEscaladas = 0
        var multiplicador = 1

        GlobalScope.launch {
            posicionesEscaladas = viewModel.obtenerPosicionInicialLeaderBoard() - viewModel.consultarPosicionActual()
            if(posicionesEscaladas > 0){
                when{
                    posicionesEscaladas > 16 -> multiplicador = 4
                    posicionesEscaladas > 8 -> multiplicador = 3
                    posicionesEscaladas > 4 -> multiplicador = 2
                }
            }
        }
        return multiplicador


    }

    fun pasa1Segundo(viewModel: LoginViewModel, context: Context) {
        if (segundosRestantes > 0) {
            segundosRestantes -= 1
        } else if (minutosRestantes > 0) {
            minutosRestantes -= 1
            segundosRestantes = 59
        } else {
            actualizarTipoTimer(viewModel, context)
        }
    }

    fun pausar() {
        this.pausa = false
    }

    fun reanudar() {
        this.pausa = true
    }

    fun reiniciar() {
        this.segundosRestantes = 0
        actualizarMinutosRestantes()
    }

    fun enPausa(): Boolean {
        return pausa
    }

    fun obtenerMinutos(): Int {
        return this.minutosRestantes
    }

    fun obtenerSegundos(): Int {
        return this.segundosRestantes
    }

    fun setearMinutos(minutos: Int) {
        this.minutosRestantes = minutos
    }

    fun updateFocusCount(nuevoConteo: Int) {
        if (nuevoConteo in 1..12) {
            cicloConteo = nuevoConteo
        }
    }

    fun updateFocusTime(nuevoTime: Int) {
        if (nuevoTime in 5..120) {
            estudioTime = nuevoTime
            if (tipoTimer == TipoTimer.ESTUDIO) actualizarMinutosRestantes()
        }
    }

    fun updateBreakTime(nuevoTime: Int) {
        if (nuevoTime in 5..60) {
            descansoTime = nuevoTime
            if (tipoTimer == TipoTimer.DESCANSOCORTO) actualizarMinutosRestantes()
        }
    }

    fun updateLongBreakTime(nuevoTime: Int) {
        if (nuevoTime in 5..60) {
            descansoLargoTime = nuevoTime
            if (tipoTimer == TipoTimer.DESCANSOLARGO) actualizarMinutosRestantes()
        }
    }

    private fun actualizarMinutosRestantes() {
        minutosRestantes = when (tipoTimer) {
            TipoTimer.ESTUDIO -> estudioTime
            TipoTimer.DESCANSOCORTO -> descansoTime
            TipoTimer.DESCANSOLARGO -> descansoLargoTime
        }
    }
}