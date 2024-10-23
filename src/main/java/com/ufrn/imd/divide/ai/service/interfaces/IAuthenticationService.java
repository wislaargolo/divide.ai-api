package com.ufrn.imd.divide.ai.service.interfaces;

import com.ufrn.imd.divide.ai.dto.request.AuthRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.AuthResponseDTO;

public interface IAuthenticationService {
    AuthResponseDTO authenticate(AuthRequestDTO request);
}
