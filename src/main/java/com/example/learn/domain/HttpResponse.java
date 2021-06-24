package com.example.learn.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

public class HttpResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "MM-dd-yyyy hh:mm:ss",timezone = "GMT+1")
    private Date timeStamp;
    private int httpStatsCode;//200->succes,200 range succes,400 range user error,500 range server error
    private HttpStatus httpStatus;
    private String reason;
    private String message;


    public HttpResponse(int httpStatsCode, HttpStatus httpStatus, String reason, String message) {
        this.timeStamp=new Date();
        this.httpStatsCode = httpStatsCode;
        this.httpStatus = httpStatus;
        this.reason = reason;
        this.message = message;
    }
}
