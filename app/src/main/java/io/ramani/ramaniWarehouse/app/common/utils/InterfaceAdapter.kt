package io.ramani.ramaniWarehouse.app.common.utils

import com.google.gson.*
import java.lang.reflect.Type

class InterfaceAdapter : JsonSerializer<Any?>, JsonDeserializer<Any?> {
    @Throws(JsonParseException::class)
    override fun deserialize(jsonElement: JsonElement, type: Type,
                             jsonDeserializationContext: JsonDeserializationContext): Any? {
        val jsonObject = jsonElement.asJsonObject
        val prim = jsonObject[CLASSNAME] as JsonPrimitive
        val className = prim.asString
        val klass = getObjectClass(className)
        return jsonDeserializationContext.deserialize<Any>(jsonObject[DATA], klass)
    }

    fun serialize(jsonElement: JsonObject, type: Type?, jsonSerializationContext: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty(CLASSNAME, jsonElement.javaClass.name)
        jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement))
        return jsonObject
    }

    /****** Helper method to get the className of the object to be deserialized  */
    fun getObjectClass(className: String?): Class<*> {
        return try {
            Class.forName(className!!)
        } catch (e: ClassNotFoundException) {
            //e.printStackTrace();
            throw JsonParseException(e.message)
        }
    }

    override fun serialize(src: Any?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement? {
        return null
    }

    companion object {
        private const val CLASSNAME = "CLASSNAME"
        private const val DATA = "DATA"
    }
}