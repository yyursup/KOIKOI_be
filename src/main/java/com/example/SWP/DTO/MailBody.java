package com.example.SWP.DTO;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}
