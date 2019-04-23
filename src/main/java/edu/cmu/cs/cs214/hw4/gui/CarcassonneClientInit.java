package edu.cmu.cs.cs214.hw4.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class CarcassonneClientInit extends JPanel{
    JPanel initPanel;

    CarcassonneClientInit(JFrame parent){
        Object[] possiblePlayers = {2,3,4,5};
        int choice = JOptionPane.showOptionDialog(null,"How many players?",
                "Game Setup",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,
                possiblePlayers,possiblePlayers[0]);

        final int playerNum = (int)possiblePlayers[choice];

        JPanel playerNameEnter = new JPanel();
        parent.setTitle("Enter player names");
        JTextField[] nameFields = new JTextField[playerNum];

        for(int i=0;i<playerNum;i++){
            nameFields[i] = new JTextField(String.format("Player %d",i));
            playerNameEnter.add(nameFields[i]);
        }

        String[] names=new String[playerNum];
        JButton confirmNames = new JButton("confirm");
        confirmNames.addActionListener(e -> {
            for(int i=0;i<playerNum;i++){
                names[i] = nameFields[i].getText();
                System.out.println(nameFields[i].getText());
            }
            remove(playerNameEnter);
            SwingUtilities.invokeLater(()->{
                try{
                    System.out.println("game created!");
                    parent.setTitle("Carcassonne");
                    parent.setSize(1200,800);
                    parent.add(new CarcassonneClient(names));

                }
                catch(IOException E) {
                    E.printStackTrace();
                }
            });
        });
        playerNameEnter.add(confirmNames);
        add(playerNameEnter);
    }
}
