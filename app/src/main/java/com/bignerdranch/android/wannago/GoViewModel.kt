package com.bignerdranch.android.wannago

import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


const val TAG ="GoViewModel"
class GoViewModel: ViewModel() {
    private val db = Firebase.firestore

    fun setCoords(lat: Double, lon: Double,collectionName: String, geocoder: Geocoder){
        val address = geocoder.getFromLocation(lat,lon,1)!![0].getAddressLine(0) as String
        db.collection(collectionName)
            .add(hashMapOf("latitude" to lat,"longitude" to lon, "address" to address))
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
    fun getCoords(collectionName: String) : StateFlow<List<CoordinatesData>> {
        val listFlow = MutableStateFlow(emptyList<CoordinatesData>())
        db.collection(collectionName)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.documents != null) {
                    listFlow.value = snapshot.documents.map {doc ->
                        CoordinatesData(doc["latitude"] as Double, doc["longitude"] as Double,
                            doc["address"] as String)
                    }
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
        return listFlow
    }
}