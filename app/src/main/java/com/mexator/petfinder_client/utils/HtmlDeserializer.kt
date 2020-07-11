package com.mexator.petfinder_client.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import org.apache.commons.lang3.StringEscapeUtils
import java.lang.reflect.Type


class HtmlDeserializer: JsonDeserializer<String> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement, typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String? {
        val intermediate = StringEscapeUtils.unescapeHtml4(json.asString)
        return StringEscapeUtils.unescapeHtml4(intermediate)
    }
}