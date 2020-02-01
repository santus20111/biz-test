package com.example.bizhome.web;

import com.example.bizhome.service.ConvertService;
import com.example.bizhome.web.request.ConvertRequest;
import com.example.bizhome.web.response.ConvertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/convert")
@RequiredArgsConstructor
public class ConvertController {

    private final ConvertService convertService;

    @PostMapping
    public Mono<ConvertResponse> convert(
            @Validated @RequestBody ConvertRequest request) {
        return convertService.convert(request);
    }
}
