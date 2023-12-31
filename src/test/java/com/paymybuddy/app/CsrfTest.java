package com.paymybuddy.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class CsrfTest {

    @Autowired
    private MockMvc mockMvc;

    
    @Test
    @WithMockUser
    public void testCsrfProtectionWithoutToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/create"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
