package tik.prometheus.rest.constants

import ir.mirrajabi.searchdialog.core.Searchable

enum class SensorType(val value: String) {
    HUMIDITY("HUMIDITY"),
    TEMPERATURE("TEMPERATURE"),
    SOIL_MOISTURE("SOIL_MOISTURE"),
    WATER("WATER"),
    LIGHT("LIGHT"),
    NaN("NaN");

    companion object {
        fun getModels(): ArrayList<SensorTypeModel> {
            val types = SensorType.values()
            val models = types.map { SensorTypeModel(it) }.toCollection(ArrayList())
            return models
        }
        fun getNullableModels(): ArrayList<NullableSensorTypeModel> {
            val types = SensorType.values()
            val models = types.map { NullableSensorTypeModel(it) }.toCollection(ArrayList())
            models.add(NullableSensorTypeModel(null))
            return models
        }
    }
}

data class SensorTypeModel(val sensorType: SensorType) : Searchable {
    override fun getTitle(): String {
        return sensorType.value
    }
}
data class NullableSensorTypeModel(val sensorType: SensorType?) : Searchable {
    override fun getTitle(): String {
        return sensorType?.value ?: "null"
    }
}
