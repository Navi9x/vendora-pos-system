package com.vendora.vendorapos.dto;

import java.util.List;

public record UserDTO(Long id,
                      String email,
                      String firstName,
                      String lastName,
                      String phone,
                      String role,
                      Boolean isActive,
                      List<BusinessDTO> businessDTO,
                      Long locationId) {
}
