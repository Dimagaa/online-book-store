package com.app.onlinebookstore.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public record ErrorResponseDto(LocalDateTime timestamp, HttpStatus status, String[] errors){
}
