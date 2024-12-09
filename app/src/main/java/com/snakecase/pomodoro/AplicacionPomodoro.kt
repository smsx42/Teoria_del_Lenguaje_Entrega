package com.snakecase.pomodoro


import android.os.Bundle

import androidx.compose.runtime.Composable


import androidx.compose.ui.graphics.Color

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



class AplicacionPomodoro {

    val ventanaPrincipal = VentanaPrincipal()
    val ventanaConfiguracion = VentanaConfiguracion()
    val ventanaPersonalizacion = VentanaPersonalizacion()
    val ventanaCambioImagen = VentanaCambioImagen()
    val ventanaCambioAudio = VentanaCambioAudio()
    val loginViewModel = LoginViewModel()

    @Composable
    fun EjecutarAplicacion(savedInstanceState: Bundle?) {


        val navController = rememberNavController()
        val colorVentana = Ventana.ColorVentana(Color.White)
        val brilloVentana = Ventana.BrilloVentana(1F)
        val timerPomodoro = Pomodoro(TipoTimer.ESTUDIO)
        val colorTexto = Ventana.ColorTexto(Color.Black)
        val colorVentanaConfiguracion = Ventana.ColorVentana(Color.White)
        val idImagenPrincipalPomodoro = Ventana.IdImagenPomodoro(R.drawable.tomate_study)
        val idAudio = Ventana.IdAudioPomodoro(R.raw.bubblegum)


        NavHost(navController = navController, startDestination = "login") {
            composable("pantallaPrincipal") { ventanaPrincipal.PantallaPrincipal(navController, colorVentana, brilloVentana, timerPomodoro, colorTexto, colorVentanaConfiguracion, idImagenPrincipalPomodoro, idAudio, loginViewModel)}
            composable("pantallaColores") { ventanaPersonalizacion.PantallaSeleccionColor(navController, colorVentana, brilloVentana, timerPomodoro, colorTexto, colorVentanaConfiguracion, idImagenPrincipalPomodoro, idAudio) }
            composable("pantallaConfiguracion") {ventanaConfiguracion.PantallaConfiguracion(navController, colorVentana, brilloVentana, timerPomodoro, colorTexto, colorVentanaConfiguracion, idImagenPrincipalPomodoro, idAudio)}
            composable("pantallaCambioImagen") {ventanaCambioImagen.PantallaCambioImagen(navController, colorVentana, brilloVentana, timerPomodoro, colorTexto, colorVentanaConfiguracion, idImagenPrincipalPomodoro, idAudio)}
            composable("pantallaCambioAudio") {ventanaCambioAudio.PantallaCambioAudio(navController, colorVentana, brilloVentana, timerPomodoro, colorTexto, colorVentanaConfiguracion, idImagenPrincipalPomodoro, idAudio)}
            composable("login") { PantallaLogin(navController, loginViewModel)}
            composable("registrarUsuario") { PantallaRegistrarUsuario(navController, loginViewModel)}
            composable("pantallaLeaderBoard"){ PantallaLeaderBoard(navController, loginViewModel)}
        }
    }

}

