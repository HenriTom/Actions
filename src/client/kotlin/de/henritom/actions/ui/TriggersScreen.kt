package de.henritom.actions.ui

import de.henritom.actions.actions.Action
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.util.MessageUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class TriggersScreen : Screen(Text.translatable("actions.ui.tasks.title")) {

    private var action: Action? = null

    private var addButton: ButtonWidget? = null

    private var scroll = 0

    fun asAction(action: Action): TriggersScreen {
        this.action = action
        return this
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
            Text.translatable("actions.ui.triggers.title"),
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
        val textSize = TriggerEnum.entries.toTypedArray().maxOf { textRenderer.getWidth(it.toString()) }
        val numberSize = textRenderer.getWidth(" 2147483647 ")
        val padding = 16
        val labels = listOf(
            "actions.ui.triggers.top.remove",
            "actions.ui.manage.top.edit",
            "actions.ui.triggers.top.type",
            "actions.ui.manage.top.id",
            "actions.ui.triggers.top.value"
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
            if (label == "actions.ui.triggers.top.remove" || label == "actions.ui.manage.top.edit") {
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
                    "actions.ui.triggers.top.type" -> textSize + padding
                    "actions.ui.manage.top.id" -> numberSize
                    else -> 4 + separatorWidth + textRenderer.getWidth(Text.translatable("actions.ui.triggers.top.value"))
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

        // Triggers
        for ((index, trigger) in action?.triggers?.drop(scroll)?.withIndex()!!) {
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
                "actions.ui.triggers.top.remove",
                "actions.ui.manage.top.edit",
                trigger.type.toString(),
                trigger.id.toString(),
                trigger.value.toString()
            )

            xPos = 4 + separatorWidth

            for ((valueIndex, value) in values.withIndex()) {
                val width = when (valueIndex) {
                    0, 1 -> textRenderer.getWidth(Text.translatable(value)) + padding
                    2 -> textSize + padding
                    3 -> numberSize
                    else -> 4 + separatorWidth
                }

                context.drawText(
                    textRenderer,
                    Text.translatable(value),
                    xPos + if (valueIndex != 4) (width - textRenderer.getWidth(Text.translatable(value))) / 2 else width,
                    yPos + 2,
                    if (mouseX in xPos + 8..(xPos + width - 8) && mouseY in yPos..(yPos + textRenderer.fontHeight + 4) && valueIndex in 0..1) UIColors.YELLOW.color.rgb else UIColors.WHITE.color.rgb,
                    true
                )
                xPos += width

                if (valueIndex < values.size - 1)
                    xPos += separatorWidth
            }
        }

        // Add Button
        addButton = ButtonWidget.builder(Text.translatable("actions.ui.triggers.add")) {
            MinecraftClient.getInstance().setScreen(AddTriggerScreen().asAction(action!!))
            return@builder;
        }
            .dimensions(
                width - (textRenderer.getWidth(Text.translatable("actions.ui.triggers.add")) + textRenderer.getWidth("  ") + 8),
                height - (textRenderer.fontHeight + 12),
                textRenderer.getWidth(Text.translatable("actions.ui.triggers.add")) + textRenderer.getWidth("  "),
                textRenderer.fontHeight + 8
            )
            .build()

        addDrawableChild(addButton)

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

        val triggersList = action?.triggers?.drop(scroll) ?: return false
        val separatorWidth = textRenderer.getWidth(Text.translatable("actions.ui.manage.top.separator"))
        val padding = 16

        for ((index, trigger) in triggersList.withIndex()) {
            val yPos = (textRenderer.fontHeight * 2 * (index + 2) + textRenderer.fontHeight + 8)

            if (yPos > height - (textRenderer.fontHeight - 4) * 6)
                break

            val values = listOf(
                "actions.ui.triggers.top.remove",
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
                        "actions.ui.triggers.top.remove" -> {
                            action?.triggers?.remove(trigger)
                            MessageUtil().printTranslatable("actions.trigger.removed", trigger.type.name, trigger.id.toString(), action!!.name)
                        }
                        "actions.ui.manage.top.edit" -> {
                            MinecraftClient.getInstance().setScreen(EditTriggerScreen().asTrigger(trigger))
                        }
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

        scroll = (scroll - verticalAmount.toInt()).coerceAtLeast(0).coerceAtMost((action?.triggers?.size ?: 1) - 1)

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