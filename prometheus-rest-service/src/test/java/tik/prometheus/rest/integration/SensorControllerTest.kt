package tik.prometheus.rest.integration

import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import tik.prometheus.rest.Apis
import tik.prometheus.rest.Application
import tik.prometheus.rest.services.SensorService

@RunWith(SpringRunner::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = [Application::class],
)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = ["classpath:application-integrationtest.yml"]
)
class SensorControllerTest {
    private val log: Logger = LoggerFactory.getLogger(SensorControllerTest::class.java)

    @Autowired
    lateinit var mvc: MockMvc;

    @MockBean
    lateinit var service: SensorService;

    @Test
    fun getSensors() {

//        BDDMockito.given(sensors).willReturn(sensors)
        val result = mvc.perform(
            MockMvcRequestBuilders
                .get(Apis.sensorGetList)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
                .queryParam("page", "0")
                .queryParam("size", "10")
        )
//            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andReturn()
        log.info("--------------------------")
        log.info(result.response.getContentAsString())
        log.info(result.response.contentType)
//            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    }
}