package de.henritom.actions.ui.impl

import de.henritom.actions.config.ConfigManager
import de.henritom.actions.region.Region
import de.henritom.actions.region.RegionManager
import de.henritom.actions.ui.GlobalUI
import de.henritom.actions.ui.UIColors
import de.henritom.actions.util.MessageUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class AddRegionScreen(val parent: Screen?) : Screen(Text.translatable("actions.ui.addregion.title")) {

    private var nameField: TextFieldWidget? = null

    private var pos1X: TextFieldWidget? = null
    private var pos1Y: TextFieldWidget? = null
    private var pos1Z: TextFieldWidget? = null

    private var pos2X: TextFieldWidget? = null
    private var pos2Y: TextFieldWidget? = null
    private var pos2Z: TextFieldWidget? = null

    private var addButton: ButtonWidget? = null

    override fun init() {
        super.init()
        nameField = null

        pos1X = null
        pos1Y = null
        pos1Z = null

        pos2X = null
        pos2Y = null
        pos2Z = null

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
            Text.translatable("actions.ui.addregion.title"),
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

        // Name
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addregion.name").append(":"),
            4,
            4 + textRenderer.fontHeight * 3,
            UIColors.PURPLE.color.rgb,
            true
        )

        if (nameField == null) {
            nameField = TextFieldWidget(
                textRenderer,
                4,
                4 + textRenderer.fontHeight * 4,
                (4 + textRenderer.getWidth(" 30000000 ")) * 2,
                textRenderer.fontHeight + 8,
                Text.translatable("actions.ui.addregion.name")
            )
            nameField?.setMaxLength(16)

            addDrawableChild(nameField)
        }

        // Position 1
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addregion.pos1").append(":"),
            4,
            4 + textRenderer.fontHeight * 7,
            UIColors.PURPLE.color.rgb,
            true
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addregion.pos1.x").append(":"),
            4,
            4 + textRenderer.fontHeight * 8,
            UIColors.PURPLE.color.rgb,
            true
        )

        if (pos1X == null) {
            pos1X = TextFieldWidget(
                textRenderer,
                4,
                4 + textRenderer.fontHeight * 9,
                textRenderer.getWidth(" 30000000 "),
                textRenderer.fontHeight + 8,
                Text.translatable("actions.ui.addregion.pos1.x")
            )
            pos1X?.setMaxLength(9)
            pos1X?.setChangedListener { newText ->
                if (!newText.matches(Regex("-?\\d*"))) {
                    val filteredText = newText.filter { it == '-' || it.isDigit() }
                    if (filteredText != newText)
                        pos1X?.text = filteredText
                }
            }

            addDrawableChild(pos1X)
        }

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addregion.pos1.y").append(":"),
            4,
            4 + textRenderer.fontHeight * 12,
            UIColors.PURPLE.color.rgb,
            true
        )

        if (pos1Y == null) {
            pos1Y = TextFieldWidget(
                textRenderer,
                4,
                4 + textRenderer.fontHeight * 13,
                textRenderer.getWidth(" 30000000 "),
                textRenderer.fontHeight + 8,
                Text.translatable("actions.ui.addregion.pos1.y")
            )
            pos1Y?.setMaxLength(9)
            pos1Y?.setChangedListener { newText ->
                if (!newText.matches(Regex("-?\\d*"))) {
                    val filteredText = newText.filter { it == '-' || it.isDigit() }
                    if (filteredText != newText)
                        pos1Y?.text = filteredText
                }
            }

            addDrawableChild(pos1Y)
        }

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addregion.pos1.z").append(":"),
            4,
            4 + textRenderer.fontHeight * 16,
            UIColors.PURPLE.color.rgb,
            true
        )

        if (pos1Z == null) {
            pos1Z = TextFieldWidget(
                textRenderer,
                4,
                4 + textRenderer.fontHeight * 17,
                textRenderer.getWidth(" 30000000 "),
                textRenderer.fontHeight + 8,
                Text.translatable("actions.ui.addregion.pos1.z")
            )
            pos1Z?.setMaxLength(9)
            pos1Z?.setChangedListener { newText ->
                if (!newText.matches(Regex("-?\\d*"))) {
                    val filteredText = newText.filter { it == '-' || it.isDigit() }
                    if (filteredText != newText)
                        pos1Z?.text = filteredText
                }
            }

            addDrawableChild(pos1Z)
        }

        // Position 2
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addregion.pos2").append(":"),
            4 + textRenderer.getWidth(" 30000000 ") + 4,
            4 + textRenderer.fontHeight * 7,
            UIColors.PURPLE.color.rgb,
            true
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addregion.pos2.x").append(":"),
            4 + textRenderer.getWidth(" 30000000 ") + 4,
            4 + textRenderer.fontHeight * 8,
            UIColors.PURPLE.color.rgb,
            true
        )

        if (pos2X == null) {
            pos2X = TextFieldWidget(
                textRenderer,
                4 + textRenderer.getWidth(" 30000000 ") + 4,
                4 + textRenderer.fontHeight * 9,
                textRenderer.getWidth(" 30000000 "),
                textRenderer.fontHeight + 8,
                Text.translatable("actions.ui.addregion.pos1.x")
            )
            pos2X?.setMaxLength(9)
            pos2X?.setChangedListener { newText ->
                if (!newText.matches(Regex("-?\\d*"))) {
                    val filteredText = newText.filter { it == '-' || it.isDigit() }
                    if (filteredText != newText)
                        pos2X?.text = filteredText
                }
            }

            addDrawableChild(pos2X)
        }

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addregion.pos1.y").append(":"),
            4 + textRenderer.getWidth(" 30000000 ") + 4,
            4 + textRenderer.fontHeight * 12,
            UIColors.PURPLE.color.rgb,
            true
        )

        if (pos2Y == null) {
            pos2Y = TextFieldWidget(
                textRenderer,
                4 + textRenderer.getWidth(" 30000000 ") + 4,
                4 + textRenderer.fontHeight * 13,
                textRenderer.getWidth(" 30000000 "),
                textRenderer.fontHeight + 8,
                Text.translatable("actions.ui.addregion.pos1.y")
            )
            pos2Y?.setMaxLength(9)
            pos2Y?.setChangedListener { newText ->
                if (!newText.matches(Regex("-?\\d*"))) {
                    val filteredText = newText.filter { it == '-' || it.isDigit() }
                    if (filteredText != newText)
                        pos2Y?.text = filteredText
                }
            }

            addDrawableChild(pos2Y)
        }

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addregion.pos1.z").append(":"),
            4 + textRenderer.getWidth(" 30000000 ") + 4,
            4 + textRenderer.fontHeight * 16,
            UIColors.PURPLE.color.rgb,
            true
        )

        if (pos2Z == null) {
            pos2Z = TextFieldWidget(
                textRenderer,
                4 + textRenderer.getWidth(" 30000000 ") + 4,
                4 + textRenderer.fontHeight * 17,
                textRenderer.getWidth(" 30000000 "),
                textRenderer.fontHeight + 8,
                Text.translatable("actions.ui.addregion.pos1.z")
            )
            pos2Z?.setMaxLength(9)
            pos2Z?.setChangedListener { newText ->
                if (!newText.matches(Regex("-?\\d*"))) {
                    val filteredText = newText.filter { it == '-' || it.isDigit() }
                    if (filteredText != newText)
                        pos2Z?.text = filteredText
                }
            }

            addDrawableChild(pos2Z)
        }

        // Add Button
        if (addButton == null) {
            addButton = ButtonWidget.builder(Text.translatable("actions.ui.triggers.add")) {
                val name = nameField?.text.toString()
                val pos1 = Vec3d(pos1X?.text?.toDoubleOrNull() ?: 0.0, pos1Y?.text?.toDoubleOrNull() ?: 0.0, pos1Z?.text?.toDoubleOrNull() ?: 0.0)
                val pos2 = Vec3d(pos2X?.text?.toDoubleOrNull() ?: 0.0, pos2Y?.text?.toDoubleOrNull() ?: 0.0, pos2Z?.text?.toDoubleOrNull() ?: 0.0)

                if (name.isNotEmpty() && RegionManager.instance.regions.none { it.name == name }) {
                    RegionManager.instance.addRegion(Region(name, pos1, pos2))
                    MinecraftClient.getInstance().setScreen(RegionsScreen(OtherScreen(MainScreen(GlobalUI.mainScreenParent))))
                }

                return@builder
            }
                .dimensions(
                    4,
                    4 + textRenderer.fontHeight * 20,
                    (4 + textRenderer.getWidth(" 30000000 ")) * 2,
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