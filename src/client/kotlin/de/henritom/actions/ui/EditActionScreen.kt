package de.henritom.actions.ui

import de.henritom.actions.actions.Action
import de.henritom.actions.actions.ActionEditManager
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

class EditActionScreen : Screen(Text.translatable("actions.ui.edit.title")) {

    private var nameField: TextFieldWidget? = null
    private var idField: TextFieldWidget? = null

    private var renameButton: ButtonWidget? = null
    private var reidButton: ButtonWidget? = null
    private var clearButton: ButtonWidget? = null
    private var disableButton: ButtonWidget? = null
    private var triggersButton: ButtonWidget? = null
    private var tasksButton: ButtonWidget? = null

    private var action: Action? = null

    fun asAction(action: Action): EditActionScreen {
        this.action = action
        return this
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        if (action == null)
            return

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
            Text.translatable("actions.ui.edit.title"),
            4 + textRenderer.getWidth(Text.translatable("actions.ui.main.title")) + textRenderer.getWidth(" "),
            4,
            UIColors.YELLOW.color.rgb,
            true
        )

        // Name Textbox
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.edit.name"),
            4,
            4 + textRenderer.fontHeight * 3,
            UIColors.YELLOW.color.rgb,
            true
        )

        if (nameField == null) {
            nameField = TextFieldWidget(
                textRenderer,
                4,
                4 + textRenderer.fontHeight * 4,
                textRenderer.getWidth(" ________________ "),
                textRenderer.fontHeight + 8,
                Text.literal(action!!.name)
            )
            nameField?.text = action!!.name
        }
        nameField?.setMaxLength(16)

        addDrawableChild(nameField)

        renameButton = ButtonWidget.builder(Text.translatable("actions.ui.edit.change")) {
            val name = nameField?.text ?: ""

            if (name.isNotBlank() && name.length >= 3 && name.length <= 16 && name.first().isLetter()) {
                val newAction = ActionEditManager.instance.renameAction(action!!, name)
                if (newAction != null) {
                    MessageUtil().printTranslatable("actions.ui.edit.changed_name", name)
                    MinecraftClient.getInstance().setScreen(this.asAction(newAction))
                    return@builder;
                }
            }

            MessageUtil().printTranslatable("actions.ui.edit.invalid_name")
        }
            .dimensions(textRenderer.getWidth(" ________________ ") + 8, 4 + textRenderer.fontHeight * 4, textRenderer.getWidth(Text.translatable("actions.ui.edit.change")) + textRenderer.getWidth("  "), textRenderer.fontHeight + 8)
            .build()

        addDrawableChild(renameButton)

        // Id Textbox
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.edit.id"),
            4,
            4 + textRenderer.fontHeight * 7,
            UIColors.YELLOW.color.rgb,
            true
        )

        if (idField == null) {
            idField = TextFieldWidget(
                textRenderer,
                4,
                4 + textRenderer.fontHeight * 8,
                textRenderer.getWidth(" 2147483647 "),
                textRenderer.fontHeight + 8,
                Text.literal(action!!.id.toString())
            )
            idField?.text = action!!.id.toString()
        }
        idField?.setMaxLength(10)
        idField?.setChangedListener { newText ->
            if (!newText.matches(Regex("\\d*")))
                idField?.text = newText.filter { it.isDigit() }
        }

        addDrawableChild(idField)

        reidButton = ButtonWidget.builder(Text.translatable("actions.ui.edit.change")) {
            val name = idField?.text ?: ""

            val id = name.toIntOrNull()
            if (id == null || id < 0 || !ActionManager.instance.actions.none { it.id == id }) {
                MessageUtil().printTranslatable("actions.ui.edit.invalid_id")
                return@builder
            }

            action!!.id = name.toInt()
            MessageUtil().printTranslatable("actions.ui.edit.changed_id", name)
            ConfigManager().saveAction(action!!, true)
            return@builder
        }
            .dimensions(textRenderer.getWidth(" 2147483647 ") + 8, 4 + textRenderer.fontHeight * 8, textRenderer.getWidth(Text.translatable("actions.ui.edit.change")) + textRenderer.getWidth("  "), textRenderer.fontHeight + 8)
            .build()

        addDrawableChild(reidButton)

        // Author Text
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.edit.author"),
            4,
            4 + textRenderer.fontHeight * 11,
            UIColors.YELLOW.color.rgb,
            true
        )

        context.drawText(
            textRenderer,
            Text.literal(action!!.author),
            4,
            9 + textRenderer.fontHeight * 12,
            UIColors.WHITE.color.rgb,
            true
        )

        if (action!!.author != "%Unknown%") {
            clearButton = ButtonWidget.builder(Text.translatable("actions.ui.edit.clear")) {
                ActionEditManager.instance.removeAuthor(action!!)
                MessageUtil().printTranslatable("actions.ui.edit.cleared_author")
                MinecraftClient.getInstance().setScreen(this.asAction(action!!))
                return@builder;
            }
                .dimensions(
                    textRenderer.getWidth(Text.literal(action!!.author)) + 8,
                    4 + textRenderer.fontHeight * 12,
                    textRenderer.getWidth(Text.translatable("actions.ui.edit.clear")) + textRenderer.getWidth("  "),
                    textRenderer.fontHeight + 8
                )
                .build()

            addDrawableChild(clearButton)
        }

        // Disable Button
        disableButton = ButtonWidget.builder(Text.translatable("actions.ui.edit.disable")) {
            if (ActionEditManager.instance.disableAction(action!!)) {
                MessageUtil().printTranslatable("actions.action.disabled", action!!.name)
                ConfigManager().reloadActions()
                MinecraftClient.getInstance().setScreen(ManageScreen())
            } else
                MessageUtil().printTranslatable("actions.action.not_disabled", action!!.name)
        }
            .dimensions(
                4,
                4 + textRenderer.fontHeight * 14,
                textRenderer.getWidth(Text.translatable("actions.ui.edit.disable")) + textRenderer.getWidth("  "),
                textRenderer.fontHeight + 8
            )
            .build()

        addDrawableChild(disableButton)

        // Triggers Button
        triggersButton = ButtonWidget.builder(Text.translatable("actions.ui.edit.triggers", action!!.triggers.size)) {
            MinecraftClient.getInstance().setScreen(TriggersScreen().asAction(action!!))
        }
            .dimensions(
                4,
                4 + textRenderer.fontHeight * 17,
                textRenderer.getWidth(Text.translatable("actions.ui.edit.triggers")) + textRenderer.getWidth("  "),
                textRenderer.fontHeight + 8
            )
            .build()

        addDrawableChild(triggersButton)

        // Tasks Button
        tasksButton = ButtonWidget.builder(Text.translatable("actions.ui.edit.tasks", action!!.tasks.size)) {
            MinecraftClient.getInstance().setScreen(TasksScreen().asAction(action!!))
        }
            .dimensions(
                4,
                4 + textRenderer.fontHeight * 20,
                textRenderer.getWidth(Text.translatable("actions.ui.edit.tasks")) + textRenderer.getWidth("  "),
                textRenderer.fontHeight + 8
            )
            .build()

        addDrawableChild(tasksButton)

        // Version
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.version", FabricLoader.getInstance().getModContainer("actions").get().metadata.version.toString()),
            4,
            4 + textRenderer.fontHeight,
            UIColors.YELLOW.color.rgb,
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

        return true
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)

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