package com.example.demo_spring_boot.exception;

public class InternalAuthException extends RuntimeException {
  public InternalAuthException(String message, Throwable cause) {
    super(message, cause);
  }
}