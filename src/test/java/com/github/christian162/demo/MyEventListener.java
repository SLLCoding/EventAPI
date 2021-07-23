package com.github.christian162.demo;

import com.github.christian162.annotations.EventListener;
import com.github.christian162.annotations.Filter;
import com.github.christian162.annotations.Node;
import com.github.christian162.interfaces.Listener;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.event.trait.PlayerEvent;

/*
    Parent nodes must be less strict then their child nodes.
    This structure works as each child event type is derived from their parent's event type.
    -> Event
        -> EntityEvent (extends Event)
            -> PlayerEvent (extends EntityEvent)

    This structure wouldn't work as InstanceEvent is not derived from EntityEvent
    -> EntityEvent
        -> InstanceEvent (DOES NOT extend EntityEvent)
*/
@Node(name = "my-simple-node", event = Event.class, priority = 0)
public class MyEventListener implements Listener {
    // put some listener methods here.

    @Node(name = "my-child-node", event = EntityEvent.class, priority = 1)
    public class MyChildListener implements Listener {
        // put some listener methods here.

        @Node(name = "my-other-child-node", event = PlayerEvent.class, priority = 2)
        public class MyOtherChildListener implements Listener {

            /*
                A filter can be used to only listen to events that meet a certain condition.

                The filter method must return a boolean and accept a single parameter of <T extends Event>.
                T being the event type defined in @Node#event.
            */
            @Filter
            public boolean check(PlayerEvent playerEvent) {
                return playerEvent.getPlayer().hasPermission("my.permission");
            }

            // The parameter type T, in this case PlayerLoginEvent
            // must be assignable from @Node#event
            @EventListener
            public void onPlayerLogin(PlayerLoginEvent playerLoginEvent) {

            }
        }
    }
}
