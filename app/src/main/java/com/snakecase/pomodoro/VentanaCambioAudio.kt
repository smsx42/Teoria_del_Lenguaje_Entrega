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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

class VentanaCambioAudio() : Ventana{

    override var colorVentana = Ventana.ColorVentana(Color.White)
    override var brilloVentana = Ventana.BrilloVentana(1F)
    override var timerPomodoro = Pomodoro(TipoTimer.ESTUDIO)
    override var colorTexto = Ventana.ColorTexto(Color.Black)
    override var colorVentanaConfiguracion = Ventana.ColorVentana(Color.White)
    override var idImagenPrincipalPomodoro = Ventana.IdImagenPomodoro(R.drawable.tomate_study)
    override var idAudio = Ventana.IdAudioPomodoro(R.raw.bubblegum)

    @Composable
    fun CrearBotonIndividualCambioAudio(textoBoton : String, id : Int) {

        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {idAudio.setAudio(id)},
            modifier = Modifier
                .offset(x = -5.dp, y = 0.dp)
                .border(BorderStroke(4.dp, color = Color.Gray)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
            shape = RectangleShape
        ) {
            Text(textoBoton,
                style = TextStyle(fontSize = 23.sp), fontFamily = vt323FontFamily)
        }
    }

    @Composable
    fun crearBotonesCambioAudio() {

        CrearBotonIndividualCambioAudio(textoBoton = "BubbleGum                        >>", id = R.raw.bubblegum)
        CrearBotonIndividualCambioAudio(textoBoton = "CutePop                          >>", id = R.raw.cutepop)
        CrearBotonIndividualCambioAudio(textoBoton = "GhemJanDi                        >>", id = R.raw.ghemjandi)
        CrearBotonIndividualCambioAudio(textoBoton = "LovelyWhisper                    >>", id = R.raw.lovelywhisperer)
        CrearBotonIndividualCambioAudio(textoBoton = "NightCity                        >>", id = R.raw.nightcity)
        CrearBotonIndividualCambioAudio(textoBoton = "ShootingStar                     >>", id = R.raw.shootingstar)
        CrearBotonIndividualCambioAudio(textoBoton = "SleepyCat                        >>", id = R.raw.sleepycat)
    }

    @Composable
    fun PantallaCambioAudio(navController: NavHostController, colorVentanaAux : Ventana.ColorVentana, brilloVentanaAux : Ventana.BrilloVentana,
                            timerPomodoroAux : Pomodoro, colorTextoAux : Ventana.ColorTexto, colorVentanaConfiguracionAux : Ventana.ColorVentana,
                            idImagenPrincipalPomodoroAux : Ventana.IdImagenPomodoro, idAudioAux : Ventana.IdAudioPomodoro
    ) {
        setearParametros(colorVentanaAux, brilloVentanaAux, timerPomodoroAux, colorTextoAux, colorVentanaConfiguracionAux, idImagenPrincipalPomodoroAux, idAudioAux)

        Column(modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxSize()
            .drawBrightnessOverlay(brilloVentana.getBrillos())
            .background(color = colorVentanaConfiguracion.getColorVentana()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
                Button(onClick = {navController.navigate("pantallaColores")},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black),
                    modifier = Modifier.offset(x = -150.dp, y = -50.dp))
                {
                    Text("<< Volver",modifier = Modifier.offset(x = 0.dp,y = 0.dp) ,color = colorTexto.getColorTexto(), fontFamily = vt323FontFamily,
                        style = TextStyle(fontSize = 18.sp)
                    )

                }
                Text("Cambiar Audio",modifier = Modifier.offset(x = 0.dp, y = -40.dp) ,color = colorTexto.getColorTexto(), fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 18.sp)
                )

            }
            Text(text = "  Seleccine Audio del Pomodoro  ", color = Color.Black,
                modifier = Modifier
                    .offset(x = 6.dp, y = -20.dp)
                    .border(2.dp, color = Color.Gray),
                fontFamily = vt323FontFamily,
                style = TextStyle(fontSize = 25.sp, background = Color.LightGray),
            )
            crearBotonesCambioAudio()
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick ={ navController.navigate("PantallaPrincipal")},
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
                border = BorderStroke(4.dp, Color.Gray)
            ) {
                Text("Confirmar Audio", fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 20.sp),)
            }

        }

    }
}