package tik.prometheus.rest.constants

enum class SensorType(val value: String) {
    HUMIDITY("HUMIDITY"),
    TEMPERATURE("TEMPERATURE"),
    SOIL_MOISTURE("SOIL_MOISTURE"),
    WATER("WATER"),
    LIGHT("LIGHT"),
    NaN("NaN")
}