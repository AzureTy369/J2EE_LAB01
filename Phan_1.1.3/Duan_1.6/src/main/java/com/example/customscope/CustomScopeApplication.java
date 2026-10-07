package com.example.customscope;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CustomScopeApplication {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(CustomScopeConfig.class)) {
            RequestContext first = context.getBean(RequestContext.class);
            RequestContext second = context.getBean(RequestContext.class);
            System.out.println("Cùng thread, cùng bean: " + (first == second));
            System.out.println("Context id: " + first.id());
            Thread worker = new Thread(() -> {
                RequestContext other = context.getBean(RequestContext.class);
                System.out.println("Thread khác, bean mới: " + (first != other));
                System.out.println("Other context id: " + other.id());
            }, "worker-thread");
            worker.start();
            worker.join();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Worker thread was interrupted", exception);
        }
    }
}
