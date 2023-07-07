package com.paranid5.innobookingfakeapi.data.firebase

import com.google.firebase.cloud.FirestoreClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

private const val COLLECTION_NAME = "users"

internal suspend inline fun fetchUsersFromFirestoreAsync() = coroutineScope {
    async(Dispatchers.IO) {
        FirestoreClient
            .getFirestore()
            .collection(COLLECTION_NAME)
            .get()
            .get()
            .documents
            .map { document -> document["id"]!! as String }
    }
}