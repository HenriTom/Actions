package de.henritom.actions.ui

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class ManageScreen : Screen(Text.translatable("actions.ui.manage.title")) {

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
            Text.translatable("actions.ui.manage.title"),
            4 + textRenderer.getWidth(Text.translatable("actions.ui.main.title")) + textRenderer.getWidth(" "),
            4,
            UIColors.YELLOW.color.rgb,
            true
        )

        // Version
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.version", FabricLoader.getInstance().getModContainer("actions").get().metadata.version.toString()),
            4,
            4 + textRenderer.fontHeight,
            UIColors.YELLOW.color.rgb,
            true
        )

        // Top
        val separatorWidth = textRenderer.getWidth(Text.translatable("actions.ui.manage.top.separator"))
        val textSize = textRenderer.getWidth(" ________________ ")
        val numberSize = textRenderer.getWidth(" 2147483647 ")
        val padding = 16
        val labels = listOf(
            "actions.ui.manage.top.call",
            "actions.ui.manage.top.delete",
            "actions.ui.manage.top.edit",
            "actions.ui.manage.top.name",
            "actions.ui.manage.top.id",
            "actions.ui.manage.top.author",
            "actions.ui.manage.top.triggers",
            "actions.ui.manage.top.tasks"
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
            if (label == "actions.ui.manage.top.call" || label == "actions.ui.manage.top.delete" || label == "actions.ui.manage.top.edit") {

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
                val size = when (label) {
                    "actions.ui.manage.top.name", "actions.ui.manage.top.author" -> textSize
                    else -> numberSize
                }

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
                    xPos + (size - textRenderer.getWidth(Text.translatable(label))) / 2,
                    4 + textRenderer.fontHeight * 3,
                    UIColors.WHITE.color.rgb,
                    true
                )
                xPos += size
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
        for ((index, action) in ActionManager.instance.actions.drop(scroll).withIndex()) {
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
                "actions.ui.manage.top.call",
                "actions.ui.manage.top.delete",
                "actions.ui.manage.top.edit",
                action.name,
                action.id.toString(),
                action.author,
                action.triggers.size.toString(),
                action.tasks.size.toString()
            )

            xPos = 4 + separatorWidth

            for ((valueIndex, value) in values.withIndex()) {
                val width = when (valueIndex) {
                    0, 1, 2 -> textRenderer.getWidth(Text.translatable(value)) + padding
                    3, 5 -> textSize
                    else -> numberSize
                }

                context.drawText(
                    textRenderer,
                    Text.translatable(value),
                    xPos + (width - textRenderer.getWidth(Text.translatable(value))) / 2,
                    yPos + 2,
                    if (mouseX in xPos + 8..(xPos + width - 8) && mouseY in yPos..(yPos + textRenderer.fontHeight + 4) && valueIndex in 0..2) UIColors.YELLOW.color.rgb else UIColors.WHITE.color.rgb,
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

        val actionList = ActionManager.instance.actions.drop(scroll)
        val separatorWidth = textRenderer.getWidth(Text.translatable("actions.ui.manage.top.separator"))
        val padding = 16

        for ((index, action) in actionList.withIndex()) {
            val yPos = (textRenderer.fontHeight * 2 * (index + 2) + textRenderer.fontHeight + 8)

            if (yPos > height - (textRenderer.fontHeight - 4) * 6)
                break

            val values = listOf(
                "actions.ui.manage.top.call",
                "actions.ui.manage.top.delete",
                "actions.ui.manage.top.edit"
            )

            var xPos = 4 + separatorWidth

            for (value in values) {
                val width = textRenderer.getWidth(Text.translatable(value)) + padding
                val xStart = xPos + 8
                val xEnd = xPos + width - 8
                val yEnd = yPos + textRenderer.fontHeight + 4

                if (mouseX.toInt() in xStart..xEnd && mouseY.toInt() in yPos..yEnd) {
                    when (value) {
                        "actions.ui.manage.top.call" -> {
                            action.call()
                            MessageUtil().printTranslatable("actions.action.called", action.name)
                        }
                        "actions.ui.manage.top.delete" -> if (ActionManager.instance.deleteAction(action.name)) MessageUtil().printTranslatable("actions.action.deleted", action.name) else MessageUtil().printTranslatable("actions.action.not_found", action.name)
                        "actions.ui.manage.top.edit" -> MinecraftClient.getInstance().setScreen(EditActionScreen().asAction(action))
                    }
                    return true
                }

                xPos += width + separatorWidth
            }
        }

        return true
    }


    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)

        scroll = (scroll - verticalAmount.toInt()).coerceAtLeast(0).coerceAtMost(ActionManager.instance.actions.size - 1)

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
}