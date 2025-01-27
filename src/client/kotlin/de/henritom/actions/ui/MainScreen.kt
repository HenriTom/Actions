package de.henritom.actions.ui

import de.henritom.actions.config.ConfigManager
import de.henritom.actions.scheduler.ActionScheduler
import de.henritom.actions.scheduler.SchedulerHelper
import de.henritom.actions.util.MessageUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class MainScreen : Screen(Text.translatable("actions.ui.main.title")) {

    private var scrollConsole = 0
    private var scrollRunning = 0

    override fun init() {
        scrollConsole = (MessageUtil.consoleLog.size - (height - ((height / 24) * 6 + width / 24 + (height / 24) * 6 + 4 + width / 24)) / (textRenderer.fontHeight + 2)).coerceAtLeast(0) + 2
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

        // Buttons
        // Button 1
        context.fillGradient(
            width / 24,
            width / 24,
            (width / 24) * 8,
            (height / 24) * 6,
            if (mouseX in width / 24..(width / 24) * 8 && mouseY in width / 24..(height / 24) * 6) UIColors.BLUE.color.brighter().brighter().rgb else UIColors.BLUE.color.brighter().rgb,
            if (mouseX in width / 24..(width / 24) * 8 && mouseY in width / 24..(height / 24) * 6) UIColors.BLUE.color.rgb else UIColors.BLUE.color.darker().rgb,
        )

        drawTextWithWrap(
            context,
            textRenderer,
            Text.translatable("actions.ui.main.button1.title"),
            width / 24 + 4,
            width / 24 + 4,
            (width / 24) * 8 - width / 24 - 8,
            UIColors.WHITE.color.rgb,
            true
        )

        // Button 2
        context.fillGradient(
            (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24,
            width / 24,
            (width / 1.5 + width / 24).toInt() - width / 24,
            (height / 24) * 6,
            if (mouseX in (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY in width / 24..(height / 24) * 6) UIColors.YELLOW.color.brighter().brighter().rgb else UIColors.YELLOW.color.brighter().rgb,
            if (mouseX in (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY in width / 24..(height / 24) * 6) UIColors.YELLOW.color.rgb else UIColors.YELLOW.color.darker().rgb
        )

        drawTextWithWrap(
            context,
            textRenderer,
            Text.translatable("actions.ui.main.button2.title"),
            (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24 + 4,
            width / 24 + 4,
            (width / 24) * 8 - width / 24 - 8,
            UIColors.WHITE.color.rgb,
            true
        )

        // Button 3
        context.fillGradient(
            width / 24,
            (height / 24) * 6 + width / 24,
            (width / 24) * 8,
            (height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24,
            if (mouseX in width / 24..(width / 24) * 8 && mouseY in (height / 24) * 6 + width / 24..(height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24) UIColors.RED.color.brighter().brighter().rgb else UIColors.RED.color.brighter().rgb,
            if (mouseX in width / 24..(width / 24) * 8 && mouseY in (height / 24) * 6 + width / 24..(height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24) UIColors.RED.color.rgb else UIColors.RED.color.darker().rgb
        )

        drawTextWithWrap(
            context,
            textRenderer,
            Text.translatable("actions.ui.main.button3.title"),
            width / 24 + 4,
            (height / 24) * 6 + width / 24 + 4,
            (width / 24) * 8 - width / 24 - 8,
            UIColors.WHITE.color.rgb,
            true
        )

        // Button 4
        context.fillGradient(
            (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24,
            (height / 24) * 6 + width / 24,
            (width / 1.5 + width / 24).toInt() - width / 24,
            (height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24,
            if (mouseX in (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY in (height / 24) * 6 + width / 24..(height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24) UIColors.PURPLE.color.brighter().brighter().rgb else UIColors.PURPLE.color.brighter().rgb,
            if (mouseX in (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY in (height / 24) * 6 + width / 24..(height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24) UIColors.PURPLE.color.rgb else UIColors.PURPLE.color.darker().rgb
        )

        drawTextWithWrap(
            context,
            textRenderer,
            Text.translatable("actions.ui.main.button4.title"),
            (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24 + 4,
            (height / 24) * 6 + width / 24 + 4,
            (width / 24) * 8 - width / 24 - 8,
            UIColors.WHITE.color.rgb,
            true
        )

        // Console
        context.fill(
            width / 24,
            (height / 24) * 6 + width / 24 + (height / 24) * 6,
            (width / 1.5 + width / 24).toInt() - width / 24,
            height - width / 24,
            UIColors.BACKGROUND.color.rgb
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.console", (scrollConsole + 14).coerceAtMost(MessageUtil.consoleLog.size), MessageUtil.consoleLog.size).styled {
                it.withBold(true)
                it.withUnderline(true)
            },
            width / 24 + 4,
            (height / 24) * 6 + width / 24 + (height / 24) * 6 + 4,
            UIColors.WHITE.color.rgb,
            true
        )

        // Console Text
        for (i in scrollConsole until MessageUtil.consoleLog.size)
            if ((height / 24) * 6 + width / 24 + (height / 24) * 6 + (i - scrollConsole + 4) * (textRenderer.fontHeight + 2) + 1 in width / 24..height - width / 24)
                context.drawText(
                    textRenderer,
                    Text.literal(MessageUtil.consoleLog[i]),
                    width / 24 + 4,
                    (height / 24) * 6 + width / 24 + (height / 24) * 6 + (i - scrollConsole + 2) * (textRenderer.fontHeight + 2) + 1,
                    UIColors.WHITE.color.rgb,
                    true
                )


        // Console Clear
        context.fill(
            width / 24,
            height - width / 24 - textRenderer.fontHeight - 1,
            (width / 1.5 + width / 24).toInt() - width / 24,
            height - width / 24,
            if (mouseX in width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY in height - width / 24 - textRenderer.fontHeight - 1..height - width / 24) UIColors.PURPLE.color.rgb else UIColors.PURPLE.color.darker().rgb
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.console.clear"),
            ((width / 24) + ((width / 1.5 + width / 24).toInt() - width / 24) - (textRenderer.getWidth(Text.translatable("actions.ui.main.console.clear")))) / 2,
            height - width / 24 - textRenderer.fontHeight,
            UIColors.WHITE.color.rgb,
            true
        )

        // Running Actions
        context.fill(
            (width / 1.5 + width / 24).toInt(),
            width / 24,
            width - width / 24,
            height - width / 24,
            UIColors.BACKGROUND.color.rgb
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.running_actions", (scrollRunning + 1).coerceAtMost(ActionScheduler.runningActions.size), ActionScheduler.runningActions.size).styled {
                it.withBold(true)
                it.withUnderline(true)
            },
            (width / 1.5 + width / 24).toInt() + 4,
            width / 24 + 4,
            UIColors.WHITE.color.rgb,
            true
        )
        
        // End All Button
        context.fill(
            (width / 1.5 + width / 24).toInt(),
            height - width / 24 - textRenderer.fontHeight - 1,
            width - width / 24,
            height - width / 24,
            if (mouseX in (width / 1.5 + width / 24).toInt()..(width - width / 24) && mouseY in (height - width / 24 - textRenderer.fontHeight - 1)..(height - width / 24)) UIColors.PURPLE.color.rgb else UIColors.PURPLE.color.darker().rgb
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.main.running_actions.end_all"),
            (((width / 1.5 + width / 24).toInt()) + (width - width / 24) - (textRenderer.getWidth(Text.translatable("actions.ui.main.running_actions.end_all")))) / 2,
            height - width / 24 - textRenderer.fontHeight,
            UIColors.WHITE.color.rgb,
            true
        )

        // Running Actions List
        for (i in scrollRunning until ActionScheduler.runningActions.size)
            if (ActionScheduler.runningActions.size > i) { // Needed to fix crash, when resizing
                // Action
                if (width / 24 + (i - scrollRunning + 4) * (textRenderer.fontHeight + 2) + 1 in width / 24..height - width / 24) {
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
                        UIColors.WHITE.color.rgb,
                        true
                    )

                    // End Button
                    context.fill(
                        width - width / 24 - textRenderer.getWidth(Text.translatable("actions.ui.main.running_actions.end")) - 8,
                        width / 24 + (i - scrollRunning + 2) * (textRenderer.fontHeight + 2),
                        width - width / 24,
                        width / 24 + (i - scrollRunning + 3) * (textRenderer.fontHeight + 2) - 1,
                        if (isMouseOverEndButton(mouseX, mouseY, i - scrollRunning)) UIColors.PURPLE.color.rgb else UIColors.PURPLE.color.darker().rgb
                    )

                    // End Text
                    context.drawText(
                        textRenderer,
                        Text.translatable("actions.ui.main.running_actions.end"),
                        width - width / 24 - textRenderer.getWidth(Text.translatable("actions.ui.main.running_actions.end")) - 4,
                        width / 24 + (i - scrollRunning + 2) * (textRenderer.fontHeight + 2) + 1,
                        UIColors.WHITE.color.rgb,
                        true
                    )
                }
            }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        super.mouseClicked(mouseX, mouseY, button)

        // Single End Buttons
        if (mouseX.toInt() in (width / 1.5 + width / 24).toInt()..(width - width / 24) && mouseY.toInt() in width / 24..height - width / 24) {
            for (i in 0 until ActionScheduler.runningActions.size)
                if (isMouseOverEndButton(mouseX.toInt(), mouseY.toInt(), i) && i + scrollRunning < ActionScheduler.runningActions.size) {
                    ActionScheduler.runningActions[i + scrollRunning].end()
                    return true
                }
        }

        // End All Actions Button
        if (mouseX.toInt() in (width / 1.5 + width / 24).toInt()..(width - width / 24) && mouseY.toInt() in (height - width / 24 - textRenderer.fontHeight - 1)..height - width / 24)
            scrollRunning = SchedulerHelper().endAllActions()

        // Clear Console Button
        if (mouseX.toInt() in width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY.toInt() in height - width / 24 - textRenderer.fontHeight - 1..height - width / 24)
            scrollConsole = MessageUtil().clearConsole()

        // Button 1 (Add Action)
        if (mouseX.toInt() in width / 24..(width / 24) * 8 && mouseY.toInt() in width / 24..(height / 24) * 6)
            MinecraftClient.getInstance().setScreen(CreateScreen())

        // Button 2 (Edit Action)
        if (mouseX.toInt() in (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY.toInt() in width / 24..(height / 24) * 6)
            MinecraftClient.getInstance().setScreen(ManageScreen())

        // Button 3 (Coming Soon...)
        if (mouseX.toInt() in width / 24..(width / 24) * 8 && mouseY.toInt() in (height / 24) * 6 + width / 24..(height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24)
            MinecraftClient.getInstance().setScreen(ComingScreen())

        // Button 4 (Settings)
        if (mouseX.toInt() in (width / 1.5 + width / 24).toInt() - width / 24 - (width / 24) * 8 + width / 24..(width / 1.5 + width / 24).toInt() - width / 24 && mouseY.toInt() in (height / 24) * 6 + width / 24..(height / 24) * 6 + width / 24 + (height / 24) * 6 - width / 24)
            MinecraftClient.getInstance().setScreen(SettingsScreen())

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

    private fun drawTextWithWrap(context: DrawContext, textRenderer: TextRenderer, text: Text, x: Int, y: Int, maxWidth: Int, color: Int, shadow: Boolean) {
        val words = text.string.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = ""

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"

            if (textRenderer.getWidth(testLine) <= maxWidth)
                currentLine = testLine
            else {
                lines.add(currentLine)
                currentLine = word
            }
        }

        if (currentLine.isNotEmpty()) lines.add(currentLine)

        var currentY = y
        for (line in lines) {
            context.drawText(textRenderer, line, x, currentY, color, shadow)
            currentY += textRenderer.fontHeight + 2
        }
    }
}