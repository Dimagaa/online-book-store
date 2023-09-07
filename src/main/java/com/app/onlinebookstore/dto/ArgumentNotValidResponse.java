package com.app.onlinebookstore.dto;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public record ArgumentNotValidResponse(LocalDateTime timestamp, HttpStatus status, String[] errors){

}
