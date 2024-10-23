package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.AuthRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.AuthResponseDTO;

public interface AuthenticationService {
    AuthResponseDTO authenticate(AuthRequestDTO request);
}
