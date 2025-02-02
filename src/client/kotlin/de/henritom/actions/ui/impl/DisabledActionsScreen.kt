package de.henritom.actions.ui.impl

import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
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

class DisabledActionsScreen(val parent: Screen?) : Screen(Text.translatable("actions.ui.disable.title")) {

    private var scroll = 0

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
            Text.translatable("actions.ui.disabled.title"),
            4 + textRenderer.getWidth(Text.translatable("actions.ui.main.title")) + textRenderer.getWidth(" "),
            4,
            UIColors.RED.color.rgb,
            true
        )

        // Version
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.version", FabricLoader.getInstance().getModContainer("actions").get().metadata.version.toString()),
            4,
            4 + textRenderer.fontHeight,
            UIColors.RED.color.rgb,
            true
        )

        // Top
        val separatorWidth = textRenderer.getWidth(Text.translatable("actions.ui.manage.top.separator"))
        val textSize = textRenderer.getWidth(" ________________ ")
        val padding = 16
        val labels = listOf(
            "actions.ui.disabled.top.enable",
            "actions.ui.manage.top.name"
        )

        var xPos = 4

        // Separator
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.manage.top.separator"),
            xPos,
            4 + textRenderer.fontHeight * 3,
            UIColors.WHITE.color.rgb,
            true
        )
        xPos += separatorWidth

        // Labels
        for ((index, label) in labels.withIndex()) {
            if (label == "actions.ui.disabled.top.enable") {

                val width = textRenderer.getWidth(Text.translatable(label)) + padding

                if (index > 0) {
                    context.drawText(
                        textRenderer,
                        Text.translatable("actions.ui.manage.top.separator"),
                        xPos,
                        4 + textRenderer.fontHeight * 3,
                        UIColors.WHITE.color.rgb,
                        true
                    )
                    xPos += separatorWidth
                }

                context.drawText(
                    textRenderer,
                    Text.translatable(label),
                    xPos + (width - textRenderer.getWidth(Text.translatable(label))) / 2,
                    4 + textRenderer.fontHeight * 3,
                    UIColors.WHITE.color.rgb,
                    true
                )
                xPos += width
            } else {
                if (index > 0) {
                    context.drawText(
                        textRenderer,
                        Text.translatable("actions.ui.manage.top.separator"),
                        xPos,
                        4 + textRenderer.fontHeight * 3,
                        UIColors.WHITE.color.rgb,
                        true
                    )
                    xPos += separatorWidth
                }

                context.drawText(
                    textRenderer,
                    Text.translatable(label),
                    xPos + (textSize - textRenderer.getWidth(Text.translatable(label))) / 2,
                    4 + textRenderer.fontHeight * 3,
                    UIColors.WHITE.color.rgb,
                    true
                )
                xPos += textSize
            }
        }

        // Separator
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.manage.top.separator"),
            xPos,
            4 + textRenderer.fontHeight * 3,
            UIColors.WHITE.color.rgb,
            true
        )

        // Actions
        for ((index, action) in ActionManager.instance.getDisabledActions().drop(scroll).withIndex()) {
            val yPos = (textRenderer.fontHeight * 2 * (index + 2) + textRenderer.fontHeight + 8)

            if (yPos > height - (textRenderer.fontHeight - 4) * 6)
                break

            context.fill(
                4,
                yPos,
                width - 8,
                yPos + textRenderer.fontHeight + 4,
                UIColors.BACKGROUND.color.rgb
            )

            val values = listOf(
                "actions.ui.disabled.top.enable",
                action.name
            )

            xPos = 4 + separatorWidth

            for ((valueIndex, value) in values.withIndex()) {
                val newValue = if (valueIndex == 0) value else value.replace(".disabled", "")
                val width = if (valueIndex == 0) textRenderer.getWidth(Text.translatable(newValue)) + padding else textSize

                context.drawText(
                    textRenderer,
                    Text.translatable(newValue),
                    xPos + (width - textRenderer.getWidth(Text.translatable(newValue))) / 2,
                    yPos + 2,
                    if (mouseX in xPos + 8..(xPos + width - 8) && mouseY in yPos..(yPos + textRenderer.fontHeight + 4) && valueIndex == 0) UIColors.RED.color.rgb else UIColors.WHITE.color.rgb,
                    true
                )
                xPos += width

                if (valueIndex < values.size - 1)
                    xPos += separatorWidth
            }
        }

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

        val actionList = ActionManager.instance.getDisabledActions()
        val separatorWidth = textRenderer.getWidth(Text.translatable("actions.ui.manage.top.separator"))
        val padding = 16

        for ((index, action) in actionList.withIndex()) {
            val yPos = (textRenderer.fontHeight * 2 * (index + 2) + textRenderer.fontHeight + 8)

            if (yPos > height - (textRenderer.fontHeight - 4) * 6)
                break

            val values = listOf(
                "actions.ui.disabled.top.enable",
            )

            var xPos = 4 + separatorWidth

            for (value in values) {
                val width = textRenderer.getWidth(Text.translatable(value)) + padding
                val xStart = xPos + 8
                val xEnd = xPos + width - 8
                val yEnd = yPos + textRenderer.fontHeight + 4

                if (mouseX.toInt() in xStart..xEnd && mouseY.toInt() in yPos..yEnd) {
                    if (value == "actions.ui.disabled.top.enable")
                        if (ActionEditManager.instance.enableAction(action)) {
                            MessageUtil().printTranslatable("actions.action.enabled", action.name)
                            ConfigManager().reloadActions()
                        } else
                            MessageUtil().printTranslatable("actions.action.not_enabled", action.name)

                    return true
                }

                xPos += width + separatorWidth
            }
        }

        return true
    }


    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)

        scroll = (scroll - verticalAmount.toInt()).coerceAtLeast(0).coerceAtMost(ActionManager.instance.getDisabledActions().size - 1)

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
        MessageUtil().printTranslatable("actions.file.reloaded.actions")
    }

    override fun close() {
        this.client?.setScreen(this.parent)
    }
}