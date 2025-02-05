package de.henritom.actions.region

import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.util.math.Vec3d

class Region(val name: String, var pos1: Vec3d, val pos2: Vec3d) {

    val playerList: MutableList<ClientPlayerEntity> = mutableListOf()

    var maxX = pos1.x.coerceAtLeast(pos2.x)
    var minX = pos1.x.coerceAtMost(pos2.x)

    var maxY = pos1.y.coerceAtLeast(pos2.y)
    var minY = pos1.y.coerceAtMost(pos2.y)

    var maxZ = pos1.z.coerceAtLeast(pos2.z)
    var minZ = pos1.z.coerceAtMost(pos2.z)

}