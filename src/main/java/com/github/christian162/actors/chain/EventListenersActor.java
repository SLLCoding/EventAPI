package com.github.christian162.actors.chain;

import com.github.christian162.EventNodeOptions;
import com.github.christian162.actors.EventNodeOptionsActor;
import com.github.christian162.annotations.EventListener;
import com.github.christian162.annotations.Node;
import com.github.christian162.interfaces.Listener;
import net.minestom.server.event.Event;

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

        List<Consumer<? extends T>> consumers = Arrays.stream(listenerClass.getMethods())
                .filter(method -> isListenerMethod(method, event))
                .map(method -> (Consumer<? extends T>) getConsumer(listener, method))
                .collect(Collectors.toList());

        eventNodeOptions.setConsumers(consumers);
    }

    private <T extends Event> Consumer<? extends T> getConsumer(Listener listener, Method method) {
        return parameter -> {
            try {
                method.invoke(listener, parameter);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        };
    }

    private boolean isListenerMethod(Method method, Class<? extends Event> event) {
        if (!method.isAnnotationPresent(EventListener.class)) {
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