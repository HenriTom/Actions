package ui

import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.awt.Color
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class MainScreen : Screen(Text.translatable("actions.ui.main.title")) {

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        context.fill(0, 0, width, height, Color(0, 0, 0, 128).rgb)
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            Text.translatable("actions.ui.main.drag_and_drop"),
            (width - MinecraftClient.getInstance().textRenderer.getWidth(Text.translatable("actions.ui.main.drag_and_drop"))) / 2,
            (height - MinecraftClient.getInstance().textRenderer.fontHeight) / 2,
            Color.WHITE.rgb,
            true
        )
    }

    override fun onFilesDropped(paths: MutableList<Path>?) {
        super.onFilesDropped(paths)

        val file = FabricLoader.getInstance().configDir.resolve("actions/actions/")

        if (!file.exists())
            file.createDirectory()

        if (paths != null)
            for (path in paths) {
                val f = file.resolve("${path.fileName}")

                if (f.exists())
                    f.deleteExisting()

                Files.copy(path, file.resolve("${path.fileName}"))
            }

        ConfigManager().loadActions()
        MessageUtil().printTranslatable("actions.file.reloaded.actions")
    }
}