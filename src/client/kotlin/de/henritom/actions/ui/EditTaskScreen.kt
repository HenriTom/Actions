package de.henritom.actions.ui

import de.henritom.actions.config.ConfigManager
import de.henritom.actions.tasks.Task
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

class EditTaskScreen : Screen(Text.translatable("actions.ui.coming.title")) {

    private var valueField: TextFieldWidget? = null
    private var editButton: ButtonWidget? = null

    private var task: Task? = null

    fun asTask(task: Task): EditTaskScreen {
        this.task = task
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
            Text.translatable("actions.ui.edittask.title"),
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

        // Value Textbox
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addtask.value").append(":"),
            4,
            5 + textRenderer.fontHeight * 3,
            UIColors.YELLOW.color.rgb,
            true
        )

        if (valueField == null) {
            valueField = TextFieldWidget(
                textRenderer,
                4,
                5 + textRenderer.fontHeight * 4,
                width - 8,
                textRenderer.fontHeight + 8,
                Text.translatable("actions.ui.addtask.value")
            )
            valueField?.text = task?.value.toString()
        }
        valueField?.setMaxLength(8192)
        valueField?.width = width - 8

        addDrawableChild(valueField)

        // Edit Button
        editButton = ButtonWidget.builder(Text.translatable("actions.ui.edittask.edit")) {
            val value = valueField?.text ?: ""

            if (task == null) {
                MessageUtil().printTranslatable("actions.task.not_found", "%Unknown%")
                return@builder
            }

            task!!.value = value

            MessageUtil().printTranslatable("actions.task.edited", task!!.type.name, task!!.id.toString(), value)
            MinecraftClient.getInstance().setScreen(TasksScreen().asAction(task!!.action))
        }
            .dimensions(4, 5 + textRenderer.fontHeight * 7, textRenderer.getWidth(Text.translatable("actions.ui.edittask.edit")) + textRenderer.getWidth("  ") + 16, textRenderer.fontHeight + 8)
            .build()

        addDrawableChild(editButton)

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
        MessageUtil().printTranslatable("actions.file.reloaded.actions")
    }
}