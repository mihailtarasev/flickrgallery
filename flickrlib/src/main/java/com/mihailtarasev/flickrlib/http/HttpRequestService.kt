package com.mihailtarasev.flickrlib.http

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

internal class HttpRequestService {
    fun request(path: String, params: String?): String? {
        var reader: BufferedReader? = null
        var stream: InputStream? = null
        var connection: HttpsURLConnection? = null
        return try {
            var resultPath = path
            if(params != null) {
                resultPath += "?${params}"
            }
            val url = URL(resultPath)
            connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = httpRequestMethod
            connection.readTimeout = httpReadTimeout
            connection.connect()
            stream = connection.inputStream
            reader = BufferedReader(InputStreamReader(stream))
            val buf = java.lang.StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                buf.append(line).append("\n")
            }
            buf.toString()
        } finally {
            reader?.close()
            stream?.close()
            connection?.disconnect()
        }
    }

    companion object {
        const val httpReadTimeout = 10000
        const val httpRequestMethod = "GET"
    }
}