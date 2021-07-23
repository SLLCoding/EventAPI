package com.github.christian162.listeners;

import com.github.christian162.annotations.Node;
import com.github.christian162.interfaces.Listener;
import net.minestom.server.event.Event;

@Node(name = "my-simple-node", event = Event.class, priority = 0)
public class MyEventListener implements Listener {
}
