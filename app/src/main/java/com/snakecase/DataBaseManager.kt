package com.snakecase

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


class DataBaseManager(val nombreUsuario: String) {


    fun incrementarCiclos(context: Context, multiplicador: Int) {


        try {

            val actualizacion: Map<String,Any> = hashMapOf(
                "ciclos" to FieldValue.increment(multiplicador.toLong())
            )
            Firebase.firestore.collection("LeaderBoard").document(nombreUsuario).set(actualizacion, SetOptions.merge())
        }catch(exception: Exception){
            Toast.makeText(context, "Hubo un error y no pudimos actualizar tu score :(", Toast.LENGTH_LONG).show()}
    }


    suspend fun obtenerLeaderBoard(): HashMap<String, Int> {
        val leaderBoard: HashMap<String, Int> = HashMap()
        val resultado = Firebase.firestore.collection("LeaderBoard").orderBy("ciclos").get().await()

        for (documento in resultado) {
            leaderBoard[documento.id] = documento.get("ciclos").toString().toInt()
        }
        return leaderBoard

    }
}