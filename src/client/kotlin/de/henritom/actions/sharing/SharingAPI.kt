package de.henritom.actions.sharing

import de.henritom.actions.actions.Action
import de.henritom.actions.config.ConfigManager
import java.io.*
import java.net.HttpURLConnection
import java.net.URI
import java.util.concurrent.Executors
import java.util.concurrent.Future

class SharingAPI(private val url: String) {
    private val apiVersion = "v1"

    private fun uploadAction(action: Action?): String {
        if (action?.file == null || !action.file!!.exists()) return "null"

        val boundary = "----Boundary${System.currentTimeMillis()}"
        val connection = URI("$url/$apiVersion/upload").toURL().openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

        try {
            DataOutputStream(connection.outputStream).use { output ->
                output.writeBytes("--$boundary\r\n")
                output.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"${action.file!!.name}\"\r\n")
                output.writeBytes("Content-Type: application/octet-stream\r\n\r\n")

                FileInputStream(action.file!!).use { it.copyTo(output) }

                output.writeBytes("\r\n--$boundary--\r\n")
                output.flush()
            }

            return if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().readText()
            } else {
                "null"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "null"
        } finally {
            connection.disconnect()
        }
    }

    private fun downloadFile(fileName: String): String {
        val connection = URI("$url/$apiVersion/download/$fileName").toURL().openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val fileBytes = connection.inputStream
                val finalName = (connection.getHeaderField("Content-Disposition")?.let {
                    "filename=\"([^\"]+)\"".toRegex().find(it)?.groupValues?.get(1)
                } ?: "tempfile").replace(Regex("[^a-zA-Z0-9._-]"), "_")

                val tempFile = File.createTempFile(finalName, null)
                tempFile.writeBytes(fileBytes.readAllBytes())

                ConfigManager().copyFileToFolder(tempFile, finalName)
                ConfigManager().reloadActions()

                tempFile.delete()

                finalName
            } else {
                "null"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "null"
        } finally {
            connection.disconnect()
        }
    }

    fun uploadActionInBackground(action: Action?): Future<String> {
        return Executors.newSingleThreadExecutor().submit<String> {
            uploadAction(action)
        }
    }

    fun downloadActionInBackground(fileName: String): Future<String> {
        return Executors.newSingleThreadExecutor().submit<String> {
            downloadFile(fileName)
        }
    }

    fun getStatus(): String {
        return try {
            val connection = URI("$url/$apiVersion").toURL() .openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 3000
            connection.readTimeout = 3000

            val status = if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                "actions.ui.sharing.status.online"
            } else {
                "actions.ui.sharing.status.offline"
            }

            connection.disconnect()
            status
        } catch (e: Exception) {
            "actions.ui.sharing.status.offline"
        }
    }
}
