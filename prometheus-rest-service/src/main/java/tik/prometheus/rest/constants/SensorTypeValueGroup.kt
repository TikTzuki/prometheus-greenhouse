package tik.prometheus.rest.constants

import tik.prometheus.rest.dtos.RangeDTO

enum class SensorValueGroup {
    HIGH,
    MID,
    LOW;
    companion object{
        const val NEGATIVE_INFINITY = -99999F;
        const val POSITIVE_INFINITY = 99999F;
    }
}
