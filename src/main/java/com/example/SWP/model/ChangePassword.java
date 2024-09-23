package com.example.SWP.model;

import lombok.Builder;

@Builder
public record ChangePassword(String password, String repeatPassword) {
}
