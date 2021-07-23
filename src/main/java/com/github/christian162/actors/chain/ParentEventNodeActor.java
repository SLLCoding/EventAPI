package com.github.christian162.actors.chain;

import com.github.christian162.EventAPIOptions;
import com.github.christian162.EventNodeContainer;
import com.github.christian162.EventNodeOptions;
import com.github.christian162.actors.EventNodeOptionsActor;
import com.github.christian162.annotations.Node;
import com.github.christian162.interfaces.Listener;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

import java.util.Optional;

public class ParentEventNodeActor implements EventNodeOptionsActor {
    private final EventAPIOptions eventAPIOptions;
    private final EventNodeContainer eventNodeContainer;

    public ParentEventNodeActor(EventAPIOptions eventAPIOptions, EventNodeContainer eventNodeContainer) {
        this.eventAPIOptions = eventAPIOptions;
        this.eventNodeContainer = eventNodeContainer;
    }

    @Override
    public <T extends Event> void act(Listener listener, Node node, EventNodeOptions<T> eventNodeOptions) {
        Class<? extends Listener> listenerClass = listener.getClass();
        Class<?> enclosingClass = listenerClass.getEnclosingClass();

        if (enclosingClass == null) {
            eventNodeOptions.setParentEventNode(eventAPIOptions.getDefaultParentNode());
            return;
        }

        Node listenerNode = listenerClass.getAnnotation(Node.class);
        Node enclosingNode = enclosingClass.getAnnotation(Node.class);

        if (listenerNode == null) {
            return;
        }

        if (enclosingNode == null) {
            eventNodeOptions.setParentEventNode(eventAPIOptions.getDefaultParentNode());
            return;
        }

        Class<? extends Event> listenerEvent = listenerNode.event();
        Class<? extends Event> topLevelEvent = enclosingNode.event();

        if (!topLevelEvent.isAssignableFrom(listenerEvent) ||
            topLevelEvent.equals(listenerEvent)) {

            if (eventAPIOptions.isRegisterInvalidChildren()) {
                eventNodeOptions.setParentEventNode(eventAPIOptions.getDefaultParentNode());
            } else {
                eventNodeOptions.setParentEventNode(null);
            }

            return;
        }

        String name = enclosingNode.name();
        Optional<EventNode<? extends Event>> eventNodeOptional = eventNodeContainer.getEventNode(name);

        if (eventNodeOptional.isEmpty()) {
            eventNodeOptions.setParentEventNode(eventAPIOptions.getDefaultParentNode());
            return;
        }

        eventNodeOptions.setParentEventNode(eventNodeOptional.get());
    }
}