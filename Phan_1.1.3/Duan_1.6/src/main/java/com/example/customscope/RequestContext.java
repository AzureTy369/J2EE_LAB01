package com.example.customscope;

import java.util.UUID;

public class RequestContext {
    private final String id = UUID.randomUUID().toString();
    public String id() { return id; }
}
