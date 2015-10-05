package com.game;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by User on 27.08.2015.
 * Класс создающий игровое поле и обеспечивающий возможность корректного размещение на нем кораблей.
 */
public class GameField {
    private final int DIMENSION;
    private ArrayList<Ship> shipList = new ArrayList<>();
    private HashSet<Integer> collisionTestSet = new HashSet<>();
    private Ship lastDestroyedShip;

    public GameField(int fieldDimension) {
        DIMENSION = fieldDimension;
    }

    /*
    * Метод создает новый корабль и проверяет, вызывает ли новый корабль коллизии размещенными ранее кораблями,
    * пока очередной корабль не будет вызывать коллизий.
    * Помещает корабль на игровое поле.
    * Добавляет координаты нового корабля и клеток вокруг него в сет для проверки следующих кораблей на предмет
    * вызываемых коллизий.
    */
    public void setShip(int shipLength) {
        if (shipLength < 1) shipLength = 1;
        boolean collision;
        Ship newShip;
        do {
            collision = false;
            newShip = new Ship(shipLength, DIMENSION);
            for (int i = 0; i < shipLength; i++) {
                if (collisionTestSet.contains(newShip.getShipSections().get(i))) {
                    collision = true;
                    System.out.println("Collision on " + newShip); //Вывод сообщения о коллизии нужен только для отладки
                }
            }
        } while (collision);
        shipList.add(newShip);
        for (int i = 0; i < shipLength; i++) {
            collisionTestSet.add(newShip.getShipSections().get(i));
            collisionTestSet.add(newShip.getShipSections().get(i) - 1);
            collisionTestSet.add(newShip.getShipSections().get(i) + 1);
            collisionTestSet.add(newShip.getShipSections().get(i) - DIMENSION);
            collisionTestSet.add(newShip.getShipSections().get(i) - DIMENSION - 1);
            collisionTestSet.add(newShip.getShipSections().get(i) - DIMENSION + 1);
            collisionTestSet.add(newShip.getShipSections().get(i) + DIMENSION);
            collisionTestSet.add(newShip.getShipSections().get(i) + DIMENSION - 1);
            collisionTestSet.add(newShip.getShipSections().get(i) + DIMENSION + 1);
        }
    }

    /*
    * Метод для отображения игрового поля в консоли.
    * Нужен только на этапе отладки.
    */
    public void showGameField() {
        int[] gamefield = new int[DIMENSION*DIMENSION];
        for(Ship ship : shipList) {
            for (int i = 0; i < ship.LENGTH; i++) {
                gamefield[ship.getShipSections().get(i)] = 1;
            }
        }
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                System.out.print(gamefield[i * DIMENSION + j] + " ");
            }
            System.out.println();
        }
    }

    /*
    * Метод для возвращает реакцию игрового поля на выстрел.
    */
    public ShotResult shotResult(int shot) {
        for (Ship ship : shipList) {
            if (ship.niceShot(shot)) {
                if (ship.isDestroyed()) {
                    lastDestroyedShip = ship;
                    shipList.remove(ship);
                    return ShotResult.DESTROYED;
                } else {
                    return ShotResult.HIT;
                }
            }
        }
        return ShotResult.MISSED;
    }

    /*
    * Геттер ссылки на последний уничтоженный корабль.
    */
    public Ship getLastDestroyedShip() {
        return lastDestroyedShip;
    }

    /*
    * Метод проверяет, остались ли неунечтоженные корабли.
    */
    public boolean gameEnd() {
        return shipList.isEmpty();
    }
}
