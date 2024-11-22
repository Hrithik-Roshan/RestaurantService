package com.bits.scalable.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@RestController
@RequestMapping("/api/restaurants")
public class CustomOrderController {

    private static final String ORDER_SERVICE_BASE_URL = "http://localhost:8081/api/orders";

    HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .sslContext(createInsecureSslContext())
            .build();
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static SSLContext createInsecureSslContext() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                (TrustManager) new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    @PutMapping("/update-order-status/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestParam String targetStatus) {
        try {
            // Step 1: Fetch the order details
            String getOrderUrl = ORDER_SERVICE_BASE_URL + "/get-order/" + orderId;

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create(getOrderUrl))
                    .GET()
                    .build();

            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

            if (getResponse.statusCode() != 200) {
                return ResponseEntity.status(getResponse.statusCode())
                        .body("Failed to fetch order details.");
            }

            // Parse the response JSON into a Map
            Map<String, Object> orderDetails = objectMapper.readValue(getResponse.body(), Map.class);

            if (orderDetails == null) {
                return ResponseEntity.badRequest().body("No order details found.");
            }

            // Step 2: Update the status field
            orderDetails.put("status", targetStatus);

            // Step 3: Send updated order using PUT
            String putOrderUrl = ORDER_SERVICE_BASE_URL + "/update/" + orderId;

            HttpRequest putRequest = HttpRequest.newBuilder()
                    .uri(URI.create(putOrderUrl))
                    .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(orderDetails)))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> putResponse = httpClient.send(putRequest, HttpResponse.BodyHandlers.ofString());

            if (putResponse.statusCode() == 200 || putResponse.statusCode() == 204) {
                return ResponseEntity.ok("Order status updated successfully.");
            } else {
                return ResponseEntity.status(putResponse.statusCode())
                        .body("Failed to update order status.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
}
