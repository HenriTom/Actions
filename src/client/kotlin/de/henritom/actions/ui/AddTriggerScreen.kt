package de.henritom.actions.ui

import de.henritom.actions.actions.Action
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.util.MessageUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.CyclingButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class AddTriggerScreen : Screen(Text.translatable("actions.ui.coming.title")) {

    private var typeButton: CyclingButtonWidget<TriggerEnum>? = null
    private var valueField: TextFieldWidget? = null
    private var addButton: ButtonWidget? = null

    private var action: Action? = null

    fun asAction(action: Action): AddTriggerScreen {
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
            Text.translatable("actions.ui.addtrigger.title"),
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

        // Type Button
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addtask.type").append(":"),
            4,
            5 + textRenderer.fontHeight * 3,
            UIColors.YELLOW.color.rgb,
            true
        )

        if (typeButton == null && action != null)
            typeButton = CyclingButtonWidget.builder { triggerEnum: TriggerEnum -> Text.literal(triggerEnum.name) }
                .values(ActionManager().getAvailableTriggersForAction(action!!))
                .initially(TriggerEnum.entries.first())
                .build(4, 8 + textRenderer.fontHeight * 4, textRenderer.getWidth(" : ") + textRenderer.getWidth(Text.translatable("actions.ui.addtask.type")) + TriggerEnum.entries.toTypedArray().maxOf { textRenderer.getWidth(it.toString()) } + 16, textRenderer.fontHeight + 8, Text.translatable("actions.ui.addtask.type"))

        addDrawableChild(typeButton)

        // Value Textbox
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addtask.value").append(":"),
            4,
            5 + textRenderer.fontHeight * 7,
            UIColors.YELLOW.color.rgb,
            true
        )

        if (valueField == null)
            valueField = TextFieldWidget(
                textRenderer,
                4,
                5 + textRenderer.fontHeight * 8,
                width - 8,
                textRenderer.fontHeight + 8,
                Text.translatable("actions.ui.addtask.value")
            )
        valueField?.setMaxLength(8192)
        valueField?.width = width - 8

        addDrawableChild(valueField)

        // Add Button
        addButton = ButtonWidget.builder(Text.translatable("actions.ui.addtask.add")) {
            val trigger = typeButton?.value ?: TriggerEnum.entries.first()
            val value = valueField?.text ?: ""

            if (action == null) {
                MessageUtil().printTranslatable("actions.action.not_found", "%Unknown%")
                return@builder
            }

            when (ActionEditManager.instance.addTrigger(action!!, trigger)) {
                1 -> {
                    action!!.triggers.last().value = value

                    MessageUtil().printTranslatable("actions.trigger.added.initial_value", trigger.name, action!!.name, value)
                    MinecraftClient.getInstance().setScreen(TriggersScreen().asAction(action!!))
                }
                2 -> {
                    MessageUtil().printTranslatable("actions.trigger.multiple_triggers")
                    return@builder
                }
            }
        }
            .dimensions(4, 5 + textRenderer.fontHeight * 11, textRenderer.getWidth(Text.translatable("actions.ui.addtask.add")) + textRenderer.getWidth("  ") + 16, textRenderer.fontHeight + 8)
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