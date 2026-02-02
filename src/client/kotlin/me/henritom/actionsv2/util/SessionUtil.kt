package me.henritom.actionsv2.util

import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Modifier
import java.nio.file.Files
import java.nio.file.Paths

object SessionUtil {

    private val logger = LogManager.getLogger("Actions/Loader")
    private val dataDir = Paths.get(FabricLoader.getInstance().configDir.toString(), "actionsv2", "data")

    var timeLastSession: Long = -1
    var timeCurrentSession: Long = -1

    fun loadSession() {
        timeCurrentSession = System.currentTimeMillis()

        try {
            if (!Files.exists(dataDir))
                Files.createDirectories(dataDir)

            val sessionPath = dataDir.resolve("last_session.json")

            if (Files.exists(sessionPath)) {
                timeLastSession = GsonBuilder().create().fromJson(Files.readString(sessionPath), Long::class.java)
                logger.info("Loaded last session time: $timeLastSession")
            } else
                logger.info("No previous session found.")
        } catch (e: Exception) {
            logger.error("Failed to load session data.", e)
        }
    }

    fun saveSession() {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
            .disableHtmlEscaping()
            .create()

        val filePath = dataDir.resolve("last_session.json")
        Files.write(filePath, gson.toJson(timeCurrentSession).toByteArray())
    }
}