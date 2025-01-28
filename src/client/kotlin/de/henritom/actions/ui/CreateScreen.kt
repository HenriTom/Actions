package de.henritom.actions.ui

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class CreateScreen : Screen(Text.translatable("actions.ui.create.title")) {

    private var nameField: TextFieldWidget? = null
    private var idField: TextFieldWidget? = null
    private var createButton: ButtonWidget? = null
    private var callTrigger = true

    private var createText = "actions.ui.create.default";

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
            Text.translatable("actions.ui.create.title"),
            4 + textRenderer.getWidth(Text.translatable("actions.ui.main.title")) + textRenderer.getWidth(" "),
            4,
            UIColors.BLUE.color.rgb,
            true
        )

        // Name Textbox
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.create.name"),
            4,
            4 + textRenderer.fontHeight * 3,
            UIColors.BLUE.color.rgb,
            true
        )

        if (nameField == null)
            nameField = TextFieldWidget(
                textRenderer,
                4,
                4 + textRenderer.fontHeight * 4,
                textRenderer.getWidth(" ________________ "),
                textRenderer.fontHeight + 8,
                Text.translatable("actions.ui.create.name")
            )
        nameField?.setMaxLength(16)

        addDrawableChild(nameField)

        // Preferred ID Textbox
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.create.id"),
            4,
            5 + textRenderer.fontHeight * 7,
            UIColors.BLUE.color.rgb,
            true
        )

        if (idField == null)
            idField = TextFieldWidget(
                textRenderer,
                4,
                5 + textRenderer.fontHeight * 8,
                textRenderer.getWidth(" 2147483647 "),
                textRenderer.fontHeight + 8,
                Text.translatable("actions.ui.create.id")
            )
        idField?.setMaxLength(10)
        idField?.setChangedListener { newText ->
            if (!newText.matches(Regex("\\d*")))
                idField?.text = newText.filter { it.isDigit() }
        }

        addDrawableChild(idField)

        // Call Trigger Checkbox
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.create.call_trigger"),
            textRenderer.fontHeight + 8,
            7 + textRenderer.fontHeight * 11,
            UIColors.BLUE.color.rgb,
            true
        )

        context.drawText(
            textRenderer,
            Text.literal(if (callTrigger) "☑" else "☐"),
            4,
            7 + textRenderer.fontHeight * 11,
            if (mouseX in 4..4 + textRenderer.fontHeight && mouseY in 7 + textRenderer.fontHeight * 11..7 + textRenderer.fontHeight * 12) UIColors.BLUE.color.rgb else UIColors.WHITE.color.rgb,
            true
        )

        // Create Button
        createButton = ButtonWidget.builder(Text.translatable("actions.ui.create.create")) {
            val name = nameField?.text ?: ""
            val preferredID = idField?.text?.toIntOrNull()?: 0

            when (ActionManager.instance.createAction(name, callTrigger)) {
                1 -> {
                    val action = ActionManager.instance.getActionByNameID(name)

                    if (action == null) {
                        createText = "actions.ui.create.failed"
                        return@builder
                    }

                    if (action.id != preferredID)
                        action.id = if (ActionManager.instance.actions.none { it.id == preferredID })
                            preferredID
                        else
                            ActionManager.instance.getNextAvailableID()

                    MinecraftClient.getInstance().setScreen(ManageScreen())
                }
                2 -> createText = "actions.ui.create.already_used"
                3 -> createText = "actions.ui.create.start_with_letter"
                4 -> createText = "actions.ui.create.min_length"
            }
        }
            .dimensions(4, 1 + textRenderer.fontHeight * 13, textRenderer.getWidth(Text.translatable("actions.ui.create.create")) + textRenderer.getWidth("  "), textRenderer.fontHeight + 8)
            .build()

        addDrawableChild(createButton)

        context.drawText(
            textRenderer,
            Text.translatable(createText),
            4,
            4 + textRenderer.fontHeight * 15,
            UIColors.BLUE.color.rgb,
            true
        )

        // Version
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.version", FabricLoader.getInstance().getModContainer("actions").get().metadata.version.toString()),
            4,
            4 + textRenderer.fontHeight,
            UIColors.BLUE.color.rgb,
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

        if (mouseX.toInt() in 4..4 + textRenderer.fontHeight && mouseY.toInt() in 7 + textRenderer.fontHeight * 11..7 + textRenderer.fontHeight * 12)
            callTrigger = !callTrigger

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