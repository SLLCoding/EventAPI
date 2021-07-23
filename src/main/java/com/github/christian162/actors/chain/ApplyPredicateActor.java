package com.github.christian162.actors.chain;

import com.github.christian162.EventNodeOptions;
import com.github.christian162.actors.EventNodeOptionsActor;
import com.github.christian162.annotations.Filter;
import com.github.christian162.annotations.Node;
import com.github.christian162.interfaces.Listener;
import net.minestom.server.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public class ApplyPredicateActor implements EventNodeOptionsActor {
    @Override
    public <T extends Event> void act(Listener listener, Node node, EventNodeOptions<T> eventNodeOptions) {
        Class<? extends Listener> listenerClass = listener.getClass();

        Optional<Method> firstMethod = Arrays.stream(listenerClass.getMethods())
                .filter(method -> isPredicateMethod(method, node))
                .findFirst();

        if (firstMethod.isEmpty()) {
            eventNodeOptions.setPredicate($ -> true);
            return;
        }

        Method method = firstMethod.get();

        Predicate<T> predicate = parameter -> {
            try {
                Object result = method.invoke(listener, parameter);
                return (boolean) result;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return false;
            }
        };

        eventNodeOptions.setPredicate(predicate);
    }

    private boolean isPredicateMethod(Method method, Node node) {
        Class<?> returnType = method.getReturnType();
        Parameter[] parameters = method.getParameters();

        if (!method.isAnnotationPresent(Filter.class)) {
            return false;
        }

        if (!returnType.equals(Boolean.class) && !returnType.equals(boolean.class)) {
            return false;
        }

        Class<? extends Event> event = node.event();

        if (parameters.length != 1) {
            return false;
        }

        Parameter parameter = parameters[0];
        Class<?> type = parameter.getType();

        return type.equals(event);
    }
}