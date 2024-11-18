package com.task314.service;

import com.task314.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    private static final String URL = "http://94.198.50.185:7081/api/users";
    private final RestTemplate restTemplate;
    private String sessionId;

    public UserService() {
        this.restTemplate = new RestTemplate();
    }

    public List<User> getAllUsers() {
        ResponseEntity<User[]> response = restTemplate.getForEntity(URL, User[].class);
        if (response.getStatusCode() == HttpStatus.OK) {
            sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        }
        return Arrays.asList(response.getBody());
    }

    public String saveUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, sessionId);

        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(URL, request, String.class);
        return response.getBody();
    }

    public String updateUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, sessionId);

        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.PUT, request, String.class);
        return response.getBody();
    }


    public String deleteUser(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, sessionId);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, request, String.class);
        return response.getBody();
    }
}