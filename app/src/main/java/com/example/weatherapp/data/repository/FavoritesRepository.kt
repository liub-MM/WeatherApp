package com.example.weatherapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

data class FavoriteCity(
    val id: String = "",
    val name: String = "",
    val country: String = ""
)
class FavoritesRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun getFavoriteCities(): Flow<List<FavoriteCity>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val collectionRef = firestore
            .collection("users")
            .document(uid)
            .collection("favorites")

        val listener = collectionRef
            .addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val cities = snapshot?.toObjects(FavoriteCity::class.java) ?: emptyList()
            trySend(cities)
        }

        awaitClose { listener.remove() }
    }

    fun addCity(name: String, country: String) {
        val uid = auth.currentUser?.uid ?: return
        val collectionRef = firestore.collection("users").document(uid).collection("favorites")

        val newDocRef = collectionRef.document()
        val city = FavoriteCity(id = newDocRef.id, name = name, country = country)

        newDocRef.set(city)
    }

    fun removeCity(cityId: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).collection("favorites").document(cityId).delete()
    }
}