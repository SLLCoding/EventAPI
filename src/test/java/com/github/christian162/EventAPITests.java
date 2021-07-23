package com.github.christian162;

import com.github.christian162.annotations.Node;
import com.github.christian162.listeners.MyEventListener;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class EventAPITests {
    @Test
    public void getNodeTest() {
        EventAPI eventAPI = new EventAPI();
        MyEventListener myEventListener = new MyEventListener();

        Optional<Node> nodeOptional = eventAPI.getNodeOptional(myEventListener);
        Assertions.assertFalse(nodeOptional.isEmpty());

        Node node = nodeOptional.get();
        Assertions.assertEquals("my-simple-node", node.name());
        Assertions.assertEquals(Event.class, node.event());
        Assertions.assertEquals(0, node.priority());
    }

    @Test
    public void getEventFilterTest() {
        EventAPI eventAPI = new EventAPI();
        MyEventListener myEventListener = new MyEventListener();

        EventFilter<? extends Event, ?> expected = EventFilter.ALL;
        EventFilter<? extends Event, ?> actual = eventAPI.getEventFilter(myEventListener.getClass());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void instanceIsListenerTest() {
        EventAPI eventAPI = new EventAPI();
        MyEventListener myEventListener = new MyEventListener();

        boolean actual = eventAPI.isListener(myEventListener);
        Assertions.assertTrue(actual);
    }
}
