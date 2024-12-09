package com.snakecase.pomodoro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer


interface Ventana {

    var colorVentana : ColorVentana
    var brilloVentana : BrilloVentana
    var timerPomodoro : Pomodoro
    var colorTexto : ColorTexto
    var colorVentanaConfiguracion : ColorVentana
    var idImagenPrincipalPomodoro : IdImagenPomodoro
    var idAudio : IdAudioPomodoro

    data class ColorVentana(private var colorConstructor : Color) {

        private var color by mutableStateOf(colorConstructor)

        fun setColorVentana(nuevoColor : Color){
            color = nuevoColor
        }

        fun getColorVentana() : Color {
            return color
        }
    }

    data class BrilloVentana(var valor : Float) {

        private var brillo by  mutableFloatStateOf(valor)

        fun setBrillos(nuevoBrillo : Float){
            brillo = nuevoBrillo
        }

        fun getBrillos() : Float {
            return brillo
        }

    }

    data class ColorTexto(private var colorTexto : Color) {

        fun setColorTexto(nuevoColorTexto : Color){
            colorTexto = nuevoColorTexto
        }

        fun getColorTexto() : Color {
            return colorTexto
        }

    }

    data class IdImagenPomodoro(private var idImagen : Int) {

        fun setImagen(nuevaImagen : Int){
            idImagen = nuevaImagen
        }

        fun getImagen() : Int {
            return idImagen
        }
    }

    data class IdAudioPomodoro(private var idAudio : Int) {

        fun setAudio(nuevoAuido : Int) {
            idAudio = nuevoAuido
        }

        fun getAudio() : Int {
            return idAudio
        }
    }

    fun Modifier.drawBrightnessOverlay(brightness: Float): Modifier = this.then(
        Modifier.graphicsLayer(alpha = brightness)
    )

    fun setearParametros(colorVentanaAux : ColorVentana, brilloVentanaAux : BrilloVentana,
                         timerPomodoroAux : Pomodoro, colorTextoAux : ColorTexto, colorVentanaConfiguracionAux : ColorVentana,
                         idImagenPrincipalPomodoroAux : IdImagenPomodoro, idAudioAux : IdAudioPomodoro) {
        colorVentana = colorVentanaAux
        brilloVentana = brilloVentanaAux
        timerPomodoro = timerPomodoroAux
        colorTexto = colorTextoAux
        colorVentanaConfiguracion = colorVentanaConfiguracionAux
        idImagenPrincipalPomodoro = idImagenPrincipalPomodoroAux
        idAudio = idAudioAux
    }

    //var colorVentana = ColorVentana(Color.White)
   // var brilloVentana = BrilloVentana(1F)
   // var timerPomodoro = Pomodoro(TipoTimer.ESTUDIO)
   // var colorTexto = ColorTexto(Color.Black)
   // var colorVentanaConfiguracion = ColorVentana(Color.White)
   // var idImagenPrincipalPomodoro = IdImagenPomodoro(R.drawable.tomate_study)
   // var idAudio = IdAudioPomodoro(R.raw.alarma1)

}