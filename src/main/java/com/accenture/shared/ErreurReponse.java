package com.accenture.shared;

import java.time.LocalDateTime;

public record ErreurReponse(LocalDateTime temporalite, String type, String message) {}
