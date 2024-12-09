package com.snakecase.pomodoro

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.snakecase.DataBaseManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val _registrandoUsuario = MutableLiveData(false)

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _contrasenia = MutableLiveData<String>()
    val contrasenia : LiveData<String> = _contrasenia

    private val _permitirLogin = MutableLiveData<Boolean>()
    val permitirLogin: LiveData<Boolean> = _permitirLogin

    private var posicionUsuario: Int? = null

    fun obtenerUserName(): String{
        return email.value?.substringBefore("@") ?: "guest"
    }


    fun cambioEnCampos(email: String, contrasenia: String){
        _email.value = email
        _contrasenia.value = contrasenia
        _permitirLogin.value = emailValido(email) && contraseniaValida(contrasenia)
    }

    fun emailValido(email: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    } //esto basicamente compara el mail con los patrones habituales ya creados


    fun contraseniaValida(contrasenia: String): Boolean{
        return true
    }

    fun iniciarSesion(navController : NavHostController, context : Context) {
        val email = _email.value ?: return
        val contrasenia = _contrasenia.value ?: return

        viewModelScope.launch {
            try {

                auth.signInWithEmailAndPassword(email, contrasenia).await()
                crearPosicionLeaderBoard()
                navController.navigate("pantallaPrincipal")
            } catch (exception: Exception) {
                Log.d("AuthViewModel", "Error al iniciar sesión: ${exception.message}")

                when (exception) {
                    is FirebaseAuthInvalidUserException -> {
                        Log.d("No se pudo iniciar sesion, usuario no encontrado", "${exception.message}")
                        Toast.makeText(context, "Error de Inicializacion", Toast.LENGTH_LONG).show()
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        Log.d("No se pudo iniciar sesión", "Credenciales inválidas: ${exception.message}")
                        Toast.makeText(context, "Error de Inicializacion", Toast.LENGTH_LONG).show()
                    }
                    else -> { Log.d("no se pudo iniciar sesion", "${exception.message}")
                        Toast.makeText(context, "Error de Inicializacion", Toast.LENGTH_LONG).show()}
                }
            }

        }


    }

    fun registrarUsuario(navController : NavHostController, context: Context){
        val email = _email.value ?: return
        val contrasenia = _contrasenia.value ?: return

        try {
            if(!_registrandoUsuario.value!!) {
                auth.createUserWithEmailAndPassword(email, contrasenia)
                GlobalScope.launch{

                    crearPosicionLeaderBoard()
                }
                navController.navigate("pantallaPrincipal")
            }

        } catch(exception : Exception) {
            Log.d("AuthViewModel", "Error al iniciar sesión: ${exception.message}")

            when (exception) {
                is FirebaseAuthUserCollisionException -> {
                    Log.d("No se pudo registrar", "${exception.message}")
                    Toast.makeText(context, "El usuario ya esta registrado", Toast.LENGTH_LONG).show()
                }
                else -> { Log.d("no se pudo iniciar sesion", "${exception.message}")
                    Toast.makeText(context, "Error de Registracion", Toast.LENGTH_LONG).show()}
            }
        }
    }

     suspend fun crearPosicionLeaderBoard() {

         posicionUsuario = consultarPosicionActual()

    }

    suspend fun consultarPosicionActual(): Int{
        val nombreUsuario = this.obtenerUserName()
        val  db = DataBaseManager(nombreUsuario)
        var posicion = 0

        val leaderboard = db.obtenerLeaderBoard()
        var contar = true
        val datalist = leaderboard.toList().sortedByDescending { it.second }

        for((clave,valor) in datalist){
            if(clave != nombreUsuario && contar){

                posicion = posicion + 1
            }
            else{
                contar = false
            }
        }
        return posicion

    }

    fun obtenerPosicionInicialLeaderBoard(): Int{
        return posicionUsuario ?: 0

    }
}