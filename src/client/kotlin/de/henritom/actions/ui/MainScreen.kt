package de.henritom.actions.ui

import de.henritom.actions.config.ConfigManager
import de.henritom.actions.scheduler.ActionScheduler
import de.henritom.actions.scheduler.SchedulerHelper
import de.henritom.actions.util.MessageUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.awt.Color
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class MainScreen : Screen(Text.translatable("actions.ui.main.title")) {

    private val white = Color(255, 255, 255, 255)
    private val background = Color(32, 32, 32, 128)
    private val blue = Color(115, 206, 245)
    private val purple = Color(97, 53, 190)

    private var scrollConsole = 0
    private var scrollRunning = 0

    override fun init() {
        scrollConsole = (MessageUtil.consoleLog.size - (height - ((height / 24) * 6 + width / 24 + (height / 24) * 6 + 4 + width / 24)) / (textRenderer.fontHeight + 2)).coerceAtLeast(0) + 2
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        // Background
        context.fill(0, 0, width, height, background.rgb)

        // Title
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.title"),
            4,
            4,
            white.rgb,
            true
        )

        // Version
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.version", FabricLoader.getInstance().getModContainer("actions").get().metadata.version.toString()),
            4,
            4 + textRenderer.fontHeight,
            blue.rgb,
            true
        )

        // Drag and Drop
        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.drag_and_drop"),
            4,
            height - textRenderer.fontHeight - 4,
            white.rgb,
            true
        )

        // Buttons
        // Button 1
        context.fill(
            width / 24,
            width / 24,
            (width / 24) * 8,
            (height / 24) * 6,
            if (mouseX in width / 24..(width / 24) * 8 && mouseY in width / 24..(height / 24) * 6) background.brighter().rgb else background.rgb
        )

        // Button 2
        context.fill(
            (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24,
            width / 24,
            (width / 1.5 + width / 24).toInt() - width / 24,
            (height / 24) * 6,
            if (mouseX in (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY in width / 24..(height / 24) * 6) background.brighter().rgb else background.rgb
        )

        // Button 3
        context.fill(
            width / 24,
            (height / 24) * 6 + width / 24,
            (width / 24) * 8,
            (height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24,
            if (mouseX in width / 24..(width / 24) * 8 && mouseY in (height / 24) * 6 + width / 24..(height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24) background.brighter().rgb else background.rgb
        )

        // Button 4
        context.fill(
            (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24,
            (height / 24) * 6 + width / 24,
            (width / 1.5 + width / 24).toInt() - width / 24,
            (height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24,
            if (mouseX in (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY in (height / 24) * 6 + width / 24..(height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24) background.brighter().rgb else background.rgb
        )

        // Console
        context.fill(
            width / 24,
            (height / 24) * 6 + width / 24 + (height / 24) * 6,
            (width / 1.5 + width / 24).toInt() - width / 24,
            height - width / 24,
            background.rgb
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.console").styled {
                it.withBold(true)
                it.withUnderline(true)
            },
            width / 24 + 4,
            (height / 24) * 6 + width / 24 + (height / 24) * 6 + 4,
            white.rgb,
            true
        )

        // Console Text
        for (i in scrollConsole until MessageUtil.consoleLog.size)
            if ((height / 24) * 6 + width / 24 + (height / 24) * 6 + (i - scrollConsole + 3) * (textRenderer.fontHeight + 2) + 1 in width / 24..height - width / 24)
                context.drawText(
                    textRenderer,
                    Text.literal(MessageUtil.consoleLog[i]),
                    width / 24 + 4,
                    (height / 24) * 6 + width / 24 + (height / 24) * 6 + (i - scrollConsole + 2) * (textRenderer.fontHeight + 2) + 1,
                    white.rgb,
                    true
                )


        // Console Clear
        context.fill(
            width / 24,
            height - width / 24 - textRenderer.fontHeight - 1,
            (width / 1.5 + width / 24).toInt() - width / 24,
            height - width / 24,
            if (mouseX in width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY in height - width / 24 - textRenderer.fontHeight - 1..height - width / 24) purple.rgb else purple.darker().rgb
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.console.clear"),
            ((width / 24) + ((width / 1.5 + width / 24).toInt() - width / 24) - (textRenderer.getWidth(Text.translatable("actions.ui.main.console.clear")))) / 2,
            height - width / 24 - textRenderer.fontHeight,
            white.rgb,
            true
        )

        // Running Actions
        context.fill(
            (width / 1.5 + width / 24).toInt(),
            width / 24,
            width - width / 24,
            height - width / 24,
            background.rgb
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.running_actions").styled {
                it.withBold(true)
                it.withUnderline(true)
            },
            (width / 1.5 + width / 24).toInt() + 4,
            width / 24 + 4,
            white.rgb,
            true
        )
        
        // End All Button
        context.fill(
            (width / 1.5 + width / 24).toInt(),
            height - width / 24 - textRenderer.fontHeight - 1,
            width - width / 24,
            height - width / 24,
            if (mouseX in (width / 1.5 + width / 24).toInt()..(width - width / 24) && mouseY in (height - width / 24 - textRenderer.fontHeight - 1)..(height - width / 24)) purple.rgb else purple.darker().rgb
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.running_actions.end_all"),
            (((width / 1.5 + width / 24).toInt()) + (width - width / 24) - (textRenderer.getWidth(Text.translatable("actions.ui.main.running_actions.end_all")))) / 2,
            height - width / 24 - textRenderer.fontHeight,
            white.rgb,
            true
        )

        // Running Actions List
        for (i in scrollRunning until ActionScheduler.runningActions.size)
            if (ActionScheduler.runningActions.size > i) { // Needed to fix crash, when resizing
                // Action
                if (width / 24 + (i - scrollRunning + 3) * (textRenderer.fontHeight + 2) + 1 in width / 24..height - width / 24) {
                    context.drawText(
                        textRenderer,
                        Text.translatable(
                            "actions.ui.main.running_actions.it",
                            ActionScheduler.runningActions[i].action.name,
                            ActionScheduler.runningActions[i].runID,
                            ActionScheduler.runningActions[i].currentTask,
                            ActionScheduler.runningActions[i].action.tasks.size
                        ),
                        (width / 1.5 + width / 24).toInt() + 4,
                        width / 24 + (i - scrollRunning + 2) * (textRenderer.fontHeight + 2) + 1,
                        white.rgb,
                        true
                    )

                    // End Button
                    context.fill(
                        width - width / 24 - textRenderer.getWidth(Text.translatable("actions.ui.main.running_actions.end")) - 8,
                        width / 24 + (i - scrollRunning + 2) * (textRenderer.fontHeight + 2),
                        width - width / 24,
                        width / 24 + (i - scrollRunning + 3) * (textRenderer.fontHeight + 2) - 1,
                        if (isMouseOverEndButton(mouseX, mouseY, i - scrollRunning)) purple.rgb else purple.darker().rgb
                    )

                    // End Text
                    context.drawText(
                        textRenderer,
                        Text.translatable("actions.ui.main.running_actions.end"),
                        width - width / 24 - textRenderer.getWidth(Text.translatable("actions.ui.main.running_actions.end")) - 4,
                        width / 24 + (i - scrollRunning + 2) * (textRenderer.fontHeight + 2) + 1,
                        white.rgb,
                        true
                    )
                }
            }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        super.mouseClicked(mouseX, mouseY, button)

        for (i in 0 until ActionScheduler.runningActions.size)
            if (isMouseOverEndButton(mouseX.toInt(), mouseY.toInt(), i) && i + scrollRunning < ActionScheduler.runningActions.size) {
                ActionScheduler.runningActions[i + scrollRunning].end()
                return true
            }

        if (mouseX.toInt() in (width / 1.5 + width / 24).toInt()..(width - width / 24) && mouseY.toInt() in (height - width / 24 - textRenderer.fontHeight - 1)..height - width / 24)
            scrollRunning = SchedulerHelper().endAllActions()

        if (mouseX.toInt() in width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY.toInt() in height - width / 24 - textRenderer.fontHeight - 1..height - width / 24)
            scrollConsole = MessageUtil().clearConsole()

        return false
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

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)

        if (mouseX.toInt() in (width / 1.5 + width / 24).toInt()..(width - width / 24) && mouseY.toInt() in width / 24..height - width / 24) {
            scrollRunning = (scrollRunning - verticalAmount.toInt()).coerceIn(0, ActionScheduler.runningActions.size.coerceAtLeast(1) - 1)
            return true
        }

        if (mouseX.toInt() in width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY.toInt() in (height / 24) * 6 + width / 24 + (height / 24) * 6..height - width / 24) {
            scrollConsole = (scrollConsole - verticalAmount.toInt()).coerceIn(0, (MessageUtil.consoleLog.size - (height - ((height / 24) * 6 + width / 24 + (height / 24) * 6 + 4 + width / 24)) / (textRenderer.fontHeight + 2)).coerceAtLeast(0) + 2)
            return true
        }

        return false
    }

    private fun isMouseOverEndButton(mouseX: Int, mouseY: Int, i: Int): Boolean {
        val buttonX1 = width - width / 24 - textRenderer.getWidth(Text.translatable("actions.ui.main.running_actions.end")) - 8
        val buttonX2 = width - width / 24
        val buttonY1 = width / 24 + (i + 2) * (textRenderer.fontHeight + 2)
        val buttonY2 = width / 24 + (i + 3) * (textRenderer.fontHeight + 2) - 1

        return mouseX in buttonX1..buttonX2 && mouseY in buttonY1..buttonY2
    }
}