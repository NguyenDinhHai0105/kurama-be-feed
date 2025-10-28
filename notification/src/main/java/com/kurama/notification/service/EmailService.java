package com.kurama.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Send a simple text email
     *
     * @param to      recipient email address
     * @param subject email subject
     * @param text    email body (plain text)
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            log.info("Sending simple email to: {}", to);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Simple email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send simple email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Send multiple recipients simple text email
     *
     * @param to      array of recipient email addresses
     * @param subject email subject
     * @param text    email body (plain text)
     */
    public void sendSimpleEmail(String[] to, String subject, String text) {
        try {
            log.info("Sending simple email to {} recipients", to.length);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Simple email sent successfully to {} recipients", to.length);
        } catch (Exception e) {
            log.error("Failed to send simple email to multiple recipients", e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Send an HTML email
     *
     * @param to       recipient email address
     * @param subject  email subject
     * @param htmlBody email body (HTML content)
     */
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            log.info("Sending HTML email to: {}", to);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indicates HTML

            mailSender.send(message);
            log.info("HTML email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to: {}", to, e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    /**
     * Send an HTML email to multiple recipients
     *
     * @param to       array of recipient email addresses
     * @param subject  email subject
     * @param htmlBody email body (HTML content)
     */
    public void sendHtmlEmail(String[] to, String subject, String htmlBody) {
        try {
            log.info("Sending HTML email to {} recipients", to.length);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indicates HTML

            mailSender.send(message);
            log.info("HTML email sent successfully to {} recipients", to.length);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to multiple recipients", e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    /**
     * Send an email with attachments
     *
     * @param to          recipient email address
     * @param subject     email subject
     * @param htmlBody    email body (HTML content)
     * @param attachments array of files to attach
     */
    public void sendEmailWithAttachments(String to, String subject, String htmlBody, File[] attachments) {
        try {
            log.info("Sending email with {} attachments to: {}", attachments.length, to);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            // Add attachments
            for (File attachment : attachments) {
                helper.addAttachment(attachment.getName(), attachment);
                log.debug("Attached file: {}", attachment.getName());
            }

            mailSender.send(message);
            log.info("Email with attachments sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email with attachments to: {}", to, e);
            throw new RuntimeException("Failed to send email with attachments", e);
        }
    }

    /**
     * Send an email with CC and BCC recipients
     *
     * @param to       recipient email address
     * @param cc       CC email addresses (can be null)
     * @param bcc      BCC email addresses (can be null)
     * @param subject  email subject
     * @param htmlBody email body (HTML content)
     */
    public void sendEmailWithCcBcc(String to, String[] cc, String[] bcc, String subject, String htmlBody) {
        try {
            log.info("Sending email to: {} with CC and BCC", to);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            if (cc != null && cc.length > 0) {
                helper.setCc(cc);
                log.debug("Added {} CC recipients", cc.length);
            }
            if (bcc != null && bcc.length > 0) {
                helper.setBcc(bcc);
                log.debug("Added {} BCC recipients", bcc.length);
            }
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
            log.info("Email with CC/BCC sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email with CC/BCC to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Send a comprehensive email with all options
     *
     * @param to          recipient email address(es)
     * @param cc          CC email addresses (can be null)
     * @param bcc         BCC email addresses (can be null)
     * @param subject     email subject
     * @param htmlBody    email body (HTML content)
     * @param attachments files to attach (can be null)
     * @param from        custom from address (can be null to use default)
     */
    public void send(String[] to, String[] cc, String[] bcc, String subject, String htmlBody,
                     File[] attachments, String from) {
        try {
            log.info("Sending comprehensive email to {} recipients", to.length);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Set recipients
            helper.setTo(to);

            // Set CC if provided
            if (cc != null && cc.length > 0) {
                helper.setCc(cc);
                log.debug("Added {} CC recipients", cc.length);
            }

            // Set BCC if provided
            if (bcc != null && bcc.length > 0) {
                helper.setBcc(bcc);
                log.debug("Added {} BCC recipients", bcc.length);
            }

            // Set from if provided
            if (from != null && !from.isEmpty()) {
                helper.setFrom(from);
                log.debug("Using custom from address: {}", from);
            }

            // Set subject and body
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            // Add attachments if provided
            if (attachments != null) {
                for (File attachment : attachments) {
                    if (attachment.exists()) {
                        helper.addAttachment(attachment.getName(), attachment);
                        log.debug("Attached file: {}", attachment.getName());
                    } else {
                        log.warn("Attachment file not found: {}", attachment.getPath());
                    }
                }
            }

            mailSender.send(message);
            log.info("Comprehensive email sent successfully to {} recipients", to.length);
        } catch (MessagingException e) {
            log.error("Failed to send comprehensive email", e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Simplified send method with most common parameters
     *
     * @param to       recipient email address(es)
     * @param subject  email subject
     * @param htmlBody email body (HTML content)
     */
    public void send(String[] to, String subject, String htmlBody) {
        send(to, null, null, subject, htmlBody, null, null);
    }

    /**
     * Simplified send method for single recipient
     *
     * @param to       recipient email address
     * @param subject  email subject
     * @param htmlBody email body (HTML content)
     */
    public void send(String to, String subject, String htmlBody) {
        send(new String[]{to}, null, null, subject, htmlBody, null, null);
    }

}
