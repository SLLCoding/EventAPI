package com.github.christian162.actors.chain;

import com.github.christian162.EventNodeOptions;
import com.github.christian162.actors.EventNodeOptionsActor;
import com.github.christian162.annotations.Node;
import com.github.christian162.interfaces.Listener;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.trait.*;

import java.util.Map;

public class EventFilterActor implements EventNodeOptionsActor {
    private final Map<Class<?>, EventFilter<? extends Event, ?>> eventFilterMap;

    public EventFilterActor() {
        this.eventFilterMap = Map.ofEntries(
            Map.entry(Event.class, EventFilter.ALL),
            Map.entry(EntityEvent.class, EventFilter.ENTITY),
            Map.entry(InstanceEvent.class, EventFilter.INSTANCE),
            Map.entry(PlayerEvent.class, EventFilter.PLAYER),
            Map.entry(ItemEvent.class, EventFilter.ITEM),
            Map.entry(InventoryEvent.class, EventFilter.INVENTORY)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void act(Listener listener, Node node, EventNodeOptions<T> eventNodeOptions) {
        Class<? extends Event> event = node.event();
        EventFilter<T, ?> eventFilter = (EventFilter<T, ?>) eventFilterMap.getOrDefault(event, EventFilter.ALL);
        eventNodeOptions.setEventFilter(eventFilter);
    }
}