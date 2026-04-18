package com.dundermifflin.api.web;

import com.dundermifflin.api.domain.Catalog;
import com.dundermifflin.api.dto.PaperProductDto;
import com.dundermifflin.api.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CatalogController.class)
class CatalogControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatalogService catalogService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCatalogs_accessibleToUserAndAdmin() throws Exception {
        Mockito.when(catalogService.list(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new Catalog())));

        mockMvc.perform(get("/api/v1/dundermifflin/catalogs"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void addProduct_forbiddenForUser() throws Exception {
        mockMvc.perform(post("/api/v1/dundermifflin/catalogs/c1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n  \"name\": \"Test\",\n  \"pricePerBox\": 10.0\n}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addProduct_allowedForAdmin() throws Exception {
        PaperProductDto dto = new PaperProductDto();
        dto.setId("pX");
        dto.setName("Test");
        dto.setPricePerBox(new java.math.BigDecimal("10.00"));
        Mockito.when(catalogService.addProduct(Mockito.eq("c1"), Mockito.any(PaperProductDto.class)))
                .thenReturn(dto);

        mockMvc.perform(post("/api/v1/dundermifflin/catalogs/c1/products").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n  \"name\": \"Test\",\n  \"pricePerBox\": 10.0\n}"))
                .andExpect(status().isCreated());
    }
}
