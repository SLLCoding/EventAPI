package com.github.christian162;

import com.github.christian162.annotations.Node;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.trait.*;

import java.util.Map;
import java.util.Optional;

public class EventAPI {
    private String a;
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

    Optional<Node> getNodeOptional(Object object) {
        Class<?> objectClass = object.getClass();
        Node node = objectClass.getAnnotation(Node.class);
        return Optional.ofNullable(node);
    }

    EventFilter<? extends Event, ?> getEventFilter(Class<?> clazz) {
        Optional<Node> nodeOptional = getNodeOptional(clazz);

        if (nodeOptional.isEmpty()) {
            return EventFilter.ALL;
        }

        Node node = nodeOptional.get();
        Class<? extends Event> eventClass = node.event();
        return eventFilterMap.getOrDefault(eventClass, EventFilter.ALL);
    }

    boolean isListener(Object object) {
        Optional<Node> nodeOptional = getNodeOptional(object);

        if (nodeOptional.isEmpty()) {
            return false;
        }

        Node node = nodeOptional.get();
        return !node.name().isBlank();
    }
}