package com.example.weatherapp.data.repository

import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.domain.repository.FavouriteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FavouriteRepository {

    private fun getCollectionRef() = auth.currentUser?.uid?.let { uid ->
        firestore
            .collection("users")
            .document(uid)
            .collection("favorite_cities")
    }

    override suspend fun addToFavourite(city: City) {
        val ref = getCollectionRef() ?: return

        ref.document(city.id.toString()).set(
            mapOf(
                "id" to city.id,
                "name" to city.name,
                "country" to city.country
            )
        ).await()
    }
    override suspend fun removeFromFavourite(cityId: Int) {
        val ref = getCollectionRef() ?: return
        ref.document(cityId.toString()).delete().await()
    }

    override val favouriteCities: Flow<List<City>> = getCollectionRef()
        ?.snapshots()
        ?.map { snapshot ->
            snapshot.documents.map { doc ->
                City(
                    id = doc.getLong("id")?.toInt() ?: 0,
                    name = doc.getString("name") ?: "",
                    country = doc.getString("country") ?: ""
                )
            }
        } ?: kotlinx.coroutines.flow.flowOf(emptyList())

    override fun observeIsFavourite(cityId: Int): Flow<Boolean> = favouriteCities
        .map { cities ->
            cities.any { it.id == cityId }
        }
}