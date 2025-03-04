package tik.prometheus.rest.constants

import ir.mirrajabi.searchdialog.core.Searchable

enum class GreenhouseType(var value: String) {
    NORMAL("NORMAL"),
    NaN("NaN");

    companion object {
        fun getModels(): ArrayList<GreenhouseTypeModel> {
            val types = GreenhouseType.values()
            return types.map { GreenhouseTypeModel(it) }.toCollection(ArrayList())
        }

        fun getNullableModels(): ArrayList<NullableGreenhouseTypeModel> {
            val types = GreenhouseType.values()
            val models = types.map { NullableGreenhouseTypeModel(it) }.toCollection(ArrayList())
            models.add(NullableGreenhouseTypeModel(null))
            return models
        }
    }
}

data class GreenhouseTypeModel(val greenhouseType: GreenhouseType) : Searchable {
    override fun getTitle(): String {
        return greenhouseType.value
    }
}

data class NullableGreenhouseTypeModel(val type: GreenhouseType?) : Searchable {
    override fun getTitle(): String {
        return type?.value ?: "null"
    }
}