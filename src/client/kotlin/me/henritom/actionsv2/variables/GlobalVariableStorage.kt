package me.henritom.actionsv2.variables

import me.henritom.actionsv2.util.RegionUtil
import me.henritom.actionsv2.util.SessionUtil
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.registry.Registries
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.log2
import kotlin.math.sqrt

@Suppress("HardCodedStringLiteral")
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
        variables["math_deg_to_rad"] = Math.PI / 180
        variables["math_rad_to_deg"] = 180 / Math.PI

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
        variables["player_last_damage_source"] = client.player?.recentDamageSource?.name ?: "Unknown"

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
        variables["player_region"] = RegionUtil.getCurrentRegions().joinToString(", ")

        variables["player_name"] = client.player?.name?.string ?: "Unknown"
        variables["player_uuid"] = client.player?.uuid?.toString() ?: "Unknown"

        variables["player_height"] = client.player?.height ?: "Unknown"
        variables["player_width"] = client.player?.width ?: "Unknown"

        variables["player_age"] = client.player?.age ?: "Unknown"

        variables["player_cape_x"] = client.player?.capeX ?: "Unknown"
        variables["player_cape_y"] = client.player?.capeY ?: "Unknown"
        variables["player_cape_z"] = client.player?.capeZ ?: "Unknown"

        variables["player_input"] = client.player?.input?.playerInput ?: "Unknown"
        variables["player_last_input"] = client.player?.lastPlayerInput ?: "Unknown"

        variables["player_attack_cooldown"] = client.player?.attackCooldownProgressPerTick ?: "Unknown"

        variables["player_swing"] = client.player?.handSwinging ?: "Unknown"
        variables["player_swing_progress"] = client.player?.handSwingProgress ?: "Unknown"
        variables["player_swing_last_progress"] = client.player?.lastHandSwingProgress ?: "Unknown"
        variables["player_swing_ticks"] = client.player?.handSwingTicks ?: "Unknown"

        variables["player_death_time"] = client.player?.deathTime ?: "Unknown"

        variables["player_sleep_timer"] = client.player?.sleepTimer ?: "Unknown"
        variables["player_hit_timer"] = client.player?.playerHitTimer ?: "Unknown"

        variables["player_scoreboard_team_name"] = client.player?.scoreboardTeam?.name ?: "Unknown"
        variables["player_scoreboard_team_display_name"] = client.player?.scoreboardTeam?.displayName ?: "Unknown"
        variables["player_scoreboard_team_names"] = client.player?.scoreboard?.teamNames ?: "Unknown"

        variables["player_eye_height"] = client.player?.standingEyeHeight ?: "Unknown"
        variables["player_eye_pos_y"] = client.player?.eyeY ?: "Unknown"

        variables["player_velocity_total"] = client.player?.velocity?.let { sqrt(it.x * it.x + it.y * it.y + it.z * it.z) } ?: "Unknown"

        variables["player_velocity_x"] = client.player?.velocity?.x ?: "Unknown"
        variables["player_velocity_y"] = client.player?.velocity?.y ?: "Unknown"
        variables["player_velocity_z"] = client.player?.velocity?.z ?: "Unknown"

        variables["player_speed"] = client.player?.movementSpeed ?: "Unknown"
        variables["player_forward_speed"] = client.player?.forwardSpeed ?: "Unknown"
        variables["player_sideways_speed"] = client.player?.sidewaysSpeed ?: "Unknown"
        variables["player_upward_speed"] = client.player?.upwardSpeed ?: "Unknown"
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
        variables["player_ground_collision"] = client.player?.groundCollision ?: "Unknown"
        variables["player_invisible"] = client.player?.isInvisible ?: "Unknown"
        variables["player_custom_name_visible"] = client.player?.isCustomNameVisible ?: "Unknown"
        variables["player_armor_visibility"] = client.player?.armorVisibility ?: "Unknown"
        variables["player_underwater_visibility"] = client.player?.underwaterVisibility ?: "Unknown"

        variables["player_in_fire"] = client.player?.isOnFire ?: "Unknown"
        variables["player_in_water"] = client.player?.isTouchingWater ?: "Unknown"
        variables["player_in_lava"] = client.player?.isInLava ?: "Unknown"
        variables["player_in_rain"] = (client.player?.isTouchingWaterOrRain == true && client.player?.isTouchingWater != true)
        variables["player_in_wall"] = client.player?.isInsideWall ?: "Unknown"

        variables["player_effects"] = client.player?.statusEffects?.map { it.effectType.key.get().value.toString() } ?: "Unknown"
        variables["player_effects_details"] = client.player?.statusEffects?.map { "${it.effectType.key.get().value} (${it.duration}s, lvl ${it.amplifier})" } ?: "Unknown"

        variables["player_main_hand"] = client.player?.mainHandStack?.toString()?.split(" ")?.getOrNull(1) ?: "Unknown"
        variables["player_off_hand"] = client.player?.offHandStack?.toString()?.split(" ")?.getOrNull(1) ?: "Unknown"
        variables["player_helmet"] = client.player?.getEquippedStack(EquipmentSlot.HEAD)?.toString()?.split(" ")?.getOrNull(1) ?: "Unknown"
        variables["player_chestplate"] = client.player?.getEquippedStack(EquipmentSlot.CHEST)?.toString()?.split(" ")?.getOrNull(1) ?: "Unknown"
        variables["player_leggings"] = client.player?.getEquippedStack(EquipmentSlot.LEGS)?.toString()?.split(" ")?.getOrNull(1) ?: "Unknown"
        variables["player_boots"] = client.player?.getEquippedStack(EquipmentSlot.FEET)?.toString()?.split(" ")?.getOrNull(1) ?: "Unknown"

        variables["player_amount_main_hand"] = client.player?.mainHandStack?.toString()?.split(" ")?.getOrNull(0) ?: "Unknown"
        variables["player_amount_off_hand"] = client.player?.offHandStack?.toString()?.split(" ")?.getOrNull(0) ?: "Unknown"
        variables["player_amount_helmet"] = client.player?.getEquippedStack(EquipmentSlot.HEAD)?.toString()?.split(" ")?.getOrNull(0) ?: "Unknown"
        variables["player_amount_chestplate"] = client.player?.getEquippedStack(EquipmentSlot.CHEST) ?.toString()?.split(" ")?.getOrNull(0) ?: "Unknown"
        variables["player_amount_leggings"] = client.player?.getEquippedStack(EquipmentSlot.LEGS)?.toString()?.split(" ")?.getOrNull(0) ?: "Unknown"
        variables["player_amount_boots"] = client.player?.getEquippedStack(EquipmentSlot.FEET)?.toString()?.split(" ")?.getOrNull(0) ?: "Unknown"

        variables["player_inventory_empty"] = client.player?.inventory?.isEmpty ?: "Unknown"
        variables["player_inventory_size"] = client.player?.inventory?.size() ?: "Unknown"
        variables["player_inventory_count"] = client.player?.inventory?.count { !it.isEmpty } ?: "Unknown"

        variables["player_selected_slot"] = client.player?.inventory?.selectedSlot ?: "Unknown"
        variables["player_selected_item"] = client.player?.mainHandStack?.name?.string ?: "Unknown"

        variables["player_light_level"] = client.world?.getLightLevel(client.player?.blockPos) ?: "Unknown"
        variables["player_block"] = client.world?.getBlockState(client.player?.blockPos)?.block?.name?.string ?: "Unknown"
        variables["player_block_details"] = client.world?.getBlockState(client.player?.blockPos) ?: "Unknown"

        variables["player_gamemode"] = client.interactionManager?.currentGameMode ?: "Unknown"

        // Crosshair Target
        variables["crosshair_target_type"] = client.crosshairTarget?.type ?: "Unknown"
        variables["crosshair_target_position"] = client.crosshairTarget?.pos ?: "Unknown"
        variables["crosshair_target_position_x"] = client.crosshairTarget?.pos?.x ?: "Unknown"
        variables["crosshair_target_position_y"] = client.crosshairTarget?.pos?.y ?: "Unknown"
        variables["crosshair_target_position_z"] = client.crosshairTarget?.pos?.z ?: "Unknown"

        // Crosshair Block
        variables["crosshair_block_exists"] = (client.crosshairTarget as? BlockHitResult)?.let { client.world?.getBlockState(it.blockPos)?.isAir == false } == true
        variables["crosshair_block_id"] = (client.crosshairTarget as? BlockHitResult)?.let { hit -> client.world?.getBlockState(hit.blockPos)?.block?.let { block -> Registries.BLOCK.getId(block).toString() } } ?: "Unknown"
        variables["crosshair_block_key"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.getBlockState(pos)?.block?.translationKey } ?: "Unknown"

        variables["crosshair_block_hardness"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.getBlockState(pos)?.block?.hardness } ?: "Unknown"
        variables["crosshair_block_blast_resistance"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.getBlockState(pos)?.block?.blastResistance } ?: "Unknown"
        variables["crosshair_block_burnable"] = (client.crosshairTarget as? BlockHitResult)?.let { client.world?.getBlockState(it.blockPos)?.isBurnable } ?: "Unknown"

        variables["crosshair_block_solid"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.let { world -> world.getBlockState(pos)?.isSolidBlock(world, pos) } } ?: "Unknown"
        variables["crosshair_block_fullcube"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.let { world -> world.getBlockState(pos)?.isFullCube(world, pos) } } ?: "Unknown"
        variables["crosshair_block_opaque_fullcube"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.let { world -> world.getBlockState(pos)?.isOpaqueFullCube } } ?: "Unknown"
        variables["crosshair_block_collision_empty"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.let { world -> world.getBlockState(pos)?.getCollisionShape(world, pos)?.isEmpty } } ?: "Unknown"
        variables["crosshair_block_replaceable"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.getBlockState(pos)?.isReplaceable } ?: "Unknown"

        variables["crosshair_block_liquid"] = (client.crosshairTarget as? BlockHitResult)?.let { client.world?.getBlockState(it.blockPos)?.fluidState?.isEmpty == false } ?: "Unknown"
        variables["crosshair_block_fluid_level"] = (client.crosshairTarget as? BlockHitResult)?.let { client.world?.getBlockState(it.blockPos)?.fluidState?.level ?: "Unknown" } ?: "Unknown"

        variables["crosshair_block_slipperiness"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.getBlockState(pos)?.block?.slipperiness } ?: "Unknown"
        variables["crosshair_block_velocity_multiplier"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.getBlockState(pos)?.block?.velocityMultiplier } ?: "Unknown"

        variables["crosshair_block_luminance"] = (client.crosshairTarget as? BlockHitResult)?.let { client.world?.getBlockState(it.blockPos)?.luminance ?: "Unknown" } ?: "Unknown"
        variables["crosshair_block_light_opacity"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.let { world -> world.getBlockState(pos)?.opacity ?: "Unknown" } } ?: "Unknown"

        variables["crosshair_block_emits_redstone"] = (client.crosshairTarget as? BlockHitResult)?.let { client.world?.getBlockState(it.blockPos)?.emitsRedstonePower() } ?: "Unknown"
        variables["crosshair_block_redstone_received"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.getReceivedRedstonePower(pos) ?: "Unknown"} ?: "Unknown"

        variables["crosshair_block_has_block_entity"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> (client.world?.getBlockEntity(pos) != null) } ?: "Unknown"
        variables["crosshair_block_is_container"] = (client.crosshairTarget as? BlockHitResult)?.blockPos?.let { pos -> client.world?.getBlockEntity(pos) is Inventory } ?: "Unknown"

        // Crosshair Entity
        variables["crosshair_entity_exists"] = (client.crosshairTarget as? EntityHitResult)?.entity != null
        variables["crosshair_entity_id"] = (client.crosshairTarget as? EntityHitResult)?.let { hit -> Registries.ENTITY_TYPE.getId(hit.entity.type).toString() } ?: "Unknown"
        variables["crosshair_entity_type_key"] = (client.crosshairTarget as? EntityHitResult)?.entity?.type?.translationKey ?: "Unknown"

        variables["crosshair_entity_is_player"] = ((client.crosshairTarget as? EntityHitResult)?.entity is PlayerEntity)
        variables["crosshair_entity_is_animal"] = ((client.crosshairTarget as? EntityHitResult)?.entity is AnimalEntity)
        variables["crosshair_entity_is_mob"] = ((client.crosshairTarget as? EntityHitResult)?.entity is MobEntity)

        variables["crosshair_entity_custom_name"] = (client.crosshairTarget as? EntityHitResult)?.entity?.name?.string ?: "Unknown"
        variables["crosshair_entity_uuid"] = (client.crosshairTarget as? EntityHitResult)?.entity?.uuid?.toString() ?: "Unknown"

        variables["crosshair_entity_block_x"] = (client.crosshairTarget as? EntityHitResult)?.entity?.blockX ?: "Unknown"
        variables["crosshair_entity_block_y"] = (client.crosshairTarget as? EntityHitResult)?.entity?.blockY ?: "Unknown"
        variables["crosshair_entity_block_z"] = (client.crosshairTarget as? EntityHitResult)?.entity?.blockZ ?: "Unknown"

        variables["crosshair_entity_is_living"] = ((client.crosshairTarget as? EntityHitResult)?.entity is LivingEntity)
        variables["crosshair_entity_health"] = ((client.crosshairTarget as? EntityHitResult)?.entity as? LivingEntity)?.health ?: "Unknown"
        variables["crosshair_entity_max_health"] = ((client.crosshairTarget as? EntityHitResult)?.entity as? LivingEntity)?.maxHealth ?: "Unknown"
        variables["crosshair_entity_armor"] = ((client.crosshairTarget as? EntityHitResult)?.entity as? LivingEntity)?.armor ?: "Unknown"
        variables["crosshair_entity_absorption"] = ((client.crosshairTarget as? EntityHitResult)?.entity as? LivingEntity)?.absorptionAmount ?: "Unknown"

        variables["crosshair_entity_velocity_x"] = (client.crosshairTarget as? EntityHitResult)?.entity?.velocity?.x ?: "Unknown"
        variables["crosshair_entity_velocity_y"] = (client.crosshairTarget as? EntityHitResult)?.entity?.velocity?.y ?: "Unknown"
        variables["crosshair_entity_velocity_z"] = (client.crosshairTarget as? EntityHitResult)?.entity?.velocity?.z ?: "Unknown"

        variables["crosshair_entity_distance"] = (client.crosshairTarget as? EntityHitResult)?.entity?.let { entity -> client.player?.distanceTo(entity) } ?: "Unknown"

        variables["crosshair_entity_on_ground"] = (client.crosshairTarget as? EntityHitResult)?.entity?.isOnGround ?: "Unknown"

        variables["crosshair_entity_sneaking"] = (client.crosshairTarget as? EntityHitResult)?.entity?.isSneaking ?: "Unknown"
        variables["crosshair_entity_sprinting"] = (client.crosshairTarget as? EntityHitResult)?.entity?.isSprinting ?: "Unknown"
        variables["crosshair_entity_glowing"] = (client.crosshairTarget as? EntityHitResult)?.entity?.isGlowing ?: "Unknown"

        variables["crosshair_entity_age"] = (client.crosshairTarget as? EntityHitResult)?.entity?.age ?: "Unknown"

        variables["crosshair_entity_height"] = (client.crosshairTarget as? EntityHitResult)?.entity?.height ?: "Unknown"
        variables["crosshair_entity_width"] = (client.crosshairTarget as? EntityHitResult)?.entity?.width ?: "Unknown"

        variables["crosshair_entity_on_fire"] = (client.crosshairTarget as? EntityHitResult)?.entity?.isOnFire ?: "Unknown"
        variables["crosshair_entity_freezing"] = (client.crosshairTarget as? EntityHitResult)?.entity?.frozenTicks ?: "Unknown"

        variables["crosshair_entity_air"] = (client.crosshairTarget as? EntityHitResult)?.entity?.air ?: "Unknown"

        variables["crosshair_entity_fire_ticks"] = (client.crosshairTarget as? EntityHitResult)?.entity?.fireTicks ?: "Unknown"

        variables["crosshair_entity_riding"] = (client.crosshairTarget as? EntityHitResult)?.entity?.hasVehicle() ?: "Unknown"
        variables["crosshair_entity_passengers"] = (client.crosshairTarget as? EntityHitResult)?.entity?.passengerList?.size ?: "Unknown"

        // World
        variables["world_dimension"] = client.world?.dimension?.effects ?: "Unknown"
        variables["world_dimension_details"] = client.world?.dimension ?: "Unknown"

        variables["world_player_count"] = client.world?.players?.size ?: "Unknown"
        variables["world_moon_phase"] = client.world?.moonPhase ?: "Unknown"
        variables["world_moon_size"] = client.world?.moonSize ?: "Unknown"

        variables["world_time"] = client.world?.time ?: "Unknown"
        variables["world_cycle"] = ((client.world?.timeOfDay ?: 0) % 24000)
        variables["world_cycle_percent"] = ((client.world?.timeOfDay ?: 0) % 24000) / 24000.0
        variables["world_time_string"] = client.world?.timeOfDay?.let { "%02d:%02d".format(((it / 1000) + 6) % 24, (it / 50) * 3 % 60) } ?: "Unknown"
        variables["world_time_day"] = client.world?.timeOfDay?.div(24000) ?: "Unknown"
        variables["world_time_hour"] = (client.world?.timeOfDay?.div(1000)?.plus(6))?.rem(24) ?: "Unknown"
        variables["world_time_minute"] = client.world?.timeOfDay?.div(50)?.times(3)?.rem(60) ?: "Unknown"

        variables["world_rain"] = client.world?.isRaining ?: "Unknown"
        variables["world_thunder"] = client.world?.isThundering ?: "Unknown"

        variables["world_difficulty"] = client.world?.difficulty ?: "Unknown"
        variables["world_difficulty_id"] = client.world?.difficulty?.id ?: "Unknown"

        variables["world_spawn_x"] = client.world?.spawnPos?.x ?: "Unknown"
        variables["world_spawn_y"] = client.world?.spawnPos?.y ?: "Unknown"
        variables["world_spawn_z"] = client.world?.spawnPos?.z ?: "Unknown"

        // Client
        variables["client_username"] = client.session?.username ?: "Unknown"
        variables["client_session_id"] = client.session?.sessionId ?: "Unknown"
        variables["client_screen_title"] = client.currentScreen?.title ?: "Unknown"

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

        // Server ToDO: Fix not working on servers
        variables["server_name"] = client.server?.name ?: "Unknown"
        variables["server_ip"] = client.server?.serverIp ?: "Unknown"
        variables["server_port"] = client.server?.serverPort ?: "Unknown"
        variables["server_average_tick_time"] = client.server?.averageTickTime ?: "Unknown"
        variables["server_default_gamemode"] = client.server?.defaultGameMode ?: "Unknown"
        variables["server_online_mode"] = client.server?.isOnlineMode ?: "Unknown"
        variables["server_pvp_enabled"] = client.server?.isPvpEnabled ?: "Unknown"
        variables["server_singleplayer"] = client.server?.isSingleplayer ?: "Unknown"
        variables["ping"] = client.player?.networkHandler?.getPlayerListEntry(client.player?.uuid)?.latency ?: "Unknown"

        // Session Last
        variables["session_last_ms"] = SessionUtil.timeLastSession

        variables["session_since_last_ms"] = SessionUtil.timeCurrentSession - SessionUtil.timeLastSession
        variables["session_since_last_seconds"] = (SessionUtil.timeCurrentSession - SessionUtil.timeLastSession) / 1000
        variables["session_since_last_minutes"] = (SessionUtil.timeCurrentSession - SessionUtil.timeLastSession) / 60000
        variables["session_since_last_hours"] = (SessionUtil.timeCurrentSession - SessionUtil.timeLastSession) / 3600000
        variables["session_since_last_days"] = (SessionUtil.timeCurrentSession - SessionUtil.timeLastSession) / 86400000
        variables["session_since_last_months"] = (SessionUtil.timeCurrentSession - SessionUtil.timeLastSession) / 2629746000
        variables["session_since_last_years"] = (SessionUtil.timeCurrentSession - SessionUtil.timeLastSession) / 31557600000

        // Session Current
        variables["session_current_ms"] = SessionUtil.timeCurrentSession

        variables["session_since_current_ms"] = System.currentTimeMillis() - SessionUtil.timeCurrentSession
        variables["session_since_current_seconds"] = (System.currentTimeMillis() - SessionUtil.timeCurrentSession) / 1000
        variables["session_since_current_minutes"] = (System.currentTimeMillis() - SessionUtil.timeCurrentSession) / 60000
        variables["session_since_current_hours"] = (System.currentTimeMillis() - SessionUtil.timeCurrentSession) / 3600000
        variables["session_since_current_days"] = (System.currentTimeMillis() - SessionUtil.timeCurrentSession) / 86400000
        variables["session_since_current_months"] = (System.currentTimeMillis() - SessionUtil.timeCurrentSession) / 2629746000
        variables["session_since_current_years"] = (System.currentTimeMillis() - SessionUtil.timeCurrentSession) / 31557600000

        // System
        variables["system_os"] = System.getProperty("os.name") ?: "Unknown"
        variables["system_arch"] = System.getProperty("os.arch") ?: "Unknown"
        variables["system_java_version"] = System.getProperty("java.version") ?: "Unknown"
        variables["system_user"] = System.getProperty("user.name") ?: "Unknown"
        variables["system_user_home"] = System.getProperty("user.home") ?: "Unknown"

        // TimeZone
        variables["timezone_id"] = TimeZone.getDefault().id ?: "Unknown"
        variables["timezone_name"] = TimeZone.getDefault().displayName ?: "Unknown"

        // Runtime
        variables["runtime_memory_max"] = Runtime.getRuntime().maxMemory()
        variables["runtime_memory_free"] = Runtime.getRuntime().freeMemory()
        variables["runtime_memory_total"] = Runtime.getRuntime().totalMemory()
        variables["runtime_cpu_cores"] = Runtime.getRuntime().availableProcessors()
    }
}