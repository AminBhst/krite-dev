package io.github.aminbhst.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @GetMapping("/list")
    public ResponseEntity<String> listTasks(@AuthenticationPrincipal User user) {
        String username = user.getUsername();
        return ResponseEntity.ok("Hello, " + username + "! Here are your tasks...");
    }


}
