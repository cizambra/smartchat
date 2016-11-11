package com.pxw.smartchat.model;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;

@NoArgsConstructor
public class Message {
    private @NonNull String body;
    private final Instant createdAt = Instant.now();

    public Message(@NonNull final String body) {
        this.body = body;
    }

    public String getBody() { return body; }

    public Instant getCreatedAt() { return createdAt; }
}