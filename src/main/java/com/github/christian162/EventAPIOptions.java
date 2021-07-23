package com.github.christian162;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public class EventAPIOptions {
    private boolean autoResolveChildren;
    private EventNode<? extends Event> defaultParentNode;

    public void setDefaultParentNode(EventNode<? extends Event> defaultParentNode) {
        this.defaultParentNode = defaultParentNode;
    }

    public void setAutoResolveChildren(boolean autoResolveChildren) {
        this.autoResolveChildren = autoResolveChildren;
    }

    public boolean isAutoResolveChildren() {
        return autoResolveChildren;
    }

    public EventNode<? extends Event> getDefaultParentNode() {
        return defaultParentNode;
    }
}
