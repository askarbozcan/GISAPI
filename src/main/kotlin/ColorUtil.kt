object ColorUtil {
    fun RGForPolyScore(score: Double): String {
        val red = (255 * (1 - score)).toInt()
        val green = (255 * score).toInt()
        val blue = 0
        return String.format("#%02x%02x%02x", red, green, blue)
    }


}