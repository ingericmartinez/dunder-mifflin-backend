package com.dundermifflin.api.web;

import com.dundermifflin.api.config.StaticOpenApiResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StaticOpenApiResource.class)
@Import(com.dundermifflin.api.config.SecurityConfig.class)
class StaticOpenApiResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void staticYaml_isServedFromClassPath() throws Exception {
        mockMvc.perform(get("/openapi/df.yaml"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", org.hamcrest.Matchers.containsString("application/yaml")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("openapi:")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Dunder Mifflin API")));
    }
}
