package com.distributedproject.userservice.controller;

import com.distributedproject.userservice.controller.user.CreateUserController;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.user.CreateUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreateUserController.class)
class CreateUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CreateUserService createUserService;

    @InjectMocks
    private CreateUserController createUserController;  // Inject mocks into the controller

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_validUser_returnsSavedUser() throws Exception {
        // Arrange
        User user = new User();
        user.setUserName("John Doe");
        user.setRoleId(1L);
        user.setDepartmentId(2L);

        when(createUserService.createUser(any(User.class))).thenReturn(user);

        // Act
        ResultActions result = mockMvc.perform(post("/create/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("John Doe"))
                .andExpect(jsonPath("$.roleId").value(1))
                .andExpect(jsonPath("$.departmentId").value(2));
    }

    @Test
    void createUser_invalidInput_returnsBadRequest() throws Exception {
        // Arrange: missing required fields (e.g., no userName)
        User user = new User();  // No fields set

        // Act & Assert
        mockMvc.perform(post("/create/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
}
