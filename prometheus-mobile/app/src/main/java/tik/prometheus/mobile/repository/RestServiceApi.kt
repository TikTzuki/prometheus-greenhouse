package tik.prometheus.mobile.repository

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*
import tik.prometheus.mobile.models.*
import tik.prometheus.rest.constants.GreenhouseType
import tik.prometheus.rest.constants.SensorType
import tik.prometheus.rest.models.Farm
import tik.prometheus.rest.models.Greenhouse
import tik.prometheus.rest.models.GreenhouseReq
import java.time.LocalDateTime

interface RestServiceApi {


    @GET("/farms")
    suspend fun aGetFarms(@QueryMap pageable: Map<String, String>): Response<Page<Farm>>

    @GET("/farms")
    fun getFarms(@QueryMap pageable: Map<String, String>): Single<Page<Farm>>

    @GET("/greenhouses")
    suspend fun getGreenhouses( @QueryMap pageable: Map<String, String>, @Query("label") label: String? = null, @Query("type") greenhouseType: GreenhouseType? = null): Response<Page<Greenhouse>>

    @GET("/greenhouses/{id}")
    suspend fun getGreenhouse(@Path("id") id: Long): Response<Greenhouse>

    @PUT("/greenhouses/{id}")
    suspend fun putGreenhouse(@Path("id") id: Long, @Body greenhouse: GreenhouseReq): Response<Greenhouse>;

    @GET("/sensors")
    suspend fun getSensors(@QueryMap pageable: Map<String, String>, @Query("greenhouseId") greenhouseId: Long? = null, @Query("type") type: SensorType? = null): Response<Page<Sensor>>

    @GET("/sensors/{id}")
    suspend fun getSensor(@Path("id") id: Long): Response<Sensor>

    @PUT("/sensors/{id}")
    suspend fun putSensor(@Path("id") id: Long, @Body sensor: SensorReq): Response<Sensor>

    @GET("/actuators")
    suspend fun getActuators(@QueryMap pageable: Map<String, String>, @Query("greenhouseId") greenhouseId: Long? = null): Response<Page<Actuator>>

    @GET("/actuators/{id}")
    suspend fun getActuator(@Path("id") id: Long): Response<Actuator>

    @PUT("/actuators/{id}")
    suspend fun putActuator(@Path("id") id: Long, @Body actuator: ActuatorReq): Response<Actuator>

    @PATCH("/actuators/{id}")
    suspend fun patchActuator(@Path("id") id: Long, @Body nextState: ActuatorState): Response<Void>

    @GET("/sensors/{id}/records")
    suspend fun getSensorRecords(@Path("id") id: Long, @Query("from") from: LocalDateTime, @Query("to") to: LocalDateTime): Response<List<Float>>

    @GET("/actuators/{id}/tasks")
    suspend fun getActuatorTask(@Path("id") id: Long): Response<ActuatorTask>

    @POST("/actuators/{id}/tasks")
    suspend fun postActuatorTask(@Path("id") id: Long, @Body actuatorTask: ActuatorTask): Response<Void>

    @DELETE("/actuators/{id}/tasks")
    suspend fun deleteActuatorTask(@Path("id") id: Long): Response<Void>
}