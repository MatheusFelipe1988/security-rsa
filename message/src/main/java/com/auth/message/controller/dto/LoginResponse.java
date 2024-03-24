package com.auth.message.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
