package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by User on 16.08.2015.
 * Класс, создающий игровую панель из клеток 10х10 и обеспечивающий реакцию на клики мышкой по клеткам панели.
 */
public class GamePanel extends JPanel {
    private JButton[] gameButtons;
    private GameField field;
    private HashSet<Integer> pressedButtons;
    private JTextField gameMessage;
    private int shotsCounter = 0;

    public GamePanel() {
        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new GridLayout(10, 10));
        gameButtons = new JButton[100];
        for (int i = 0; i < 100; i++) {
            gameButtons[i] = new JButton("+");
            gameButtons[i].setActionCommand(String.valueOf(i));
            gameButtons[i].setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            gameButtons[i].addActionListener(new GameButtonListener());
            buttonPanel.add(gameButtons[i]);
        }
        add(buttonPanel, BorderLayout.CENTER);

        pressedButtons = new HashSet<>();

        gameMessage = new JTextField();
        add(gameMessage, BorderLayout.NORTH);

        gameMessage.setText("Take a shot!");

        createGameField();
    }

    /*
    * Метод создает игровое поле размера 10х10 и заполняет его кораблями.
    */
    private void createGameField() {
        field = new GameField(10);
        field.setShip(4);
        field.setShip(3);
        field.setShip(3);
        field.setShip(2);
        field.setShip(2);
        field.setShip(2);
        field.setShip(1);
        field.setShip(1);
        field.setShip(1);
        field.setShip(1);
        field.showGameField(); //Строка выводит игровое поле в консоль. Нужна только для отладки.
    }


    /*
    * Метод меняет игровое поле после нажатия на кнопку
    */
    private void buttonPressingReaction(ActionEvent event) {

        if (!field.gameEnd()) {
            shotsCounter++;
            JButton pressedButton = (JButton) event.getSource();
            int buttonNumber = Integer.parseInt(pressedButton.getActionCommand());
            ShotResult shotResult = getShotResult(buttonNumber);

            switch (shotResult) {
                case MISSED: {
                    gameButtons[buttonNumber].setBackground(Color.CYAN);
                    gameMessage.setText("Missed!");
                    break;
                }
                case HIT: {
                    gameButtons[buttonNumber].setBackground(Color.GREEN);
                    gameMessage.setText("Hit!");
                    break;
                }
                case DESTROYED: {
                    Ship lastDestroyedShip = field.getLastDestroyedShip();
                    ArrayList<Integer> lastDestroyedShipCoors = lastDestroyedShip.getShipSections();
                    for(int cell : lastDestroyedShipCoors) {
                        gameButtons[cell].setBackground(Color.RED);
                    }
                    gameMessage.setText(String.valueOf(lastDestroyedShip) + " was destroyed!");
                    break;
                }
                case REPEATED_HIT: {
                    gameMessage.setText("Wrong repeated hit!");
                }
            }
        }

        if (field.gameEnd()) {

            String winMessage = shotsCounter < 55 ? "Good job! Shots made: " + shotsCounter
                    : "Too bad! " + shotsCounter + " shots is not a good result!";

            int dialog = JOptionPane.showConfirmDialog(new JFrame(),
                    winMessage + "\nDo you want to start new game?",
                    "All ships were destroyed!", JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.OK_OPTION) {
                MainWindow.getMainWindow().remove(this);
                MainWindow.getMainWindow().setGamePanel();
            }
        }
    }

    /*
    * Метод выдает реакцию игрового поля на переданную координату произведенного выстрела.
    */
    private ShotResult getShotResult(int buttonNumber) {
        if (!pressedButtons.contains(buttonNumber)) {
            pressedButtons.add(buttonNumber);
            return field.shotResult(buttonNumber);
        } else {
            return ShotResult.REPEATED_HIT;
        }
    }

    class GameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            buttonPressingReaction(e);
        }
    }
}