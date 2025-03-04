package tik.prometheus.rest;

import avro.shaded.com.google.common.base.CharMatcher;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;

class PrometheusRestServiceApplicationTests {


    @Test
    void runParallel() throws InterruptedException {
        LosCaller t1 = new LosCaller();
        LosCaller t2 = new LosCaller();

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
    @Test
    void testBool(){
    }

    static class LosCaller extends Thread {
        @Override
        public void run() {
            RestTemplate client = new RestTemplate();
            ResponseEntity<HashMap> res = client.getForEntity("http://127.0.0.1:8001/api/v2/test-service?service=gateway-service&path=workflow/", HashMap.class);
            String v = res.getHeaders().get("x-process-time").toString();
            System.out.println(v);
        }
    }
}
