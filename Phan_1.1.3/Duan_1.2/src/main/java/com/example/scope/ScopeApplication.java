package com.example.scope;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ScopeApplication {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ProjectConfig.class)) {
            VehicleServices vehicleServices1 = context.getBean(VehicleServices.class);
            VehicleServices vehicleServices2 = context.getBean("vehicleServices", VehicleServices.class);

            System.out.println("Hashcode of vehicleServices1: " + vehicleServices1.hashCode());
            System.out.println("Hashcode of vehicleServices2: " + vehicleServices2.hashCode());
            System.out.println("Service: " + vehicleServices1.serviceName());
            System.out.println(vehicleServices1 == vehicleServices2
                    ? "VehicleServices bean is a singleton scoped bean"
                    : "VehicleServices bean is a prototype scoped bean");
        }
    }
}
