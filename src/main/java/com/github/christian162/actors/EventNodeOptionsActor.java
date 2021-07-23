package com.github.christian162.actors;

import com.github.christian162.EventNodeOptions;
import com.github.christian162.annotations.Node;
import com.github.christian162.interfaces.Listener;
import net.minestom.server.event.Event;

public interface EventNodeOptionsActor {
    <T extends Event> void act(Listener listener, Node node, EventNodeOptions<T> eventNodeOptions);
}
