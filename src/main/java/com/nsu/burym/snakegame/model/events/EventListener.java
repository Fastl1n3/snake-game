package com.nsu.burym.snakegame.model.events;

import java.net.DatagramPacket;
import java.util.List;
import java.util.Map;

import com.nsu.burym.snakegame.model.SnakesProto;
import com.nsu.burym.snakegame.model.announcement.IpAddress;


public interface EventListener {
    default void update(String eventType, Map<IpAddress, SnakesProto.GameMessage.AnnouncementMsg> availableGames) {

    }

    default void update(String eventType, List<SnakesProto.GameState.Snake> snakes, List<SnakesProto.GameState.Coord> food, List<SnakesProto.GamePlayer> players) {

    }

    default void update(String eventType, DatagramPacket receivedPacket) {

    }

    default void update(String eventType, String message) {

    }

    default void update(String eventType) {

    }
}
