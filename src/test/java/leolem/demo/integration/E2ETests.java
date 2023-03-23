package leolem.demo.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import leolem.demo.users.dto.SignUpRequest;
import lombok.val;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class E2ETests {

    @Autowired
    private MockMvc mockMVC;

    @Autowired
    private ObjectMapper objectMapper;

    // @Test
    // void
    // whenSigningUp_thenUserIsCreatedInDatabaseAndResponseIs200AndCannotSignUpAgain()
    // throws Exception {
    // val request = new SignUpRequest("John Wayne", "jwayne@xyz.com", "1234");

    // mockMVC.perform(
    // post("/users")
    // .contentType("application/json")
    // .content(objectMapper.writeValueAsString(request)))
    // .andExpect(status().isOk());

    // mockMVC.perform(
    // post("/users")
    // .contentType("application/json")
    // .content(objectMapper.writeValueAsString(request)))
    // .andExpect(status().isConflict());
    // }

}
