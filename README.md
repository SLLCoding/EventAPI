# EventAPI
An annotation based framework for Minestom's event API
(docs and demo will be updated shortly)

[![](https://jitpack.io/v/christian162/EventAPI.svg)](https://jitpack.io/#christian162/EventAPI)

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.christian162</groupId>
    <artifactId>EventAPI</artifactId>
    <version>1.0.0-BETA</version>
</dependency>

# Demo
```java
public static void main(String[] args) {
    GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

    EventAPIOptions eventAPIOptions = new EventAPIOptions();
    eventAPIOptions.setDefaultParentNode(globalEventHandler);
    eventAPIOptions.setRegisterInvalidChildren(false);

    EventAPI eventAPI = new EventAPI(eventAPIOptions);
    eventAPI.register(new MyEventListener());

    // sub node of MyEventListener
    eventAPI.register(new MyEventListener.MyChildListener());

    // sub node of MyEventListener.MyChildListener
    eventAPI.register(new MyEventListener.MyChildListener.MyOtherChildListener());
}

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
    public static class MyChildListener implements Listener {
        // put some listener methods here.

        @Node(name = "my-other-child-node", event = PlayerEvent.class, priority = 2)
        public static class MyOtherChildListener implements Listener {

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
            @Listen
            public void onPlayerLogin(PlayerLoginEvent playerLoginEvent) {

            }
        }

        /*
            This is an invalid child node

            What will happen????
            If EventAPIOptions#registerInvalidChildren is true ->
                This node will still be registered, but it will be registered normally under the DEFAULT parent node
                (EventAPIOptions#defaultParentNode)
            Else ->
                This node will not be registered
        */

        @Node(name = "testing-node", event = InstanceEvent.class, priority = 3)
        public static class MyInvalidSubListener implements Listener {
            // ...
        }
    }
}
```
