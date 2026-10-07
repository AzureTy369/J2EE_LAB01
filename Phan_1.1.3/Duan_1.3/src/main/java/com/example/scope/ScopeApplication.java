package com.example.scope;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ScopeApplication {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ProjectConfig.class)) {
            VehicleService singleton1 = context.getBean("singletonVehicleService", VehicleService.class);
            VehicleService singleton2 = context.getBean("singletonVehicleService", VehicleService.class);
            VehicleService prototype1 = context.getBean("prototypeVehicleService", VehicleService.class);
            VehicleService prototype2 = context.getBean("prototypeVehicleService", VehicleService.class);
            System.out.println("SINGLETON cùng object: " + (singleton1 == singleton2));
            System.out.println("PROTOTYPE cùng object: " + (prototype1 == prototype2));
            System.out.println("Singleton hashcodes: " + singleton1.hashCode() + ", " + singleton2.hashCode());
            System.out.println("Prototype hashcodes: " + prototype1.hashCode() + ", " + prototype2.hashCode());
        }
    }
}
