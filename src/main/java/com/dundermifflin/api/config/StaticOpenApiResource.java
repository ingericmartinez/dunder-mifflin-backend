package com.dundermifflin.api.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openapi")
public class StaticOpenApiResource {

    @GetMapping(value = "/df.yaml", produces = {"application/yaml", "application/x-yaml", "text/yaml"})
    public ResponseEntity<Resource> getSpec() throws IOException {
        ClassPathResource res1 = new ClassPathResource("scripts/df.yaml");
        if (res1.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("application/yaml"))
                    .body(res1);
        }
        ClassPathResource res2 = new ClassPathResource("df.yaml");
        if (res2.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("application/yaml"))
                    .body(res2);
        }
        // As a last resort, try to stream from the file system path used in development
        java.nio.file.Path p = java.nio.file.Paths.get("scripts", "df.yaml");
        if (java.nio.file.Files.exists(p)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("application/yaml"))
                    .body(new InputStreamResource(java.nio.file.Files.newInputStream(p)));
        }
        throw new java.io.FileNotFoundException("df.yaml not found on classpath under scripts/ or root");
    }
}
