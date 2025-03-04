package tik.prometheus.rest.constants

import ir.mirrajabi.searchdialog.core.Searchable

enum class ActuatorType(val value: String) {
    IRRIGATOR("IRRIGATOR"),
    NaN("NaN");

    companion object {
        fun getModels(): ArrayList<ActuatorTypeModel> {
            val types = ActuatorType.values()
            return types.map { ActuatorTypeModel(it) }.toCollection(ArrayList())
        }
    }
}

data class ActuatorTypeModel(val actuatorType: ActuatorType) : Searchable {
    override fun getTitle(): String {
        return actuatorType.value
    }

}