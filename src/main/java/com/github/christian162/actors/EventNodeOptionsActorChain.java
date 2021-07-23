package com.github.christian162.actors;

import com.github.christian162.EventNodeOptions;
import com.github.christian162.annotations.Node;
import com.github.christian162.interfaces.Listener;
import net.minestom.server.event.Event;

public class EventNodeOptionsActorChain {
    private final EventNodeOptionsActor[] eventNodeOptionsActors;

    public EventNodeOptionsActorChain(EventNodeOptionsActor[] eventNodeOptionsActors) {
        this.eventNodeOptionsActors = eventNodeOptionsActors;
    }

    public <T extends Event> EventNodeOptions<T> performActions(Listener listener, Node node) {
        EventNodeOptions<T> eventNodeOptions = new EventNodeOptions<>();

        for (EventNodeOptionsActor eventNodeOptionsActor : eventNodeOptionsActors) {
            eventNodeOptionsActor.act(listener, node, eventNodeOptions);
        }

        return eventNodeOptions;
    }
}