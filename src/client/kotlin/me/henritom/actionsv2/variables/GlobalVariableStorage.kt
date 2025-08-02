package me.henritom.actionsv2.variables

import net.minecraft.client.MinecraftClient
import net.minecraft.entity.EquipmentSlot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.log2
import kotlin.math.sqrt

object GlobalVariableStorage {

    fun init() {
        updateConstants()
        update()
    }

    private val variables = mutableMapOf<String, Any>()
    val vars: Map<String, Any> get() {
        update()
        return variables
    }

    fun getVariable(name: String): Any? {
        update()
        return variables[name]
    }

    fun updateConstants() {
        // Boolean
        variables["true"] = true
        variables["false"] = false
        variables["yes"] = true
        variables["no"] = false
        variables["on"] = true
        variables["off"] = false
        variables["enabled"] = true
        variables["disabled"] = false

        // Colors
        variables["color_0"] = "§0"
        variables["color_1"] = "§1"
        variables["color_2"] = "§2"
        variables["color_3"] = "§3"
        variables["color_4"] = "§4"
        variables["color_5"] = "§5"
        variables["color_6"] = "§6"
        variables["color_7"] = "§7"
        variables["color_8"] = "§8"
        variables["color_9"] = "§9"

        variables["color_a"] = "§a"
        variables["color_b"] = "§b"
        variables["color_c"] = "§c"
        variables["color_d"] = "§d"
        variables["color_e"] = "§e"
        variables["color_f"] = "§f"

        variables["format_l"] = "§l"
        variables["format_o"] = "§o"
        variables["format_n"] = "§n"
        variables["format_m"] = "§m"
        variables["format_r"] = "§r"

        variables["color_black"] = "§0"
        variables["color_dark_blue"] = "§1"
        variables["color_dark_green"] = "§2"
        variables["color_dark_turquoise"] = "§3"
        variables["color_dark_red"] = "§4"
        variables["color_purple"] = "§5"
        variables["color_dark_yellow"] = "§6"
        variables["color_light_gray"] = "§7"
        variables["color_dark_gray"] = "§8"
        variables["color_light_blue"] = "§9"

        variables["color_light_green"] = "§a"
        variables["color_light_turquoise"] = "§b"
        variables["color_light_red"] = "§c"
        variables["color_magenta"] = "§d"
        variables["color_light_yellow"] = "§e"
        variables["color_white"] = "§f"

        variables["format_bold"] = "§l"
        variables["format_italic"] = "§o"
        variables["format_underlined"] = "§n"
        variables["format_strikethrough"] = "§m"
        variables["format_reset"] = "§r"

        // Chat
        variables["chat_newline"] = "\n"
        variables["chat_space"] = " "

        // Time
        variables["time_tick"] = 1.0 / 20.0
        variables["time_second"] = 1
        variables["time_minute"] = 60
        variables["time_hour"] = 3600
        variables["time_day"] = 86400
        variables["time_week"] = 604800

        // Math
        variables["math_pi"] = Math.PI
        variables["math_tau"] = Math.TAU
        variables["math_e"] = Math.E
        variables["math_sqrt2"] = sqrt(2.0)
        variables["math_sqrt3"] = sqrt(3.0)
        variables["math_golden_ratio"] = (1 + sqrt(5.0)) / 2
        variables["math_ln2"] = ln(2.0)
        variables["math_ln10"] = ln(10.0)
        variables["math_log2e"] = log2(Math.E)
        variables["math_log10e"] = log10(Math.E)

        // Symbols
        variables["symbol_arrow"] = "→"
        variables["symbol_check"] = "✔"
        variables["symbol_cross"] = "✖"
        variables["symbol_star"] = "★"
        variables["symbol_info"] = "ℹ"
        variables["symbol_warning"] = "⚠"
        variables["symbol_question"] = "❓"
        variables["symbol_right"] = "►"
        variables["symbol_left"] = "◄"
        variables["symbol_up"] = "▲"
        variables["symbol_down"] = "▼"
        variables["symbol_bullet"] = "•"
        variables["symbol_dot"] = "·"

        // Logic
        variables["and"] = "&&"
        variables["or"] = "||"
        variables["not"] = "!"

        // Units
        variables["kb"] = 1000
        variables["kib"] = 1024
        variables["mb"] = 1000 * 1000
        variables["mib"] = 1024 * 1024
        variables["gb"] = 1000 * 1000 * 1000
        variables["gib"] = 1024 * 1024 * 1024

        // Actions
        variables["actions_prefix"] = "§8» §f§lActions §8┃ §7"
    }

    fun update() {
        val client = MinecraftClient.getInstance()

        // Math
        variables["math_random"] = Math.random() * 2 - 1
        variables["math_random_int"] = (Math.random() * Int.MAX_VALUE * 2 - Int.MAX_VALUE).toInt()
        variables["math_random_0_100"] = (Math.random() * 100).toInt()

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

        variables["player_chunk_x"] = client.player?.chunkPos?.x ?: "Unknown"
        variables["player_chunk_z"] = client.player?.chunkPos?.z ?: "Unknown"

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
        variables["player_in_rain"] = (client.player?.isTouchingWaterOrRain == true && client.player?.isTouchingWater != true)
        variables["player_in_wall"] = client.player?.isInsideWall ?: "Unknown"

        variables["player_effects"] = client.player?.statusEffects?.map { it.effectType.key.get().value.toString() } ?: "None"

        variables["player_main_hand"] = client.player?.mainHandStack ?: "Unknown"
        variables["player_off_hand"] = client.player?.offHandStack ?: "Unknown"
        variables["player_helmet"] = client.player?.getEquippedStack(EquipmentSlot.HEAD) ?: "Unknown"
        variables["player_chestplate"] = client.player?.getEquippedStack(EquipmentSlot.CHEST) ?: "Unknown"
        variables["player_leggings"] = client.player?.getEquippedStack(EquipmentSlot.LEGS) ?: "Unknown"
        variables["player_boots"] = client.player?.getEquippedStack(EquipmentSlot.FEET) ?: "Unknown"
        variables["player_inventory_empty"] = client.player?.inventory?.isEmpty ?: "Unknown"
        variables["player_inventory_size"] = client.player?.inventory?.size() ?: "Unknown"
        variables["player_selected_slot"] = client.player?.inventory?.selectedSlot ?: "Unknown"
        variables["player_selected_item"] = client.player?.mainHandStack?.name?.string ?: "Unknown"

        variables["player_light_level"] = client.world?.getLightLevel(client.player?.blockPos) ?: "Unknown"
        variables["player_block"] = client.world?.getBlockState(client.player?.blockPos)?.block?.name?.string ?: "Unknown"
        variables["player_block_details"] = client.world?.getBlockState(client.player?.blockPos) ?: "Unknown"

        variables["player_gamemode"] = client.interactionManager?.currentGameMode ?: "Unknown"

        // World
        variables["world_dimension"] = client.world?.dimension?.effects ?: "Unknown"
        variables["world_dimension_details"] = client.world?.dimension ?: "Unknown"

        variables["world_time"] = client.world?.time ?: "Unknown"
        variables["world_time_string"] = client.world?.timeOfDay?.let { "%02d:%02d".format(((it / 1000) + 6) % 24, (it / 50) * 3 % 60) } ?: "Unknown"
        variables["world_time_day"] = client.world?.timeOfDay?.div(24000) ?: "Unknown"
        variables["world_time_hour"] = (client.world?.timeOfDay?.div(1000)?.plus(6))?.rem(24) ?: "Unknown"
        variables["world_time_minute"] = client.world?.timeOfDay?.div(50)?.times(3)?.rem(60) ?: "Unknown"

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

        // Time
        variables["time_millis"] = System.currentTimeMillis()
        variables["time_nanos"] = System.nanoTime()

        variables["time_date"] = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date())
        variables["time_date_year"] = SimpleDateFormat("yyyy").format(Date())
        variables["time_date_month"] = SimpleDateFormat("MM").format(Date())
        variables["time_date_day"] = SimpleDateFormat("dd").format(Date())
        variables["time_date_hour"] = SimpleDateFormat("HH").format(Date())
        variables["time_date_minute"] = SimpleDateFormat("mm").format(Date())
        variables["time_date_second"] = SimpleDateFormat("ss").format(Date())

        // System
        variables["system_os"] = System.getProperty("os.name") ?: "Unknown"
        variables["system_arch"] = System.getProperty("os.arch") ?: "Unknown"
        variables["system_java_version"] = System.getProperty("java.version") ?: "Unknown"
        variables["system_user"] = System.getProperty("user.name") ?: "Unknown"
    }
}