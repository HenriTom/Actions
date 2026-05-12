package de.henritom.actions.ui.impl

import de.henritom.actions.config.ConfigManager
import de.henritom.actions.region.RegionManager
import de.henritom.actions.ui.GlobalUI
import de.henritom.actions.ui.UIColors
import de.henritom.actions.util.MessageUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class RegionsScreen(val parent: Screen?) : Screen(Text.translatable("actions.ui.regions.title")) {

    private var addButton: ButtonWidget? = null
    private var scroll = 0

    override fun init() {
        super.init()
        addButton = null
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
            Text.translatable("actions.ui.regions.title"),
            4 + textRenderer.getWidth(Text.translatable("actions.ui.main.title")) + textRenderer.getWidth(" "),
            4,
            UIColors.PURPLE.color.rgb,
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

        // Top
        val separatorWidth = textRenderer.getWidth(Text.translatable("actions.ui.manage.top.separator"))
        val padding = 16
        val labels = listOf(
            "actions.ui.triggers.top.remove",
            "actions.ui.manage.top.edit",
            "actions.ui.addregion.name",
            "actions.ui.addregion.pos1",
            "actions.ui.addregion.pos2"
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
            } else if (label == "actions.ui.addregion.name") {
                val width = textRenderer.getWidth(" HHHHHHHHHHHHHHHH ") + padding

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
                val size = 4 + separatorWidth + textRenderer.getWidth("(6.5456564E7, 6.5456564E7, 6.5456564E7)") + padding

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

        // Regions
        for ((index, region) in RegionManager.instance.regions.drop(scroll).withIndex()) {
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
                region.name,
                region.pos1.toString(),
                region.pos2.toString()
            )

            xPos = 4 + separatorWidth

            for ((valueIndex, value) in values.withIndex()) {
                val width = when (valueIndex) {
                    0, 1 -> textRenderer.getWidth(Text.translatable(value)) + padding
                    2 -> textRenderer.getWidth(" HHHHHHHHHHHHHHHH ") + padding
                    else -> 4 + separatorWidth + textRenderer.getWidth("(6.5456564E7, 6.5456564E7, 6.5456564E7)") + padding
                }

                context.drawText(
                    textRenderer,
                    Text.translatable(value),
                    xPos + if (valueIndex != 45) (width - textRenderer.getWidth(Text.translatable(value))) / 2 else width,
                    yPos + 2,
                    if (mouseX in xPos + 8..(xPos + width - 8) && mouseY in yPos..(yPos + textRenderer.fontHeight + 4) && valueIndex in 0..1) UIColors.PURPLE.color.rgb else UIColors.WHITE.color.rgb,
                    true
                )
                xPos += width

                if (valueIndex < values.size - 1)
                    xPos += separatorWidth
            }
        }

        // Add Button
        if (addButton == null) {
            addButton = ButtonWidget.builder(Text.translatable("actions.ui.triggers.add")) {
                MinecraftClient.getInstance()?.setScreen(AddRegionScreen(RegionsScreen(OtherScreen(MainScreen(GlobalUI.mainScreenParent)))))
                return@builder
            }
                .dimensions(
                    width - (textRenderer.getWidth(Text.translatable("actions.ui.triggers.add")) + textRenderer.getWidth(
                        "  "
                    ) + 8),
                    height - (textRenderer.fontHeight + 12),
                    textRenderer.getWidth(Text.translatable("actions.ui.triggers.add")) + textRenderer.getWidth("  "),
                    textRenderer.fontHeight + 8
                )
                .build()

            addDrawableChild(addButton)
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

    override fun mouseClicked(click: Click, doubled: Boolean): Boolean {
        super.mouseClicked(click, doubled)

        if (click.button() != 0)
            return false

        val regionList = RegionManager.instance.regions.drop(scroll)
        val separatorWidth = textRenderer.getWidth(Text.translatable("actions.ui.manage.top.separator"))
        val padding = 16

        for ((index, region) in regionList.withIndex()) {
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

                if (click.x.toInt() in xStart..xEnd && click.y.toInt() in yPos..yEnd) {
                    when (value) {
                        "actions.ui.triggers.top.remove" -> {
                            RegionManager.instance.removeRegion(region)
                        }

                        "actions.ui.manage.top.edit" -> {
                            MinecraftClient.getInstance()?.setScreen(EditRegionScreen(RegionsScreen(OtherScreen(MainScreen(GlobalUI.mainScreenParent)))).asRegion(region))
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

        scroll = (scroll - verticalAmount.toInt()).coerceAtLeast(0).coerceAtMost((RegionManager.instance.regions.size) - 1)

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