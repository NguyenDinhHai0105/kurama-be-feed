package com.kurama.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for email sending requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {

    /**
     * Primary recipients
     */
    private String[] to;

    /**
     * CC recipients (optional)
     */
    private String[] cc;

    /**
     * BCC recipients (optional)
     */
    private String[] bcc;

    /**
     * Email subject
     */
    private String subject;

    /**
     * Email body (HTML content)
     */
    private String htmlBody;

    /**
     * Custom from address (optional)
     */
    private String from;

    /**
     * Attachment file paths (optional)
     */
    private String[] attachmentPaths;
}

