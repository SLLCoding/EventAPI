package com.github.christian162;

import com.github.christian162.actors.EventNodeOptionsActor;
import com.github.christian162.actors.EventNodeOptionsActorChain;
import com.github.christian162.actors.chain.ApplyPredicateActor;
import com.github.christian162.actors.chain.EventFilterActor;
import com.github.christian162.actors.chain.EventListenersActor;
import com.github.christian162.actors.chain.ParentEventNodeActor;
import com.github.christian162.annotations.Node;
import com.github.christian162.interfaces.Listener;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;

import java.util.*;
import java.util.function.Predicate;

// TODO: Implement auto-resolving child nodes.
public class EventAPI {
    private final EventAPIOptions eventAPIOptions;
    private final EventNodeContainer eventNodeContainer;
    private final EventNodeOptionsActorChain eventNodeOptionsActorChain;

    public EventAPI(EventAPIOptions eventAPIOptions) {
        this.eventAPIOptions = eventAPIOptions;
        this.eventNodeContainer = new EventNodeContainer();

        EventNodeOptionsActor[] eventNodeOptionsActors = new EventNodeOptionsActor[] {
            new ParentEventNodeActor(eventAPIOptions, eventNodeContainer),
            new EventFilterActor(),
            new ApplyPredicateActor(),
            new EventListenersActor()
        };

        this.eventNodeOptionsActorChain = new EventNodeOptionsActorChain(eventNodeOptionsActors);
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void register(Listener listener) {
        Class<? extends Listener> listenerClass = listener.getClass();
        Node node = listenerClass.getAnnotation(Node.class);

        if (node == null) {
            return;
        }

        String name = node.name();
        Optional<EventNode<? extends Event>> eventNodeOptional = eventNodeContainer.getEventNode(name);

        if (eventNodeOptional.isPresent()) {
            // throw exception?
            return;
        }

        EventNodeOptions<? extends T> eventEventNodeOptions = eventNodeOptionsActorChain.performActions(listener, node);
        EventNode<T> parentEventNode = (EventNode<T>) eventEventNodeOptions.getParentEventNode();

        addChild(node, parentEventNode, eventEventNodeOptions);
    }

    private <T extends Event, K extends T> void addChild(Node node, EventNode<T> parentNode, EventNodeOptions<K> eventNodeOptions) {
        EventFilter<K, ?> eventFilter = eventNodeOptions.getEventFilter();
        List<EventListener<? extends K>> eventListeners = eventNodeOptions.getEventListeners();
        Predicate<K> predicate = eventNodeOptions.getPredicate();

        if (parentNode == null) {
            return;
        }

        EventNode<K> eventNode = EventNode.event(node.name(), eventFilter, predicate);
        eventNode.setPriority(node.priority());

        for (EventListener<? extends K> eventListener : eventListeners) {
            eventNode.addListener(eventListener);
        }

        parentNode.addChild(eventNode);
    }
}