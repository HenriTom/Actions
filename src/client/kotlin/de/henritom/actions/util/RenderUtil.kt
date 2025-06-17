package de.henritom.actions.util

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

class RenderUtil {

    companion object {
        fun drawTextWithWrap(
            context: DrawContext,
            textRenderer: TextRenderer,
            text: Text,
            x: Int,
            y: Int,
            maxWidth: Int,
            color: Int,
            shadow: Boolean
        ) {
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
}