package tik.prometheus.rest.models

import com.google.gson.Gson
import java.sql.Clob
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class DecisionTree(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val actuatorId: Long? = null,
    @Lob
    val tree: Clob? = null,
    val createdAt: LocalDateTime? = null
) {

    fun <T> getContent(type: Class<T>): T? {
        if (tree == null) {
            return null;
        }
        return Gson().fromJson(tree.characterStream.readText(), type);
    }
}
