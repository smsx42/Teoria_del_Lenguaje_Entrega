package com.snakecase.pomodoro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

class VentanaPersonalizacion : Ventana {

    override var colorVentana = Ventana.ColorVentana(Color.White)
    override var brilloVentana = Ventana.BrilloVentana(1F)
    override var timerPomodoro = Pomodoro(TipoTimer.ESTUDIO)
    override var colorTexto = Ventana.ColorTexto(Color.Black)
    override var colorVentanaConfiguracion = Ventana.ColorVentana(Color.White)
    override var idImagenPrincipalPomodoro = Ventana.IdImagenPomodoro(R.drawable.tomate_study)
    override var idAudio = Ventana.IdAudioPomodoro(R.raw.bubblegum)

    @Composable
    fun CrearBotonModoOscuro() {

        var activado by remember { mutableStateOf(false)}

        Spacer(modifier = Modifier.size(30.dp))

        if (!activado) {
            Button(onClick = {
                colorTexto.setColorTexto(Color.White)
                colorVentanaConfiguracion.setColorVentana(Color.Black)
                activado = true
            }, shape = RectangleShape, modifier = Modifier
                .offset(x = 5.dp)
                .border(BorderStroke(4.dp, color = Color.Gray)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)) {
                Text("Modo Oscuro                           >>", fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 20.sp))
            }
        }
        if (activado) {
            Button(onClick = {
                colorTexto.setColorTexto(Color.Black)
                colorVentanaConfiguracion.setColorVentana(Color.White)
                activado = false
            }, shape = RectangleShape, modifier = Modifier.offset(x = 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)){

                Text("Modo Oscuro                           >>", fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 20.sp))

            }

        }

    }

    @Composable
    fun ModificarBrilloAplicacion() {

        var color_aux = colorVentana.getColorVentana()
        if(colorVentana.getColorVentana() == Color.White){
            color_aux = Color.Gray
        }

        Text(
            text = "Ajustar Brillo",
            style = TextStyle(
                fontFamily = vt323FontFamily,
                fontSize = 18.sp,
                color = colorTexto.getColorTexto()
            )
        )
        Slider(
            value = brilloVentana.getBrillos(),
            onValueChange = { brilloVentana.setBrillos(it) },
            valueRange = 0.1f..1f,
            steps = 100,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) { detectTransformGestures { _, _, _, _ -> } },
            colors = SliderDefaults.colors(thumbColor = color_aux,
                activeTrackColor = color_aux)
        )
        Text(
            text = "Brillo Actual: ${(brilloVentana.getBrillos() * 100).toInt()} %",
            style = TextStyle(
                fontFamily = vt323FontFamily,
                fontSize = 16.sp,
                color = colorTexto.getColorTexto()
            )
        )
    }

    @Composable
    fun CrearBotonesColores(onColorSelected: (Color) -> Unit) {
        val colores = listOf(
            Color(0xFFFFFFFF), Color(0xFF81C784), Color(0xFFFFC0CB), Color(0xFFFFD54F),
            Color(0xFFE6CCFF), Color(0xFFFFB74D), Color(0xFF4DB6AC), Color(0xFF7986CB),
            Color(0xFF4DD0E1), Color(0xFFAED581), Color(0xFF9575CD), Color(0xFF64B5F6)
        )

        var selectedColor by remember { mutableStateOf<Color?>(null) }

        Column {
            repeat(4) { rowIndex ->
                Row {
                    repeat(3) { colIndex ->
                        val colorIndex = rowIndex * 3 + colIndex
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(colores[colorIndex])
                                .clickable {
                                    selectedColor = colores[colorIndex]
                                    onColorSelected(colores[colorIndex])
                                }
                                .border(BorderStroke(3.dp, color = Color.Gray))
                        ) {
                            if (selectedColor == colores[colorIndex]) {
                                Text("✔️", color = Color.White, modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun crearBotonCambioImagen(navController : NavHostController) {
        Button(
            onClick = { navController.navigate("pantallaCambioImagen") },
            modifier = Modifier
                .offset(x = 5.dp)
                .border(BorderStroke(4.dp, color = Color.Gray)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
            shape = RectangleShape
        ) {
            Text("Cambiar Icono Del Pomodoro            >>",
                style = TextStyle(fontSize = 20.sp), fontFamily = vt323FontFamily)
        }
    }

    @Composable
    fun crearBotonCambioTono(navController: NavHostController) {
        Button(
            onClick = { navController.navigate("pantallaCambioAudio") },
            modifier = Modifier
                .offset(x = 5.dp)
                .border(BorderStroke(4.dp, color = Color.Gray)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
            shape = RectangleShape
        ) {
            Text("Cambiar Audio                         >>",
                style = TextStyle(fontSize = 20.sp), fontFamily = vt323FontFamily)
        }
    }

    @Composable
    fun PantallaSeleccionColor(navController : NavHostController, colorVentanaAux : Ventana.ColorVentana, brilloVentanaAux : Ventana.BrilloVentana,
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
                Button(onClick = {navController.navigate("pantallaConfiguracion")}, colors = ButtonDefaults.buttonColors(contentColor = Color.Black, containerColor = Color.Transparent),
                    modifier = Modifier.offset(x = -15.dp, y = -60.dp)) {
                    Text("<< Configuración",modifier = Modifier.offset(y = 0.dp) ,color = colorTexto.getColorTexto(), fontFamily = vt323FontFamily,
                        style = TextStyle(fontSize = 18.sp)
                    )
                }
                Text(text = "Personalización", modifier = Modifier.offset(x = 140.dp, y = -50.dp), color = colorTexto.getColorTexto(),
                    fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 18.sp)
                )
                Column(modifier = Modifier
                    .padding(8.dp)
                    .offset(x = 0.dp, y = -20.dp)) {
                    ModificarBrilloAplicacion()
                    CrearBotonModoOscuro()
                    Spacer(modifier = Modifier.size(30.dp))
                    crearBotonCambioImagen(navController)
                    Spacer(modifier = Modifier.size(30.dp))
                    crearBotonCambioTono(navController)
                }
            }

            Text(text = "  Color del fondo del pomodoro  ", color = Color.Black,
                modifier = Modifier
                    .offset(x = 6.dp)
                    .border(2.dp, color = Color.Gray),
                fontFamily = vt323FontFamily,
                style = TextStyle(fontSize = 25.sp, background = Color.LightGray),
            )
            Spacer(modifier = Modifier.size(30.dp))
            CrearBotonesColores { color ->
                colorVentana.setColorVentana(color)
            }

            Spacer(modifier = Modifier.size(30.dp))

            Button(onClick ={ navController.navigate("PantallaPrincipal")},
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
                border = BorderStroke(4.dp, Color.Gray)
            ) {
                Text("Confirmar Color", fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 20.sp),)
            }

        }

    }
}