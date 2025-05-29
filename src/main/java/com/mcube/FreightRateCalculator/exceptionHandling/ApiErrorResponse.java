package com.mcube.FreightRateCalculator.exceptionHandling;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiErrorResponse {
    // Сообщение об ошибке
    private String info;
    // Код HTTP-статуса
    private int statusCode;
    // Название статуса
    private String error;
    // URI запроса
    private String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

}
