package com.dundermifflin.api.web;

import com.dundermifflin.api.dto.InventoryItemDto;
import com.dundermifflin.api.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InventoryController.class)
class InventoryControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getInventory_accessibleToUser() throws Exception {
        Mockito.when(inventoryService.list(Mockito.any(), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new InventoryItemDto())));
        mockMvc.perform(get("/api/v1/dundermifflin/inventory"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void patchInventory_forbiddenToUser() throws Exception {
        mockMvc.perform(patch("/api/v1/dundermifflin/inventory/p1?branchId=b1").with(csrf())
                        .contentType("application/json")
                        .content("{\n  \"quantityInStock\": 10\n}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void patchInventory_allowedToAdmin() throws Exception {
        InventoryItemDto dto = new InventoryItemDto();
        dto.setProductId("p1");
        dto.setBranchId("b1");
        dto.setQuantityInStock(10);
        Mockito.when(inventoryService.updateQuantity(Mockito.eq("p1"), Mockito.eq("b1"), Mockito.eq(10)))
                .thenReturn(dto);
        mockMvc.perform(patch("/api/v1/dundermifflin/inventory/p1?branchId=b1").with(csrf())
                        .contentType("application/json")
                        .content("{\n  \"quantityInStock\": 10\n}"))
                .andExpect(status().isOk());
    }
}
