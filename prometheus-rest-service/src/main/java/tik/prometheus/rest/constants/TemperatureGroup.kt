package tik.prometheus.rest.constants

enum class TemperatureGroup(val start: Double, val end: Double) {
    HOT(34.0, Double.MAX_VALUE),
    MID(20.0, 33.0),
    COOL(Double.MIN_VALUE, 19.0);

    companion object {
        fun getGroup(value: Double): TemperatureGroup {
            TemperatureGroup.values().forEach {
                if (it.start <= value && value <= it.end) {
                    return it;
                }
            }
            throw IllegalCallerException("value invalid")
        }
    }
}
