package com.github.christian162;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;

import java.util.List;
import java.util.function.Predicate;

public class EventNodeOptions<T extends Event> {
    private Predicate<T> predicate;
    private List<EventListener<? extends T>> eventListeners;
    private EventNode<? extends Event> parentEventNode;
    private EventFilter<T, ?> eventFilter;

    public void setPredicate(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public void setEventListeners(List<EventListener<? extends T>> eventListeners) {
        this.eventListeners = eventListeners;
    }

    public void setParentEventNode(EventNode<? extends Event> parentEventNode) {
        this.parentEventNode = parentEventNode;
    }

    public void setEventFilter(EventFilter<T, ?> eventFilter) {
        this.eventFilter = eventFilter;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    public List<EventListener<? extends T>> getEventListeners() {
        return eventListeners;
    }

    public EventNode<? extends Event> getParentEventNode() {
        return parentEventNode;
    }

    public EventFilter<T, ?> getEventFilter() {
        return eventFilter;
    }
}
