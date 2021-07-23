package com.github.christian162;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EventNodeContainer {
    private final Map<String, EventNode<? extends Event>> registeredNodes;

    public EventNodeContainer() {
        this.registeredNodes = new HashMap<>();
    }

    public Optional<EventNode<? extends Event>> getEventNode(String name) {
        return Optional.ofNullable(registeredNodes.get(name));
    }

    public void add(EventNode<? extends Event> eventNode) {
        String name = eventNode.getName();
        registeredNodes.putIfAbsent(name, eventNode);
    }
}