package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.GameSystem;
import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.Tile;

import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * class to represent the game component of the gui
 */
class CarcassonneClient extends JPanel {
    private int cursorX; //coords relative to board
    private int cursorY;
    private int gamePosX; //coords relative to gui
    private int gamePosY;
    private GameSystem game;
    private Tile[][] gameBoard;
    private TileButton[][] gameButtons;
    private JPanel boardPanel;
    private JPanel scoreboard;
    private TileButton curTile;
    private JPanel[] scorePanels;

    private int[][] followerCoords = new int[][]{{40,25},{130,25},{145,50},{145,120},{130,140},{40,140},{25,120},{25,50}};
    private int[][] roadFollowerCoords = new int[][]{{80,25},{145,90},{80,140},{40,90}};
    private int followerIndex=-1;
    private Color[] colors = new Color[]{Color.yellow,Color.blue,Color.green,Color.black,Color.red};

    /**
     * constructor for the game component
     * @param args array of player names
     * @throws IOException throws IOException if creating tiles goes wrong
     */
    CarcassonneClient(String[] args) throws IOException {
        cursorX=35;
        cursorY=35;
        gamePosX = 1;
        gamePosY = 2;
        game = new GameSystem(args);
        game.drawTile();
        gameBoard = game.getGBoard().getBoard();
        gameButtons = new TileButton[72][72];
        for(int j=0;j<72;j++){
            for(int i=0;i<72;i++){
                gameButtons[j][i] = new TileButton(null);
            }
        }

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(5,5));
        renderBoard(boardPanel);

        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am =getActionMap();
        //R rotates current tile
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "rotate");
        am.put("rotate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // r pressed
                game.rotate();
                curTile.rotate();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0), "cursorUp");
        am.put("cursorUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // up pressed

                if(gamePosY>0) gamePosY--;
                else if(cursorY>0) cursorY--;
                updateBoard();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0), "cursorDown");
        am.put("cursorDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // down pressed

                if(gamePosY<4) gamePosY++;
                else if(cursorY<71) cursorY++;
                updateBoard();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0), "cursorRight");
        am.put("cursorRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // right pressed

                if(gamePosX<4) gamePosX++;
                else if(cursorX<71) cursorX++;
                updateBoard();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0), "cursorLeft");
        am.put("cursorLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // left pressed

                if(gamePosX>0) gamePosX--;
                else if(cursorX>0) cursorX--;
                updateBoard();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "placeTile");
        am.put("placeTile", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // enter pressed


                if(!game.placeTile(cursorX-2+gamePosX,cursorY-2+gamePosY)){
                    System.out.println("cant!");
                }
                else{
                    curTile.setBorder(BorderFactory.createEmptyBorder());
                    gameButtons[cursorY-2+gamePosY][cursorX-2+gamePosX] = curTile;
                    setFollowerGUI();
                }
            }
        });

        add(boardPanel);

        scoreboard = new JPanel();
        scoreboard.setLayout(new GridBagLayout());

        scorePanels = new JPanel[args.length];
        updateScores();
        add(scoreboard);

        setVisible(true);
    }

    private void renderBoard(JPanel boardPanel) throws IOException {
        for(int j=cursorY-2;j<cursorY+3;j++){
            for(int i=cursorX-2;i<cursorX+3;i++){
                TileButton button;
                if(gamePosX==i-(cursorX-2) && gamePosY==j-(cursorY-2)){//draw current tile player is holding
                    button = new TileButton(game.getCurTile().getId());
                    button.setBorder(BorderFactory.createLineBorder(colors[game.getPlayerIndex()],5));
                    curTile = button;
                }
                else if(gameBoard[j][i]!=null){//draw current board
                    button = new TileButton(gameBoard[j][i].getId());
                    gameButtons[j][i] = button;
                }
                else{
                    button = gameButtons[j][i];
                }

                boardPanel.add(button);
            }
        }
    }

    private void updateBoard() { //updates tile locations
        boardPanel.removeAll();
        for(int j=cursorY-2;j<cursorY+3;j++) {
            for (int i = cursorX - 2; i < cursorX + 3; i++) {
                if(gamePosX==i-(cursorX-2) && gamePosY==j-(cursorY-2)){
                    assert(curTile!=null);
                    boardPanel.add(curTile);
                }
                else {
                    if(game.getGBoard().getBoard()[j][i]!=null&&
                            game.getGBoard().getBoard()[j][i].follower()==null){
                        gameButtons[j][i].resetIcon();
                    }
                    boardPanel.add(gameButtons[j][i]);

                }
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void setFollowerGUI(){
        JFrame frame = new JFrame("Place Follower!");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GridBagLayout buttonLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel panel = new JPanel(buttonLayout);

        //first row
        JPanel corner = new JPanel();
        constraints.weightx=1.0;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.gridx=0;
        constraints.gridy=0;
        panel.add(corner,constraints);

        constraints.gridx=1;
        JButton button0 = new JButton("    ");
        button0.addActionListener(e -> {
            if(game.placeFollower(0)){
                followerIndex=0;
                this.updateFollower(game);
                frame.dispose();
            }
            else{
                button0.setText("can't!");
            }
        });
        panel.add(button0,constraints);

        constraints.gridx=2;
        JButton button1 = new JButton("road");
        if(game.getCurTile().getRoadSides()[0]){
            button1.addActionListener(e -> {
                if(game.placeRoadFollower(0)){
                    followerIndex=8;
                    this.updateFollower(game);
                    frame.dispose();
                }
                else{
                    button1.setText("can't!");
                }
            });
            panel.add(button1,constraints);
        }
        else{
            JPanel panel1 = new JPanel();
            panel.add(panel1,constraints);
        }

        constraints.gridx=3;
        JButton button2 = new JButton("    ");
        button2.addActionListener(e -> {
            if(game.placeFollower(1)){
                followerIndex=1;
                this.updateFollower(game);
                frame.dispose();
            }
            else{
                button2.setText("can't!");
            }
        });
        panel.add(button2,constraints);
        constraints.gridx=4;
        JButton exit = new JButton("cancel");
        exit.addActionListener(e -> {
                followerIndex=-1;
                this.updateFollower(game);
                frame.dispose();
        });
        panel.add(exit,constraints);
        //2nd row
        constraints.gridx=0;
        constraints.gridy=1;
        constraints.ipady = 20;
        JButton button3 = new JButton("    ");
        button3.addActionListener(e -> {
            if(game.placeFollower(7)){
                followerIndex=7;
                this.updateFollower(game);
                frame.dispose();
            }
            else{
                button3.setText("can't!");
            }
        });
        panel.add(button3,constraints);

        constraints.gridx=1;
        constraints.gridy=1;
        constraints.gridwidth=3;
        constraints.gridheight=3;
        constraints.ipady=0;
        BufferedImage tilePic = curTile.getRotatedTileImage();
        JLabel image = new JLabel(new ImageIcon(tilePic));
        panel.add(image,constraints);

        constraints.ipady=20;
        constraints.gridx=4;
        constraints.gridy=1;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        JButton button4 = new JButton("    ");
        button4.addActionListener(e -> {
            if(game.placeFollower(2)){
                followerIndex=2;
                this.updateFollower(game);
                frame.dispose();
            }
            else{
                button4.setText("can't!");
            }
        });
        panel.add(button4,constraints);

        constraints.gridy=2;
        constraints.gridx=0;
        JButton button5 = new JButton("road");
        if(game.getCurTile().getRoadSides()[3]){
            button5.addActionListener(e -> {
                if(game.placeRoadFollower(3)){
                    followerIndex=11;
                    this.updateFollower(game);
                    frame.dispose();
                }
                else{
                    button5.setText("can't!");
                }
            });
            panel.add(button5,constraints);
        }
        else{
            JPanel panel2 = new JPanel();
            panel.add(panel2,constraints);
        }

        constraints.gridy=2;
        constraints.gridx=4;
        JButton button6 = new JButton("road");
        if(game.getCurTile().getRoadSides()[1]){
            button6.addActionListener(e -> {
                if(game.placeRoadFollower(1)){
                    followerIndex=9;
                    this.updateFollower(game);
                    frame.dispose();
                }
                else{
                    button6.setText("can't!");
                }
            });
            panel.add(button6,constraints);
        }
        else{
            JPanel panel2 = new JPanel();
            panel.add(panel2,constraints);
        }

        constraints.gridx=0;
        constraints.gridy=3;
        JButton button7 = new JButton("    ");
        button7.addActionListener(e -> {
            if(game.placeFollower(6)){
                followerIndex=6;
                this.updateFollower(game);
                frame.dispose();
            }
            else{
                button7.setText("can't!");
            }
        });
        panel.add(button7,constraints);

        constraints.gridx=4;
        constraints.gridy=3;
        JButton button8 = new JButton("    ");
        button8.addActionListener(e -> {
            if(game.placeFollower(3)){
                followerIndex=3;
                this.updateFollower(game);
                frame.dispose();
            }
            else{
                button8.setText("can't!");
            }
        });
        panel.add(button8,constraints);

        constraints.gridx=1;
        constraints.gridy=4;
        JButton button9 = new JButton("    ");
        button9.addActionListener(e -> {
            if(game.placeFollower(5)){
                followerIndex=5;
                this.updateFollower(game);
                frame.dispose();
            }
            else{
                button9.setText("can't!");
            }
        });
        panel.add(button9,constraints);

        constraints.gridx=3;
        constraints.gridy=4;
        JButton button10 = new JButton("    ");
        button10.addActionListener(e -> {
            if(game.placeFollower(4)){
                followerIndex=4;
                this.updateFollower(game);
                frame.dispose();
            }
            else{
                button10.setText("can't!");
            }
        });
        panel.add(button10,constraints);

        constraints.gridy=4;
        constraints.gridx=2;
        JButton button11 = new JButton("road");
        if(game.getCurTile().getRoadSides()[2]){
            button11.addActionListener(e -> {
                if(game.placeRoadFollower(2)){
                    followerIndex=10;
                    this.updateFollower(game);
                    frame.dispose();
                }
                else{
                    button11.setText("can't!");
                }
            });
            panel.add(button11,constraints);
        }
        else{
            JPanel panel3 = new JPanel();
            panel.add(panel3,constraints);
        }

        constraints.gridx=4;
        JButton button12 = new JButton("Cloister");
        if(game.getCurTile().getHasCloister()){
            button12.addActionListener(e -> {
                game.placeCloisterFollower();
                followerIndex=12;
                this.updateFollower(game);
                frame.dispose();
            });
            panel.add(button12,constraints);
        }

        frame.add(panel);
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);
    }

    private void updateFollower(GameSystem game){
        BufferedImage src = curTile.getRotatedTileImage();
        BufferedImage withFollower=src;
        if(followerIndex>=0){
            withFollower = withCircle(src, colors[game.getPlayerIndex()],15);
        }
        curTile.setTileImage(withFollower);

        //see if game over
        if(!game.next()){
            ArrayList<Player> winners = game.getWinner();

            JFrame ending = new JFrame();
            JPanel displayWinner = new JPanel();
            String winnerString="";
            for(Player p:winners){
                winnerString+=p.toString()+", ";
            }
            winnerString+=String.format("with %d points!",winners.get(0).getScore());
            displayWinner.add(new JLabel(winnerString));
            ending.add(displayWinner);
            ending.pack();
            ending.setResizable(true);
            ending.setVisible(true);
        }
        //update the board
        try {
            curTile = new TileButton(game.getCurTile().getId());
            curTile.setBorder(BorderFactory.createLineBorder(colors[game.getPlayerIndex()],5));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        updateBoard();
        //update scoreboard
        updateScores();

    }

    private BufferedImage withCircle(BufferedImage src, Color color, int radius) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

        Graphics2D g = (Graphics2D) dest.getGraphics();
        g.drawImage(src, 0, 0, null);
        g.setColor(color);
        if(followerIndex<8) {
            g.fillOval(followerCoords[followerIndex][0] - radius, followerCoords[followerIndex][1]
                    - radius, radius, radius);
        }
        else if(followerIndex<12){
            g.fillOval(roadFollowerCoords[followerIndex-8][0] - radius,
                    roadFollowerCoords[followerIndex-8][1] - radius, radius, radius);
        }
        else{
            g.fillOval(85 - radius,
                    85 - radius, radius, radius);
        }
        g.dispose();

        return dest;
    }

    private void updateScores(){
        Player[] players = game.getPlayers();
        scoreboard.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.ipady=50;
        constraints.ipadx=50;
        for(int i=0;i<scorePanels.length;i++){
            if(scorePanels[i]!=null) scorePanels[i].removeAll();
            scorePanels[i] = new JPanel();
            scorePanels[i].setLayout(new GridLayout(4,1));
            scorePanels[i].add(new JLabel(players[i].toString()));
            scorePanels[i].add(new JLabel(String.format("Score: %d",players[i].getScore())));
            scorePanels[i].add(new JLabel(String.format("Followers: %d",players[i].followersLeft())));
            scorePanels[i].add(new JSeparator());
            constraints.gridy=i;
            scorePanels[i].setBorder(BorderFactory.createLineBorder(colors[i], 5));

            scoreboard.add(scorePanels[i],constraints);

        }
    }
}
