package com.example.bizhome.service;

import com.example.bizhome.web.request.ConvertRequest;
import com.example.bizhome.web.response.ConvertResponse;
import reactor.core.publisher.Mono;

public interface ConvertService {
    Mono<ConvertResponse> convert(ConvertRequest request);
}
