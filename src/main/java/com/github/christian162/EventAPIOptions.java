package com.github.christian162;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public class EventAPIOptions {
    private boolean registerInvalidChildren = false;
    private EventNode<? extends Event> defaultParentNode;

    public void setRegisterInvalidChildren(boolean registerInvalidChildren) {
        this.registerInvalidChildren = registerInvalidChildren;
    }

    public void setDefaultParentNode(EventNode<? extends Event> defaultParentNode) {
        this.defaultParentNode = defaultParentNode;
    }

    public boolean isRegisterInvalidChildren() {
        return registerInvalidChildren;
    }

    public EventNode<? extends Event> getDefaultParentNode() {
        return defaultParentNode;
    }
}
