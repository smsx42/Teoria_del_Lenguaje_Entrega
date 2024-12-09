package com.snakecase.pomodoro

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.snakecase.DataBaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class VentanaPrincipal : Ventana{

    override var colorVentana = Ventana.ColorVentana(Color.White)
    override var brilloVentana = Ventana.BrilloVentana(1F)
    override var timerPomodoro = Pomodoro()
    override var colorTexto = Ventana.ColorTexto(Color.Black)
    override var colorVentanaConfiguracion = Ventana.ColorVentana(Color.White)
    override var idImagenPrincipalPomodoro = Ventana.IdImagenPomodoro(R.drawable.tomate_study)
    override var idAudio = Ventana.IdAudioPomodoro(R.raw.bubblegum)
    var cantidadCiclosInicial = 0

    @Composable
    fun CrearBoton(
        context: Context,
        estado: String,
        idImagen: Int,
        descripcion: String,
        onClick: () -> Unit
    ) {
        IconButton(
            onClick = {
                onClick()
                Toast.makeText(context, estado, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .padding(8.dp)
                .size(80.dp)
        ) {
            Image(
                painter = painterResource(id = idImagen),
                contentDescription = descripcion,
                modifier = Modifier.size(80.dp)
            )
        }
    }

    @Composable
    fun CrearBotones(context: Context, viewModel: LoginViewModel) {
        var minutos by remember { mutableIntStateOf(timerPomodoro.obtenerMinutos()) }
        var segundos by remember { mutableIntStateOf(timerPomodoro.obtenerSegundos()) }
        var timerActivo by remember { mutableStateOf(!timerPomodoro.enPausa()) }
        val reproductorSonido = MediaPlayer.create(context, R.raw.bubblegum)

        LaunchedEffect(timerActivo) {
            if (timerActivo) {
                while (minutos >= 0 || segundos >= 0) {
                    delay(1000)
                    withContext(Dispatchers.Main) {
                        timerPomodoro.pasa1Segundo(viewModel , context)
                        minutos = timerPomodoro.obtenerMinutos()
                        segundos = timerPomodoro.obtenerSegundos()
                        if (minutos == 0 && segundos == 0) {
                            timerPomodoro.pasa1Segundo(viewModel, context)
                            minutos = timerPomodoro.obtenerMinutos()
                            segundos = timerPomodoro.obtenerSegundos()
                            timerPomodoro.pausar()
                            timerActivo = timerPomodoro.enPausa()
                            reproductorSonido.start()
                        }
                    }
                }
            }
        }

        fun pausarTimer() {
            timerPomodoro.pausar()
            timerActivo = timerPomodoro.enPausa()
        }

        fun reanudarTimer() {
            timerPomodoro.reanudar()
            timerActivo = timerPomodoro.enPausa()
        }

        fun reiniciarTimer() {
            timerPomodoro.reiniciar()
            timerPomodoro.pausar()
            timerActivo = timerPomodoro.enPausa()
            minutos = timerPomodoro.obtenerMinutos()
            segundos = timerPomodoro.obtenerSegundos()
        }

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 100.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${minutos.toString().padStart(2, '0')}:",
                        fontSize = 70.sp,
                        fontFamily = vt323FontFamily,
                        color = colorTexto.getColorTexto()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = segundos.toString().padStart(2, '0'),
                        fontSize = 70.sp,
                        fontFamily = vt323FontFamily,
                        color = colorTexto.getColorTexto()
                    )
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 300.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                CrearBoton(
                    context = context,
                    estado = if (timerActivo) "Pausado" else "Iniciado",
                    idImagen = if (timerActivo) R.drawable.pause else R.drawable.play,
                    descripcion = "Botón de Play/Pausa",
                    onClick = {
                        if (timerActivo) pausarTimer() else reanudarTimer()
                    }
                )

                CrearBoton(
                    context = context,
                    estado = "Reiniciado",
                    idImagen = R.drawable.backwards,
                    descripcion = "Botón de Reiniciar",
                    onClick = {
                        reiniciarTimer()
                    }
                )
            }
        }
    }

    @Composable
    fun CrearImagenTomate() {

        Image(
            painter = painterResource(id = idImagenPrincipalPomodoro.getImagen()),
            contentDescription = "Tomate",
            modifier = Modifier.size(750.dp)
        )

    }

    @Composable
    fun CrearBotonConfiguracion(navController : NavHostController) {
        IconButton(
            onClick =  {navController.navigate("pantallaConfiguracion")},
            modifier = Modifier
                .padding(top = 50.dp, end = 16.dp)
                .size(80.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.settingicon),
                contentDescription = "Imagen de Configuracion",
                modifier = Modifier.size(80.dp)
            )
        }
    }

    @Composable
    fun CrearBotonLeaderBoard(navController: NavHostController){
        IconButton(onClick = { navController.navigate("pantallaLeaderBoard") },
            modifier = Modifier
                .offset(x=300.dp).padding(top = 41.dp, end = 16.dp)
                .size(100.dp)) {
            Image(painter = painterResource(id = R.drawable.leaderboard3), contentDescription ="Imagen de leaderBoard", modifier = Modifier.size(65.dp))

        }
    }

    @Composable
    fun PantallaPrincipal(navController: NavHostController, colorVentanaAux : Ventana.ColorVentana, brilloVentanaAux : Ventana.BrilloVentana,
                          timerPomodoroAux : Pomodoro, colorTextoAux : Ventana.ColorTexto, colorVentanaConfiguracionAux : Ventana.ColorVentana,
                          idImagenPrincipalPomodoroAux : Ventana.IdImagenPomodoro, idAudioAux : Ventana.IdAudioPomodoro, viewModel: LoginViewModel
    ) {
        setearParametros(colorVentanaAux, brilloVentanaAux, timerPomodoroAux, colorTextoAux, colorVentanaConfiguracionAux, idImagenPrincipalPomodoroAux, idAudioAux)
        PomodoroTheme {
            val context = LocalContext.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBrightnessOverlay(brilloVentana.getBrillos()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(color = colorVentana.getColorVentana())
                ) {
                    CrearBotonConfiguracion(navController)
                    CrearBotonLeaderBoard(navController)
                    CrearImagenTomate()
                    Spacer(modifier = Modifier.height(30.dp))
                    CrearBotones(context, viewModel)

                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }


}