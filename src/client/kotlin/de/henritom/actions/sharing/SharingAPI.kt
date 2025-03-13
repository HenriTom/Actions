package de.henritom.actions.sharing

import de.henritom.actions.actions.Action
import de.henritom.actions.config.ConfigManager
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.net.http.HttpResponse
import java.util.concurrent.Executors
import java.util.concurrent.Future

class SharingAPI(private val url: String) {

    private val httpClient = HttpClient(CIO)
    private val apiVersion = "v1"

    private suspend fun uploadAction(action: Action?): String {
        return if (action?.file != null && action.file!!.exists()) {
            val response = httpClient.submitFormWithBinaryData("$url/$apiVersion/upload", formData {
                append("file", action.file!!.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, ContentType.Application.OctetStream.toString())
                    append(HttpHeaders.ContentDisposition, "filename=${action.file!!.name}")
                })
            })

            if (response.status == HttpStatusCode.OK)
                response.body<String>()
            else
                "null"
        } else
            "null"
    }

    private suspend fun downloadFile(fileName: String): String {
        val response = httpClient.get("$url/$apiVersion/download/$fileName")

        return if (response.status == HttpStatusCode.OK) {
            val fileBytes = response.body<ByteArray>()
            val finalName = (response.headers["Content-Disposition"]?.let {
                "filename=\"([^\"]+)\"".toRegex().find(it)?.groupValues?.get(1)
            } ?: "tempfile").replace(Regex("[^a-zA-Z0-9._-]"), "_")

            val tempFile = withContext(Dispatchers.IO) { File.createTempFile(finalName, null) }
            tempFile.writeBytes(fileBytes)

            ConfigManager().copyFileToFolder(tempFile, finalName)
            ConfigManager().reloadActions()

            tempFile.delete()

            finalName
        } else
            "null"
    }

    fun uploadActionInBackground(action: Action?): Future<String> {
        val executor = Executors.newSingleThreadExecutor()
        return executor.submit<String> {
            runBlocking { uploadAction(action) }
        }
    }

    fun downloadActionInBackground(fileName: String): Future<String> {
        val executor = Executors.newSingleThreadExecutor()
        return executor.submit<String> {
            runBlocking { downloadFile(fileName) }
        }
    }

    fun getStatus(): String {
        return runBlocking {
            try {
                val response = httpClient.get("$url/$apiVersion")

                if (response.status == HttpStatusCode.OK) {
                    "actions.ui.sharing.status.online"
                } else {
                    "actions.ui.sharing.status.offline"
                }
            } catch (e: Exception) {
                "actions.ui.sharing.status.offline"
            }
        }
    }

    fun shutdown() {
        httpClient.close()
    }
}