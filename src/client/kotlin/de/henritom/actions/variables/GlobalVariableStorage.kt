package de.henritom.actions.variables

import net.minecraft.client.MinecraftClient
import kotlin.math.sqrt

class GlobalVariableStorage {

    private val variables = mutableMapOf<String, Any>()

    init {
        update()
    }

    fun getVariable(name: String): Any? {
        return variables[name]
    }

    fun update(): GlobalVariableStorage {
        val client = MinecraftClient.getInstance()
        variables.clear()

        // Boolean
        variables["true"] = true
        variables["false"] = false

        // Math
        variables["math_pi"] = Math.PI
        variables["math_tau"] = Math.TAU
        variables["math_e"] = Math.E
        variables["math_sqrt2"] = sqrt(2.0)
        variables["math_random"] = Math.random() * 2 - 1
        variables["math_random_int"] = (Math.random() * Int.MAX_VALUE * 2 - Int.MAX_VALUE).toInt()

        // Player
        variables["player_air"] = client.player?.air ?: "Unknown"
        variables["player_air_max"] = client.player?.maxAir ?: "Unknown"
        variables["player_armor"] = client.player?.armor ?: "Unknown"
        variables["player_health"] = client.player?.health ?: "Unknown"
        variables["player_health_max"] = client.player?.maxHealth ?: "Unknown"
        variables["player_health_absorption"] = client.player?.absorptionAmount ?: "Unknown"
        variables["player_health_absorption_max"] = client.player?.maxAbsorption ?: "Unknown"
        variables["player_hunger"] = client.player?.hungerManager?.foodLevel ?: "Unknown"
        variables["player_level"] = client.player?.experienceLevel ?: "Unknown"
        variables["player_saturation"] = client.player?.hungerManager?.saturationLevel ?: "Unknown"
        variables["player_xp"] = client.player?.experienceProgress ?: "Unknown"
        variables["player_xp_total"] = client.player?.totalExperience ?: "Unknown"

        variables["player_x"] = client.player?.blockPos?.x ?: "Unknown"
        variables["player_y"] = client.player?.blockPos?.y ?: "Unknown"
        variables["player_z"] = client.player?.blockPos?.z ?: "Unknown"

        variables["player_yaw"] = client.player?.yaw ?: "Unknown"
        variables["player_pitch"] = client.player?.pitch ?: "Unknown"
        variables["player_facing"] = client.player?.horizontalFacing ?: "Unknown"
        variables["player_fov"] = client.options?.fov?.value ?: "Unknown"

        variables["player_biome"] = client.world?.getBiome(client.player?.blockPos)?.key?.get()?.value ?: "Unknown"

        variables["player_name"] = client.player?.name?.string ?: "Unknown"
        variables["player_uuid"] = client.player?.uuid?.toString() ?: "Unknown"

        variables["player_speed"] = client.player?.movementSpeed ?: "Unknown"
        variables["player_sneaking"] = client.player?.isSneaking ?: "Unknown"
        variables["player_sprinting"] = client.player?.isSprinting ?: "Unknown"
        variables["player_swimming"] = client.player?.isSwimming ?: "Unknown"
        variables["player_flying"] = client.player?.abilities?.flying ?: "Unknown"
        variables["player_flying_speed"] = client.player?.abilities?.flySpeed ?: "Unknown"
        variables["player_flying_allowed"] = client.player?.abilities?.allowFlying ?: "Unknown"
        variables["player_sleeping"] = client.player?.isSleeping ?: "Unknown"
        variables["player_riding"] = client.player?.isRiding ?: "Unknown"
        variables["player_climbing"] = client.player?.isClimbing ?: "Unknown"
        variables["player_on_ground"] = client.player?.isOnGround ?: "Unknown"
        variables["player_invisible"] = client.player?.isInvisible ?: "Unknown"

        variables["player_in_fire"] = client.player?.isOnFire ?: "Unknown"
        variables["player_in_water"] = client.player?.isTouchingWater ?: "Unknown"
        variables["player_in_lava"] = client.player?.isInLava ?: "Unknown"
        variables["player_in_rain"] = client.player?.isWet ?: "Unknown"

        client.player?.statusEffects?.map { it.effectType.type.name } ?: "None"

        variables["player_main_hand"] = client.player?.mainHandStack ?: "Unknown"
        variables["player_off_hand"] = client.player?.offHandStack ?: "Unknown"
        variables["player_helmet"] = client.player?.inventory?.armor?.get(3) ?: "Unknown"
        variables["player_chestplate"] = client.player?.inventory?.armor?.get(2) ?: "Unknown"
        variables["player_leggings"] = client.player?.inventory?.armor?.get(1) ?: "Unknown"
        variables["player_boots"] = client.player?.inventory?.armor?.get(0) ?: "Unknown"

        variables["player_light_level"] = client.world?.getLightLevel(client.player?.blockPos) ?: "Unknown"
        variables["player_block"] = client.world?.getBlockState(client.player?.blockPos)?.block?.name?.string ?: "Unknown"
        variables["player_block_details"] = client.world?.getBlockState(client.player?.blockPos) ?: "Unknown"

        variables["player_gamemode"] = client.interactionManager?.currentGameMode ?: "Unknown"

        // World
        variables["world_dimension"] = client.world?.dimension?.effects ?: "Unknown"
        variables["world_dimension_details"] = client.world?.dimension ?: "Unknown"

        variables["world_time"] = client.world?.time ?: "Unknown"
        variables["world_rain"] = client.world?.isRaining ?: "Unknown"
        variables["world_thunder"] = client.world?.isThundering ?: "Unknown"

        variables["world_difficulty"] = client.world?.difficulty ?: "Unknown"

        variables["world_spawn_x"] = client.world?.spawnPos?.x ?: "Unknown"
        variables["world_spawn_y"] = client.world?.spawnPos?.y ?: "Unknown"
        variables["world_spawn_z"] = client.world?.spawnPos?.z ?: "Unknown"

        // Client
        variables["client_language"] = client.languageManager?.language ?: "Unknown"
        variables["client_version"] = client.gameVersion ?: "Unknown"
        variables["client_fps"] = client.currentFps

        // Window
        variables["window_width"] = client.window?.width ?: "Unknown"
        variables["window_height"] = client.window?.height ?: "Unknown"
        variables["window_fullscreen"] = client.window?.isFullscreen ?: "Unknown"

        return this
    }
}