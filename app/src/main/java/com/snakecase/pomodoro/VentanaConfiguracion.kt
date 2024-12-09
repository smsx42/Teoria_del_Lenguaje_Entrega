package com.snakecase.pomodoro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

class VentanaConfiguracion() : Ventana {

    override var colorVentana = Ventana.ColorVentana(Color.White)
    override var brilloVentana = Ventana.BrilloVentana(1F)
    override var timerPomodoro = Pomodoro(TipoTimer.ESTUDIO)
    override var colorTexto = Ventana.ColorTexto(Color.Black)
    override var colorVentanaConfiguracion = Ventana.ColorVentana(Color.White)
    override var idImagenPrincipalPomodoro = Ventana.IdImagenPomodoro(R.drawable.tomate_study)
    override var idAudio = Ventana.IdAudioPomodoro(R.raw.bubblegum)

    @Composable
    fun ConfigSlider(label: String, initialValue: Int, onValueChange: (Int) -> Unit, range: IntRange, step: Int) {

        var color_aux = colorVentana.getColorVentana()
        if(colorVentana.getColorVentana() == Color.White){
            color_aux = Color.Gray
        }

        var value by remember { mutableStateOf(initialValue) }
        Column {
            Text(text = "$label: $value", color = colorTexto.getColorTexto(),
                fontSize = 20.sp,fontFamily = vt323FontFamily)
            Slider(
                value = value.toFloat(),
                onValueChange = { newValue ->
                    value = newValue.toInt()
                    onValueChange(value)
                },
                colors = SliderDefaults.colors(thumbColor = color_aux,
                    activeTrackColor = color_aux),
                valueRange = range.first.toFloat()..range.last.toFloat(),
                steps = (range.last - range.first) / step - 1
            )
        }
    }

    @Composable
    fun PantallaConfiguracion(navController: NavHostController, colorVentanaAux : Ventana.ColorVentana, brilloVentanaAux : Ventana.BrilloVentana,
                              timerPomodoroAux : Pomodoro, colorTextoAux : Ventana.ColorTexto, colorVentanaConfiguracionAux : Ventana.ColorVentana,
                              idImagenPrincipalPomodoroAux : Ventana.IdImagenPomodoro, idAudioAux : Ventana.IdAudioPomodoro
    ) {
        setearParametros(colorVentanaAux, brilloVentanaAux, timerPomodoroAux, colorTextoAux, colorVentanaConfiguracionAux, idImagenPrincipalPomodoroAux, idAudioAux)
        Column(
            modifier = Modifier
                .padding(top = 25.dp)
                .fillMaxSize()
                .drawBrightnessOverlay(brilloVentana.getBrillos())
                .background(color = colorVentanaConfiguracion.getColorVentana()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = { navController.navigate("pantallaPrincipal") },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.Black, containerColor = Color.Transparent),
                    modifier = Modifier.offset(x = -160.dp, y = -40.dp)
                ) {
                    Text("  << Volver", color = colorTexto.getColorTexto(), fontFamily = vt323FontFamily,
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
                Text(
                    text = "Configuración",
                    modifier = Modifier.offset(x = 0.dp, y = -30.dp),
                    style = TextStyle(
                        fontFamily = vt323FontFamily,
                        fontSize = 20.sp,
                        color = colorTexto.getColorTexto()
                    )
                )
            }

            Spacer(modifier = Modifier.size(30.dp))


            Column(modifier = Modifier.padding(16.dp)) {
                ConfigSlider("Ciclos", timerPomodoro.cicloConteo,timerPomodoro::updateFocusCount, 1..12, 1)
                ConfigSlider("Estudio Time", timerPomodoro.estudioTime, timerPomodoro::updateFocusTime, 5..120, 5)
                ConfigSlider("Descanso Time", timerPomodoro.descansoTime, timerPomodoro::updateBreakTime, 5..60, 5)
                ConfigSlider("Descanso Largo Time", timerPomodoro.descansoLargoTime, timerPomodoro::updateLongBreakTime, 5..60, 5)
            }

            Spacer(modifier = Modifier.size(30.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Button(
                    onClick = { navController.navigate("pantallaColores") },
                    modifier = Modifier
                        .offset(x = 10.dp, y = 0.dp)
                        .border(BorderStroke(4.dp, color = Color.Gray)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
                    shape = RectangleShape
                ) {
                    Text("Personalización                   >>",
                        style = TextStyle(fontSize = 23.sp), fontFamily = vt323FontFamily)
                }
            }
        }
    }
}