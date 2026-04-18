package com.dundermifflin.api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dundermifflin")
public class ApiRootController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> root() {
        return ResponseEntity.ok(Map.of(
                "name", "Dunder Mifflin API",
                "version", "v1",
                "timestamp", OffsetDateTime.now().toString(),
                "authentication", Map.of(
                        "type", "HTTP Basic",
                        "users", new String[]{"user/user123 (ROLE_USER)", "admin/admin123 (ROLE_ADMIN)"}
                ),
                "links", Map.of(
                        "catalogs", "/api/v1/dundermifflin/catalogs",
                        "inventory", "/api/v1/dundermifflin/inventory",
                        "catalogos", "/api/v1/dundermifflin/catalogos",
                        "inventario", "/api/v1/dundermifflin/inventario",
                        "swaggerUi", "/swagger-ui/index.html",
                        "openapiSpec", "/openapi/df.yaml"
                )
        ));
    }
}
