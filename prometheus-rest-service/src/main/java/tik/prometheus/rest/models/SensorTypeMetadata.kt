package tik.prometheus.rest.models

import com.google.gson.Gson
import org.hibernate.engine.jdbc.ClobProxy
import tik.prometheus.rest.constants.SensorType
import java.sql.Clob
import javax.persistence.*

@Entity
@Table(name = "SENSOR_TYPE_METADATA")
class SensorTypeMetadata(
    @Id
    @Enumerated(EnumType.STRING)
    var type: SensorType? = null,
    @Lob
    var content: Clob? = null
) {
    constructor(type: SensorType, content: Any) : this(type, null) {
        setContent(content)
    }

    fun <T> getContent(type: Class<T>): T? {
        if (content == null) {
            return null;
        }
        return Gson().fromJson(content!!.characterStream.readText(), type);
    }

    fun setContent(content: Any) {
        this.content = ClobProxy.generateProxy(Gson().toJson(content))
    }
}