package de.henritom.actions.ui.impl

import de.henritom.actions.config.ConfigManager
import de.henritom.actions.ui.UIColors
import de.henritom.actions.util.MessageUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class SettingsScreen(val parent: Screen?) : Screen(Text.translatable("actions.ui.settings.title")) {

    companion object {
        @kotlin.jvm.JvmField
        var addOptionsScreenButton: Boolean = true
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        context.fill(0, 0, width, height, UIColors.BACKGROUND.color.rgb)

        // Title
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.title"),
            4,
            4,
            UIColors.WHITE.color.rgb,
            true
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.settings.title"),
            4 + textRenderer.getWidth(Text.translatable("actions.ui.main.title")) + textRenderer.getWidth(" "),
            4,
            UIColors.PURPLE.color.rgb,
            true
        )

        // x Checkbox
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.settings.add_options_screen"),
            textRenderer.fontHeight + 8,
            7 + textRenderer.fontHeight * 3,
            UIColors.PURPLE.color.rgb,
            true
        )

        context.drawText(
            textRenderer,
            Text.literal(if (addOptionsScreenButton) "☑" else "☐"),
            4,
            7 + textRenderer.fontHeight * 3,
            if (mouseX in 4..4 + textRenderer.fontHeight && mouseY in 7 + textRenderer.fontHeight * 3..7 + textRenderer.fontHeight * 4) UIColors.PURPLE.color.rgb else UIColors.WHITE.color.rgb,
            true
        )

        // Version
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.version", FabricLoader.getInstance().getModContainer("actions_legacy").get().metadata.version.toString()),
            4,
            4 + textRenderer.fontHeight,
            UIColors.PURPLE.color.rgb,
            true
        )

        // Drag and Drop
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.drag_and_drop"),
            4,
            height - textRenderer.fontHeight - 4,
            UIColors.WHITE.color.rgb,
            true
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        super.mouseClicked(mouseX, mouseY, button)

        if (button != 0)
            return false

        if (mouseX.toInt() in 4..4 + textRenderer.fontHeight && mouseY.toInt() in 7 + textRenderer.fontHeight * 3..7 + textRenderer.fontHeight * 4)
            addOptionsScreenButton = !addOptionsScreenButton

        return true
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
        MessageUtil(null).printTranslatable("actions.file.reloaded.actions")
    }

    override fun close() {
        this.client?.setScreen(this.parent)
    }
}