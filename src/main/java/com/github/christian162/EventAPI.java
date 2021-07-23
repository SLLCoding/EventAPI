package com.github.christian162;

import com.github.christian162.annotations.EventListener;
import com.github.christian162.annotations.Filter;
import com.github.christian162.annotations.Node;
import com.github.christian162.interfaces.Listener;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EventAPI {
    private final Map<Class<?>, EventFilter<? extends Event, ?>> eventFilterMap;

    public EventAPI() {
        this.eventFilterMap = Map.ofEntries(
            Map.entry(Event.class, EventFilter.ALL),
            Map.entry(EntityEvent.class, EventFilter.ENTITY),
            Map.entry(InstanceEvent.class, EventFilter.INSTANCE),
            Map.entry(PlayerEvent.class, EventFilter.PLAYER),
            Map.entry(ItemEvent.class, EventFilter.ITEM),
            Map.entry(InventoryEvent.class, EventFilter.INVENTORY)
        );
    }

    public <T extends Event> void registerListener(EventNode<T> parentNode, Listener listener) {
        Class<? extends Listener> listenerClass = listener.getClass();
        Node node = listenerClass.getAnnotation(Node.class);

        if (node == null) {
            return;
        }

        Optional<Method> filterMethodOptional = Arrays.stream(listenerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Filter.class))
                .findFirst();

        List<Method> methods = Arrays.stream(listenerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(EventListener.class))
                .collect(Collectors.toList());

        EventNode<? extends T> eventNode = getEventNode(listener, node, filterMethodOptional.orElse(null), methods);

        if (eventNode == null) {
            return;
        }

        parentNode.addChild(eventNode);
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> EventNode<T> getEventNode(Listener instance, Node node, Method filterMethod, List<Method> listeners) {
        String eventName = node.name();

        if (eventName.isBlank()) {
            return null;
        }

        Class<? extends Event> eventTypeClass = node.event();
        Predicate<T> predicate = getPredicate(instance, eventTypeClass, filterMethod);
        EventNode<T> eventNode = getEventNode(eventName, eventTypeClass, predicate);

        if (eventNode == null) {
            return null;
        }

        for (Method method : listeners) {
            if (method.getParameterCount() != 1) {
                continue;
            }

            Parameter[] parameters = method.getParameters();
            Parameter parameter = parameters[0];

            Class<?> type = parameter.getType();

            if (!isFilterType(type, eventTypeClass)) {
                continue;
            }

            Class<? extends T> typeEvent = (Class<? extends T>) type;

            eventNode.addListener(typeEvent, event -> {
                try {
                    method.invoke(instance, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        }

        eventNode.setPriority(node.priority());
        return eventNode;
    }

    private <T extends Event> Predicate<T> getPredicate(Listener listener, Class<? extends Event> eventTypeClass, Method filterMethod) {
        Predicate<T> predicate = $ -> true;

        if (filterMethod == null) {
            return predicate;
        }

        Class<?> returnType = filterMethod.getReturnType();

        if (!returnType.equals(Boolean.class) && !returnType.equals(boolean.class)) {
            return predicate;
        }

        if (filterMethod.getParameterCount() != 1) {
            return predicate;
        }


        Parameter[] parameters = filterMethod.getParameters();
        Parameter parameter = parameters[0];

        if (!parameter.getType().equals(eventTypeClass)) {
            return predicate;
        }

        return param -> {
            try {
                Object result = filterMethod.invoke(listener, param);
                return (boolean) result;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return false;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> EventNode<T> getEventNode(String name, Class<? extends Event> eventClass, Predicate<T> predicate) {
        EventFilter<? extends Event, ?> eventFilter = eventFilterMap.get(eventClass);

        if (eventFilter == null) {
            return null;
        }

        return EventNode.event(name, (EventFilter<T, ?>) eventFilter, predicate);
    }

    private boolean isFilterType(Class<?> parameterType, Class<?> filterType) {
        Class<?>[] interfaces = parameterType.getInterfaces();
        return interfaces.length > 0 && interfaces[0].equals(filterType);
    }
}