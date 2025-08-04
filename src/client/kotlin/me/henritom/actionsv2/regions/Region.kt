package me.henritom.actionsv2.regions

data class Region(
    @Transient
    var id: String,
    val server: String,
    val world: String,
    val x1: Int,
    val y1: Int,
    val z1: Int,
    val x2: Int,
    val y2: Int,
    val z2: Int
) {
    init {
        RegionManager.registerRegion(this)
    }
}
