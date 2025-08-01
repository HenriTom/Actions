package me.henritom.actionsv2.loader

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.henritom.actionsv2.axn.ActionManager
import me.henritom.actionsv2.axn.AxnAction
import me.henritom.actionsv2.axn.RawAxnAction
import me.henritom.actionsv2.axn.toAxnAction
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Modifier
import java.nio.file.Files
import java.nio.file.Paths

private const val LOADER_ENVIRONMENT = "client"

private val logger = LogManager.getLogger("Actions/Loader")
private val axnDir = Paths.get(FabricLoader.getInstance().configDir.toString(), "actionsv2", "actions")

// Loads Actions eXtended Notation (.axn) files as Actions
object ActionsLoader {

    private var processed = 0
    private var failed = 0

    fun loadAll(skipDuplicates: Boolean = true) {
        processed = 0
        failed = 0

        val gson = Gson()

        if (!Files.exists(axnDir))
            Files.createDirectories(axnDir)

        Files.walk(axnDir).use { paths ->
            paths.filter { it.toString().endsWith(".axn") }.forEach { path ->
                try {
                    Files.newBufferedReader(path).use { reader ->
                        val rawAxnAction = gson.fromJson(reader, RawAxnAction::class.java)

                        if (rawAxnAction.environment != LOADER_ENVIRONMENT)
                            logger.warn("Unsupported environment: ${rawAxnAction.environment} at ${rawAxnAction.id}")

                        when (rawAxnAction.loaderVersion) {
                            1 -> loadV1(rawAxnAction, skipDuplicates)
                            else -> logger.error("Unknown Loader version: ${rawAxnAction.loaderVersion} at ${rawAxnAction.id}")
                        }
                    }
                } catch (e: Exception) {
                    logger.error("Failed to load action from ${path.fileName}: ${e.message}")
                    failed++
                }
            }
        }

        logger.info("Loaded $processed/${processed + failed} actions from $axnDir")
    }

    fun loadV1(rawAxnAction: RawAxnAction, skipDuplicate: Boolean) {
        val action = rawAxnAction.toAxnAction()

        if (ActionManager.loadedActions.contains(action.id)) {
            if (skipDuplicate) {
                logger.warn("Action with ID ${action.id} already exists, skipping")
                return
            }

            logger.warn("Action with ID ${action.id} already exists, appending suffix")
            rawAxnAction.id += "_${System.currentTimeMillis() % 1000}"

            loadV1(rawAxnAction, false)
            return
        }

        ActionManager.loadedActions.put(action.id, action)

        processed++
    }

    fun saveAll(onlyChanged: Boolean = true) {
        processed = 0
        failed = 0

        for ((_, action) in ActionManager.loadedActions) {
            if (onlyChanged && !action.changed)
                continue

            try {
                saveV1(action)
                action.changed = false
                processed++
            } catch (e: Exception) {
                logger.error("Failed to save action ${action.id}: ${e.message}")
                failed++
            }
        }

        logger.info("Saved $processed/${processed + failed} actions to $axnDir")
    }

    fun saveV1(action: AxnAction) {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
            .disableHtmlEscaping()
            .create()

        val filePath = axnDir.resolve("${action.id}.axn")
        Files.write(filePath, gson.toJson(action).toByteArray())
    }
}