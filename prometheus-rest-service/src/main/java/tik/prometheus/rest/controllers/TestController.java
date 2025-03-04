//package tik.prometheus.rest.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//@RestController
//@EnableEurekaClient
//public class TestController {
//    @Autowired
//    private LosClient losClient;
//
//    @Autowired
//    private Gateway gatewayClient;
//
//    @Autowired
//    private DiscoveryClient discoveryClient;
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    @GetMapping("/test")
//    public Map<String, Object> getTest() {
//
////        discoveryClient.getInstances("los-be").forEach(s -> {
////            System.out.println(ToStringBuilder.reflectionToString(s));
////        });
////        return null;
//
////        ResponseEntity<Map<String, Object>> rs = restTemplate.exchange("http://gateway-service/hello-world", HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {
////        });
//        return losClient.isDeploy();
//    }
//
//    @GetMapping("/hello")
//    public Map<String, Object> hello() {
//        return Map.of("msg", "hello");
//    }
//}
//
//@Component
//class Client extends RestTemplate {
//}
//
//@FeignClient("los-be")
//interface LosClient {
//    @RequestMapping(method = RequestMethod.GET, value = "/api/v2/hello-world")
//    Map<String, Object> isDeploy();
//}
//
//@FeignClient("gateway-service")
//interface Gateway {
//    @RequestMapping("/hello-world")
//    Map<String, Object> hello();
//}
