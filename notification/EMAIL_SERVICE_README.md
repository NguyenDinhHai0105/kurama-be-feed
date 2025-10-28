# Email Service Documentation

## Overview
The EmailService provides comprehensive email sending functionality with support for:
- Simple text emails
- HTML emails
- Multiple recipients
- CC and BCC
- File attachments
- Custom from address

## Configuration

### 1. Mail Server Setup
Update `application.yml` with your SMTP server details:

```yaml
spring:
  mail:
    host: smtp.gmail.com  # Your SMTP server
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

### 2. Gmail Setup (if using Gmail)
- Enable 2-factor authentication on your Google account
- Generate an App Password: https://myaccount.google.com/apppasswords
- Use the generated app password in the configuration

### 3. Other Mail Providers
- **Outlook/Office365**: `smtp.office365.com`, port 587
- **Yahoo**: `smtp.mail.yahoo.com`, port 587
- **SendGrid**: `smtp.sendgrid.net`, port 587
- **Amazon SES**: `email-smtp.[region].amazonaws.com`, port 587

## Usage Examples

### 1. Simple Email (Single Recipient)
```java
@Autowired
private EmailService emailService;

public void sendWelcomeEmail() {
    emailService.send(
        "user@example.com",
        "Welcome!",
        "<h1>Welcome to our service!</h1><p>Thank you for signing up.</p>"
    );
}
```

### 2. Email to Multiple Recipients
```java
public void sendBulkEmail() {
    String[] recipients = {
        "user1@example.com",
        "user2@example.com",
        "user3@example.com"
    };
    
    emailService.send(
        recipients,
        "Newsletter",
        "<h1>Monthly Newsletter</h1><p>Check out our latest updates!</p>"
    );
}
```

### 3. Email with CC and BCC
```java
public void sendEmailWithCopies() {
    String[] to = {"primary@example.com"};
    String[] cc = {"manager@example.com", "team@example.com"};
    String[] bcc = {"archive@example.com"};
    
    emailService.send(
        to,
        cc,
        bcc,
        "Project Update",
        "<h1>Project Status</h1><p>The project is on track.</p>",
        null,  // no attachments
        null   // use default from address
    );
}
```

### 4. Email with Attachments
```java
public void sendEmailWithFiles() {
    File[] attachments = {
        new File("/path/to/document.pdf"),
        new File("/path/to/image.jpg")
    };
    
    emailService.send(
        new String[]{"recipient@example.com"},
        null,  // no CC
        null,  // no BCC
        "Documents Attached",
        "<h1>Please find attached documents</h1>",
        attachments,
        null   // use default from address
    );
}
```

### 5. Complete Example with All Options
```java
public void sendComprehensiveEmail() {
    String[] to = {"recipient@example.com"};
    String[] cc = {"manager@example.com"};
    String[] bcc = {"archive@example.com"};
    File[] attachments = {new File("/path/to/report.pdf")};
    String customFrom = "noreply@company.com";
    
    emailService.send(
        to,
        cc,
        bcc,
        "Monthly Report",
        "<h1>Monthly Report</h1><p>Please review the attached report.</p>",
        attachments,
        customFrom
    );
}
```

### 6. Using Helper Methods

#### Simple Text Email
```java
emailService.sendSimpleEmail(
    "user@example.com",
    "Simple Message",
    "This is a plain text email."
);
```

#### HTML Email
```java
emailService.sendHtmlEmail(
    "user@example.com",
    "HTML Message",
    "<h1>Hello!</h1><p>This is an <strong>HTML</strong> email.</p>"
);
```

#### Email with Attachments Only
```java
File[] files = {new File("/path/to/file.pdf")};
emailService.sendEmailWithAttachments(
    "user@example.com",
    "Files Attached",
    "<p>Please find the attached files.</p>",
    files
);
```

## Email Templates
When you create email templates, you can integrate them with the EmailService like this:

```java
@Service
public class NotificationService {
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private EmailTemplateService templateService;  // You'll create this later
    
    public void sendWelcomeEmail(String userEmail, String userName) {
        // Generate HTML from template
        String htmlBody = templateService.generateWelcomeEmail(userName);
        
        emailService.send(
            userEmail,
            "Welcome to Our Platform!",
            htmlBody
        );
    }
    
    public void sendPasswordResetEmail(String userEmail, String resetToken) {
        String htmlBody = templateService.generatePasswordResetEmail(resetToken);
        
        emailService.send(
            userEmail,
            "Password Reset Request",
            htmlBody
        );
    }
}
```

## Error Handling
All methods throw `RuntimeException` on failure. You can catch and handle these appropriately:

```java
try {
    emailService.send(
        "user@example.com",
        "Test Email",
        "<p>Test content</p>"
    );
} catch (RuntimeException e) {
    log.error("Failed to send email", e);
    // Handle error (retry, notify admin, etc.)
}
```

## Best Practices

1. **Use HTML Templates**: Create reusable HTML templates for consistent branding
2. **Validate Email Addresses**: Validate email addresses before sending
3. **Handle Failures**: Implement retry logic for transient failures
4. **Async Processing**: Consider using `@Async` for non-blocking email sending
5. **Rate Limiting**: Be aware of your email provider's rate limits
6. **Monitor Sending**: Log email sends for audit and debugging
7. **Test Thoroughly**: Test with different email providers before production

## Common Issues

### Gmail "Less Secure App" Error
- Solution: Use App Passwords instead of regular password

### Connection Timeout
- Check firewall settings
- Verify SMTP server and port
- Check network connectivity

### Authentication Failed
- Verify username and password
- Check if account requires app-specific password
- Ensure SMTP auth is enabled

### Emails Going to Spam
- Set up SPF, DKIM, and DMARC records
- Use a verified from address
- Avoid spam trigger words
- Include unsubscribe link for bulk emails

## Next Steps
1. Configure your SMTP server in `application.yml`
2. Test with a simple email send
3. Create email templates (HTML/Thymeleaf/Freemarker)
4. Integrate with your business logic
5. Consider implementing email queue for high volume

