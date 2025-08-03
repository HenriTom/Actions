package de.henritom.actions.ui.impl

import de.henritom.actions.config.ConfigManager
import de.henritom.actions.ui.GlobalUI
import de.henritom.actions.ui.UIColors
import de.henritom.actions.util.MessageUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ConfirmLinkScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.util.Util
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class OtherScreen(val parent: Screen?) : Screen(Text.translatable("actions.ui.other.title")) {

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
            Text.translatable("actions.ui.other.title"),
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

        // Top Buttons
        // Button 1 (Sharing)
        val hover1 = mouseX in 8..(width / 3 - 4) && mouseY in (4 + textRenderer.fontHeight * 3)..(4 + textRenderer.fontHeight * 8)
        context.fillGradient(
            8,
            4 + textRenderer.fontHeight * 3,
            width / 3 - 4,
            4 + textRenderer.fontHeight * 8,
            if (hover1) UIColors.BLUE.color.brighter().rgb else UIColors.BLUE.color.darker().rgb,
            if (hover1) UIColors.BLUE.color.darker().rgb else UIColors.BLUE.color.darker().darker().rgb
        )

        context.drawTextWithShadow(textRenderer, Text.translatable("actions.ui.sharing.title"), 16, 12 + textRenderer.fontHeight * 3, UIColors.WHITE.color.rgb)

        // Button 2 (Discord)
        val hover2 = mouseX in (width / 3 + 4)..(width / 3 + 4 + width / 3 - 8) && mouseY in (4 + textRenderer.fontHeight * 3)..(4 + textRenderer.fontHeight * 8)
        context.fillGradient(
            width / 3 + 4, 4 + textRenderer.fontHeight * 3,
            width / 3 + 4 + width / 3 - 8, 4 + textRenderer.fontHeight * 8,
            if (hover2) UIColors.DISCORD_BLURPLE.color.brighter().brighter().rgb else UIColors.DISCORD_BLURPLE.color.brighter().rgb,
            if (hover2) UIColors.DISCORD_BLURPLE.color.brighter().rgb else UIColors.DISCORD_BLURPLE.color.darker().rgb
        )

        context.drawTextWithShadow(textRenderer, Text.translatable("actions.ui.discord.title"), width / 3 + 12, 12 + textRenderer.fontHeight * 3, UIColors.WHITE.color.rgb)

        // Button 3 (Settings)
        val hover3 = mouseX in (width / 3 + 4 + width / 3 + 4 - 4)..(width / 3 - 4 + width / 3 - 4 + width / 3) && mouseY in (4 + textRenderer.fontHeight * 3)..(4 + textRenderer.fontHeight * 8)
        context.fillGradient(
            width / 3 + 4 + width / 3 + 4 - 4,
            4 + textRenderer.fontHeight * 3,
            width / 3 - 4 + width / 3 - 4 + width / 3,
            4 + textRenderer.fontHeight * 8,
            if (hover3) UIColors.PURPLE.color.brighter().brighter().rgb else UIColors.PURPLE.color.brighter().rgb,
            if (hover3) UIColors.PURPLE.color.brighter().rgb else UIColors.PURPLE.color.darker().rgb
        )

        context.drawTextWithShadow(textRenderer, Text.translatable("actions.ui.settings.title"), width / 3 + 4 + width / 3 + 8, 12 + textRenderer.fontHeight * 3, UIColors.WHITE.color.rgb)

        // List
        // Button 1 (Regions)
        context.fill(
            8,
            3 + textRenderer.fontHeight * 9,
            width - 8,
            4 + textRenderer.fontHeight * 12,
            if (mouseX in 8..(width - 8) && mouseY in (3 + textRenderer.fontHeight * 9)..(4 + textRenderer.fontHeight * 12)) UIColors.BACKGROUND.color.darker().rgb else UIColors.BACKGROUND.color.darker().darker().rgb
        )

        context.drawText(
            textRenderer,
            Text.translatable("actions.ui.regions.title"),
            16,
            4 + textRenderer.fontHeight * 10,
            UIColors.WHITE.color.rgb,
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

        // Top Buttons
        // Button 1
        if (mouseX.toInt() in 8..(width / 3 - 4) && mouseY.toInt() in (4 + textRenderer.fontHeight * 3)..(4 + textRenderer.fontHeight * 8))
            MinecraftClient.getInstance().setScreen(SharingScreen(OtherScreen(MainScreen(GlobalUI.mainScreenParent))))

        // Button 2
        if (mouseX.toInt() in (width / 3 + 4)..(width / 3 + 4 + width / 3 - 8) && mouseY.toInt() in (4 + textRenderer.fontHeight * 3)..(4 + textRenderer.fontHeight * 8))
            MinecraftClient.getInstance().setScreen(ConfirmLinkScreen({ bl: Boolean ->
                if (bl)
                    Util.getOperatingSystem().open(URI("https://discord.gg/XdHBJKTvxJ"))

                MinecraftClient.getInstance().setScreen(this) }, "https://discord.gg/XdHBJKTvxJ", true))

        // Button 3
        if (mouseX.toInt() in (width / 3 + 4 + width / 3 + 4 - 4)..(width / 3 - 4 + width / 3 - 4 + width / 3) && mouseY.toInt() in (4 + textRenderer.fontHeight * 3)..(4 + textRenderer.fontHeight * 8))
            MinecraftClient.getInstance().setScreen(SettingsScreen(OtherScreen(MainScreen(GlobalUI.mainScreenParent))))

        // List
        // Button 1
        if (mouseX.toInt() in 8..(width - 8) && mouseY.toInt() in (3 + textRenderer.fontHeight * 9)..(4 + textRenderer.fontHeight * 12))
            MinecraftClient.getInstance().setScreen(RegionsScreen(OtherScreen(MainScreen(GlobalUI.mainScreenParent))))

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