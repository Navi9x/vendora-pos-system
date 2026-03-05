package com.vendora.vendorapos.dto;


public record LoginResponse(UserDTO user, String jwtToken) {
}
