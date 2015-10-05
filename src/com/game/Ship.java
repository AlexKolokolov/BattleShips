package com.game;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by User on 27.08.2015.
 * Класс, обеспечивающий создание кораблей и задающий их реакцию на попадания.
 */
public class Ship {
    public final String TYPE;
    public final int LENGTH;
    private ArrayList<Integer> shipSections;
    private int heatCounter;

    public Ship(int length, int fieldDimension) {
        LENGTH = length;
        TYPE = defineShipType();
        createShip(fieldDimension);
    }

    /*
    * Метод создает корабль.
    */
    private void createShip(int fieldDimension) {
        Random rand = new Random();
        boolean vertical = rand.nextBoolean();
        int firstSectionPosition = (vertical ? rand.nextInt(fieldDimension * fieldDimension - (fieldDimension * (LENGTH - 1)))
                : rand.nextInt(fieldDimension - LENGTH + 1) + rand.nextInt(fieldDimension) * fieldDimension);

        shipSections = new ArrayList<>();
        for (int i = 0; i < LENGTH; i++) {
            shipSections.add(vertical ? firstSectionPosition + (fieldDimension * i) : firstSectionPosition + i);
        }
    }

    /*
    * Метод выдает тип корабля в зависимости от его длины.
    */
    private String defineShipType() {
        switch (LENGTH) {
            case 1: {
                return "Cruiser";
            }
            case 2: {
                return "Submarine";
            }
            case 3: {
                return "Battleship";
            }
            case 4: {
                return "Aircraft carrier";
            }
            default: {
                return "Ship";
            }
        }
    }

    /*
    * Метод проверяет, было ли попадание по кораблю.
    * В случае попадания инкрементирует счетчик попаданий.
    */
    public boolean niceShot(int shot) {
        if (shipSections.contains(shot)) {
            heatCounter++;
            return true;
        } else {
            return false;
        }
    }

    /*
    * Метод проверяет, был ли корабль уничтожен
    */
    public boolean isDestroyed() {
        return heatCounter == LENGTH;
    }

    /*
    * Геттер списка координат секций корабля.
    */
    public ArrayList<Integer> getShipSections() {
        return new ArrayList<>(shipSections);
    }

    @Override
    public String toString() {
        return TYPE + " " + getShipSections();
    }
}
