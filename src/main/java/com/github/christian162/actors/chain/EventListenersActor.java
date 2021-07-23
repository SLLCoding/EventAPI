package com.github.christian162.actors.chain;

import com.github.christian162.EventNodeOptions;
import com.github.christian162.actors.EventNodeOptionsActor;
import com.github.christian162.annotations.Listen;
import com.github.christian162.annotations.Node;
import com.github.christian162.interfaces.Listener;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EventListenersActor implements EventNodeOptionsActor {
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void act(Listener listener, Node node, EventNodeOptions<T> eventNodeOptions) {
        Class<? extends Event> event = node.event();
        Class<? extends Listener> listenerClass = listener.getClass();

        List<EventListener<? extends T>> eventListeners = Arrays.stream(listenerClass.getMethods())
                .filter(method -> isListenerMethod(method, event))
                .map(method -> (EventListener<? extends T>) getEventListener(node, listener, method))
                .collect(Collectors.toList());

        eventNodeOptions.setEventListeners(eventListeners);
    }

    @SuppressWarnings("unchecked")
    private <K extends Event, T extends K> EventListener<T> getEventListener(Node node, Listener listener, Method method) {
        return new EventListener<T>() {
            @Override
            public @NotNull Class<T> getEventType() {
                return (Class<T>) method.getParameters()[0].getType();
            }

            @Override
            public @NotNull Result run(@NotNull T t) {
                try {
                    method.invoke(listener, t);
                    return Result.SUCCESS;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return Result.EXCEPTION;
                }
            }
        };
    }

    private boolean isListenerMethod(Method method, Class<? extends Event> event) {
        if (!method.isAnnotationPresent(Listen.class)) {
            return false;
        }

        Parameter[] parameters = method.getParameters();

        if (parameters.length != 1) {
            return false;
        }

        Parameter parameter = parameters[0];
        Class<?> parameterType = parameter.getType();

        Class<?>[] interfaces = parameterType.getInterfaces();
        return interfaces.length > 0 && interfaces[0].equals(event);
    }
}