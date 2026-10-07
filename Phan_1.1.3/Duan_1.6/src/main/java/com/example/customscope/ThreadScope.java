package com.example.customscope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

public class ThreadScope implements Scope {
    private final ThreadLocal<Map<String, Object>> values =
            ThreadLocal.withInitial(HashMap::new);

    public Object get(String name, ObjectFactory<?> objectFactory) {
        return values.get().computeIfAbsent(name, key -> objectFactory.getObject());
    }
    public Object remove(String name) { return values.get().remove(name); }
    public void registerDestructionCallback(String name, Runnable callback) { }
    public Object resolveContextualObject(String key) { return null; }
    public String getConversationId() { return Thread.currentThread().getName(); }
}
