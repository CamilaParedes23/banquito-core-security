package com.banquito.platform.identity.application.service;

import com.banquito.platform.identity.domain.repository.ParametroSeguridadRepository;
import org.springframework.stereotype.Service;

@Service
public class ParametroSeguridadService {
    private final ParametroSeguridadRepository parametroRepository;

    public ParametroSeguridadService(ParametroSeguridadRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    public int getInteger(String code, int defaultValue) {
        return parametroRepository.findById(code)
                .map(p -> p.valorEntero(defaultValue))
                .orElse(defaultValue);
    }
}
