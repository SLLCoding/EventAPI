package com.github.christian162.demo;

import com.github.christian162.EventAPI;
import com.github.christian162.EventAPIOptions;
import net.minestom.server.MinecraftServer;

public class MyMainClass {
    public static void main(String[] args) {
        EventAPIOptions eventAPIOptions = new EventAPIOptions();
        eventAPIOptions.setDefaultParentNode(MinecraftServer.getGlobalEventHandler());

        EventAPI eventAPI = new EventAPI(eventAPIOptions);
        eventAPI.register(new MyEventListener());
    }
}
