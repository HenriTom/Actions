package de.henritom.actions.ui.impl

import de.henritom.actions.actions.Action
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.triggers.settings.InvUpdateEnum
import de.henritom.actions.triggers.settings.ReceiveMessageEnum
import de.henritom.actions.ui.GlobalUI
import de.henritom.actions.ui.UIColors
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

class AddTriggerScreen(val parent: Screen?) : Screen(Text.translatable("actions.ui.coming.title")) {

    private var typeButton: CyclingButtonWidget<TriggerEnum>? = null
    private var subtypeButton: CyclingButtonWidget<Enum<*>>? = null
    private var valueField: TextFieldWidget? = null
    private var amountField: TextFieldWidget? = null
    private var addButton: ButtonWidget? = null

    private var action: Action? = null

    private var amountPlus = 0

    fun asAction(action: Action): AddTriggerScreen {
        this.action = action
        return this
    }

    override fun init() {
        super.init()
        typeButton = null
        subtypeButton = null
        valueField = null
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

        if (typeButton == null && action != null) {
            typeButton = CyclingButtonWidget.builder { triggerEnum: TriggerEnum -> Text.literal(triggerEnum.name) }
                .values(ActionManager().getAvailableTriggersForAction(action!!))
                .initially(TriggerEnum.entries.first())
                .build(
                    4,
                    8 + textRenderer.fontHeight * 4,
                    textRenderer.getWidth(" : ") + textRenderer.getWidth(Text.translatable("actions.ui.addtask.type")) + TriggerEnum.entries.toTypedArray().maxOf { textRenderer.getWidth(it.toString()) } + 16,
                    textRenderer.fontHeight + 8,
                    Text.translatable("actions.ui.addtask.type"))

            addDrawableChild(typeButton)
        }

        // Subtype Button
        when (typeButton?.value) {
            TriggerEnum.RECEIVE_MESSAGE -> {
                if (subtypeButton == null && action != null) {
                    subtypeButton =
                        CyclingButtonWidget.builder { receiveMessageEnum: Enum<*> -> Text.literal(receiveMessageEnum.name) }
                            .values(ReceiveMessageEnum.entries)
                            .initially(ReceiveMessageEnum.entries.first())
                            .build(
                                8 + textRenderer.getWidth(" : ") + textRenderer.getWidth(Text.translatable("actions.ui.addtask.type")) + TriggerEnum.entries.toTypedArray()
                                    .maxOf { textRenderer.getWidth(it.toString()) } + 16,
                                8 + textRenderer.fontHeight * 4,
                                textRenderer.getWidth(" : ") + textRenderer.getWidth(Text.translatable("actions.ui.addtask.filter")) + ReceiveMessageEnum.entries.toTypedArray()
                                    .maxOf { textRenderer.getWidth(it.toString()) } + 16,
                                textRenderer.fontHeight + 8,
                                Text.translatable("actions.ui.addtask.filter")
                            )

                    subtypeButton!!.visible = true
                    addDrawableChild(subtypeButton)
                }
            }

            TriggerEnum.INV_UPDATE -> {
                if (subtypeButton == null && action != null) {
                    subtypeButton =
                        CyclingButtonWidget.builder { invUpdateEnum: Enum<*> -> Text.literal(invUpdateEnum.name) }
                            .values(InvUpdateEnum.entries)
                            .initially(InvUpdateEnum.entries.first())
                            .build(
                                8 + textRenderer.getWidth(" : ") + textRenderer.getWidth(Text.translatable("actions.ui.addtask.type")) + TriggerEnum.entries.toTypedArray()
                                    .maxOf { textRenderer.getWidth(it.toString()) } + 16,
                                8 + textRenderer.fontHeight * 4,
                                textRenderer.getWidth(" : ") + textRenderer.getWidth(Text.translatable("actions.ui.addtask.filter")) + InvUpdateEnum.entries.toTypedArray()
                                    .maxOf { textRenderer.getWidth(it.toString()) } + 16,
                                textRenderer.fontHeight + 8,
                                Text.translatable("actions.ui.addtask.filter")
                            )

                    amountPlus = textRenderer.getWidth(" : ") + textRenderer.getWidth(Text.translatable("actions.ui.addtask.filter")) + InvUpdateEnum.entries.toTypedArray()
                        .maxOf { textRenderer.getWidth(it.toString()) } + 16

                    subtypeButton!!.visible = true
                    addDrawableChild(subtypeButton)
                }
            }

            else -> {
                if (subtypeButton != null) {
                    subtypeButton!!.visible = false
                    remove(subtypeButton)
                    subtypeButton = null
                }
            }
        }

        // Amount Textbox
        when (typeButton?.value) {
            TriggerEnum.INV_UPDATE -> {
                if (subtypeButton?.value == InvUpdateEnum.CONTAINS_LESS || subtypeButton?.value == InvUpdateEnum.CONTAINS_MORE || subtypeButton?.value == InvUpdateEnum.CONTAINS_EXACT) {
                    context.drawText(
                        textRenderer,
                        Text.translatable("actions.ui.addtask.amount").append(":"),
                        12 + textRenderer.getWidth(" : ") + textRenderer.getWidth(Text.translatable("actions.ui.addtask.type")) + TriggerEnum.entries.toTypedArray()
                            .maxOf { textRenderer.getWidth(it.toString()) } + 16 + amountPlus,
                        5 + textRenderer.fontHeight * 3,
                        UIColors.YELLOW.color.rgb,
                        true
                    )

                    if (amountField == null) {
                        amountField = TextFieldWidget(
                            textRenderer,
                            12 + textRenderer.getWidth(" : ") + textRenderer.getWidth(Text.translatable("actions.ui.addtask.type")) + TriggerEnum.entries.toTypedArray()
                                .maxOf { textRenderer.getWidth(it.toString()) } + 16 + amountPlus,
                            8 + textRenderer.fontHeight * 4,
                            textRenderer.getWidth(" 2147483647 "),
                            textRenderer.fontHeight + 8,
                            Text.translatable("actions.ui.addtask.amount")
                        )
                        amountField?.setMaxLength(8192)
                        amountField?.setMaxLength(10)
                        amountField?.setChangedListener { newText ->
                            if (!newText.matches(Regex("\\d*")))
                                amountField?.text = newText.filter { it.isDigit() }
                        }

                        amountField?.visible = true
                        addDrawableChild(amountField)
                    }
                } else {
                    if (amountField != null) {
                        amountField?.visible = false
                        remove(amountField)
                        amountField = null
                    }
                }
            }

            else -> {
                if (amountField != null) {
                    amountField?.visible = false
                    remove(amountField)
                    amountField = null
                }
            }
        }

        // Value Textbox
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.addtask.value").append(":"),
            4,
            5 + textRenderer.fontHeight * 7,
            UIColors.YELLOW.color.rgb,
            true
        )

        if (valueField == null) {
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
        }

        // Add Button
        if (addButton == null) {
            addButton = ButtonWidget.builder(Text.translatable("actions.ui.addtask.add")) {
                val trigger = typeButton?.value ?: TriggerEnum.entries.first()
                var value = valueField?.text ?: ""

                if (action == null) {
                    MessageUtil().printTranslatable("actions.action.not_found", "%Unknown%")
                    return@builder
                }

                if (trigger == TriggerEnum.RECEIVE_MESSAGE && subtypeButton != null)
                    value = subtypeButton?.value?.name + "-" + value

                if (trigger == TriggerEnum.INV_UPDATE && subtypeButton != null && amountField != null)
                    value = subtypeButton?.value?.name + "-" + value + "-" + amountField?.text?.toInt()

                else if (trigger == TriggerEnum.INV_UPDATE && subtypeButton != null)
                    value = subtypeButton?.value?.name.toString() + "-" + value

                else if (trigger == TriggerEnum.INV_UPDATE && subtypeButton != null && subtypeButton?.value == InvUpdateEnum.ANY)
                    value = subtypeButton?.value?.name.toString()

                when (ActionEditManager.instance.addTrigger(action!!, trigger)) {
                    1 -> {
                        action!!.triggers.last().value = value

                        MessageUtil().printTranslatable(
                            "actions.trigger.added.initial_value",
                            trigger.name,
                            action!!.name,
                            value
                        )
                        MinecraftClient.getInstance().setScreen(TriggersScreen(EditActionScreen(ManageScreen(MainScreen(GlobalUI.mainScreenParent))).asAction(action!!)).asAction(action!!))
                    }

                    2 -> {
                        MessageUtil().printTranslatable("actions.trigger.multiple_triggers")
                        return@builder
                    }

                    else -> return@builder
                }
            }
                .dimensions(
                    4,
                    5 + textRenderer.fontHeight * 11,
                    textRenderer.getWidth(Text.translatable("actions.ui.addtask.add")) + textRenderer.getWidth("  ") + 16,
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
        MessageUtil().printTranslatable("actions.file.reloaded.actions")
    }

    override fun close() {
        this.client?.setScreen(this.parent)
    }
}