package com.example.demo_spring_boot.exception;

public class UnauthorizedTokenException extends RuntimeException {
  public UnauthorizedTokenException(String message) {
    super(message);
  }
}
