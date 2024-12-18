package com.example.currencyconverter.data.models

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import android.util.Log

class ExchangeRatesJsonAdapter: JsonAdapter<ExchangeRatesResponse>() {
    @FromJson
    override fun fromJson(reader: JsonReader): ExchangeRatesResponse {

        reader.beginObject()

        var success = false
        var terms = ""
        var privacy = ""
        var timeStamp = 0
        var date = ""
        var base = ""
        val rates = mutableMapOf<String, Double>()

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "success" -> success = reader.nextBoolean()
                "terms" -> terms = reader.nextString()
                "privacy" -> privacy = reader.nextString()
                "timestamp" -> timeStamp = reader.nextInt()
                "date" -> date = reader.nextString()
                "base" -> base = reader.nextString()
                "rates" -> {

                    reader.beginObject()

                    while (reader.peek() != JsonReader.Token.END_OBJECT) {

                        val code = reader.nextName()

                        val amount = reader.nextDouble()

                        rates[code] = amount
                    }
                    reader.endObject()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return ExchangeRatesResponse(
            success = success,
            terms = terms,
            privacy = privacy,
            timestamp = timeStamp,
            date = date,
            base = base,
            rates = rates
        )
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value:
    ExchangeRatesResponse?) {
        throw UnsupportedOperationException()
    }
}