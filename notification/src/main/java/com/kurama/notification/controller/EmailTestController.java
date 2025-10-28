package com.kurama.notification.controller;

import com.kurama.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test controller for email sending functionality
 * This is for testing purposes - remove or secure in production
 */
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Slf4j
public class EmailTestController {

    private final EmailService emailService;

    /**
     * Send a test email
     * POST /api/email/test
     * {
     *   "to": "recipient@example.com",
     *   "subject": "Test Email",
     *   "body": "<h1>Test</h1><p>This is a test email</p>"
     * }
     */
    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> sendTestEmail(
            @RequestBody Map<String, String> request) {
        try {
            String to = request.get("to");
            String subject = request.get("subject");
            String body = request.get("body");

            log.info("Sending test email to: {}", to);
            emailService.send(to, subject, body);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Email sent successfully to " + to);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to send test email", e);
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send email: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Send email to multiple recipients
     * POST /api/email/bulk
     * {
     *   "to": ["user1@example.com", "user2@example.com"],
     *   "subject": "Bulk Email",
     *   "body": "<h1>Newsletter</h1>"
     * }
     */
    @PostMapping("/bulk")
    public ResponseEntity<Map<String, String>> sendBulkEmail(
            @RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            String[] to = ((java.util.List<String>) request.get("to")).toArray(new String[0]);
            String subject = (String) request.get("subject");
            String body = (String) request.get("body");

            log.info("Sending bulk email to {} recipients", to.length);
            emailService.send(to, subject, body);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Email sent successfully to " + to.length + " recipients");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to send bulk email", e);
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send email: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Send welcome email example
     * POST /api/email/welcome
     * {
     *   "to": "newuser@example.com",
     *   "name": "John Doe"
     * }
     */
    @PostMapping("/welcome")
    public ResponseEntity<Map<String, String>> sendWelcomeEmail(
            @RequestBody Map<String, String> request) {
        try {
            String to = request.get("to");
            String name = request.getOrDefault("name", "User");

            // Create a simple welcome email HTML
            String htmlBody = String.format("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>
                            body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                            .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                            .header { background: #4CAF50; color: white; padding: 20px; text-align: center; }
                            .content { padding: 20px; background: #f9f9f9; }
                            .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>Welcome to Our Platform!</h1>
                            </div>
                            <div class="content">
                                <h2>Hello %s,</h2>
                                <p>Thank you for joining our platform. We're excited to have you on board!</p>
                                <p>Here's what you can do next:</p>
                                <ul>
                                    <li>Complete your profile</li>
                                    <li>Explore our features</li>
                                    <li>Connect with other users</li>
                                </ul>
                                <p>If you have any questions, feel free to reach out to our support team.</p>
                            </div>
                            <div class="footer">
                                <p>&copy; 2025 Kurama Platform. All rights reserved.</p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """, name);

            log.info("Sending welcome email to: {}", to);
            emailService.send(to, "Welcome to Kurama Platform!", htmlBody);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Welcome email sent successfully to " + to);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to send welcome email", e);
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send email: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}

