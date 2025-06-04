package com.distributedproject.userservice.controller;

import com.distributedproject.userservice.model.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class SendPayrollUserController {
    @RestController
    @RequestMapping("/user")
    public class UserController {

        @GetMapping("/{employeeId}/details")
        public UserDTO getUserDetails(@PathVariable String employeeId) {
            return new UserDTO(employeeId, "Software Engineer");
        }
    }

}
