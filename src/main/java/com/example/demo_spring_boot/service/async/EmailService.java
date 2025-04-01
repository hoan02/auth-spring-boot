package com.example.demo_spring_boot.service.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  @Async
  public void sendEmail(String to, String subject, String content) {
    // Gửi email
    System.out.println("Đang gửi email đến " + to);
    try {
      Thread.sleep(5000); // Simulate sending email
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    System.out.println("Đã gửi email đến " + to);
  }
}
