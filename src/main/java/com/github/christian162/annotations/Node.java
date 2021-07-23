package com.github.christian162.annotations;

import net.minestom.server.event.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Node {
    String name() default "";
    Class<? extends Event> event();
    int priority() default 0;
}