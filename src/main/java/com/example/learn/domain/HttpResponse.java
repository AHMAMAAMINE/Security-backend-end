package com.example.learn.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse {
    private int HttpStatsCode;//200->succes,200 range succes,400 range user error,500 range server error
    private HttpStatus httpStatus;
    private String reason;
    private String message;
}
