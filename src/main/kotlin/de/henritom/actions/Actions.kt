package de.henritom.actions

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Actions : ModInitializer {
    private val logger = LoggerFactory.getLogger("actions")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")
	}
}