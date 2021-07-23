package com.github.christian162.demo;

import com.github.christian162.EventAPI;
import com.github.christian162.EventAPIOptions;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;

public class MyMainClass {
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
}
