package io.ramani.ramaniWarehouse.data.utils

import android.net.Uri
import com.google.gson.*

inline fun <reified T> String.fromJsonString() =
    GsonBuilder().apply {
        registerTypeAdapter(Uri::class.java, UriTypeHierarchyAdapter())
    }.create().fromJson(this, T::class.java)

fun Any.toJsonString() =
    GsonBuilder().apply {
        registerTypeAdapter(Uri::class.java, UriTypeHierarchyAdapter())
    }.create().toJson(this)

fun Any.appendToJsonArray(jsonArray: JsonArray, jsonResponse: String) {
    val jsonObject = JsonParser().parse(jsonResponse).asJsonObject
    jsonObject.get("Data").asJsonArray.addAll(jsonArray)

    jsonObject.toJsonString()
}
