package org.utl.dovasystemcompose

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.utl.dovasystemcompose.model.Temperatura

fun buscarTemperaturas(
    criterio: String,
    valor: String,
    onSuccess: (List<Temperatura>) -> Unit,
    onError: (String) -> Unit
) {
    val db = Firebase.database("https://dovasystemcompose-default-rtdb.firebaseio.com")
    val ref = db.getReference("temperatura")

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val lista = mutableListOf<Temperatura>()
            for (post in snapshot.children) {
                val fecha = post.child("fecha").getValue(String::class.java) ?: continue
                val hora = post.child("hora").getValue(String::class.java) ?: ""
                val valorTemp = post.child("valor").getValue(Double::class.java) ?: continue

                if (criterio == "Fecha" && fecha == valor) {
                    lista.add(Temperatura(fecha, hora, valorTemp))
                } else if (criterio == "Mes") {
                    val mes = fecha.split("/").getOrNull(1) ?: ""
                    if (mes == valor) {
                        lista.add(Temperatura(fecha, hora, valorTemp))
                    }
                }
            }
            onSuccess(lista)
        }

        override fun onCancelled(error: DatabaseError) {
            onError("Error al buscar: ${error.message}")
        }
    })
}
