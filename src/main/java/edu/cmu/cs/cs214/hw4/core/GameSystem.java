package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Stack;

import org.yaml.snakeyaml.Yaml;

public class GameSystem {
    private Player[] players;
    private Stack<Tile> tileDeck;
    private GameBoard gBoard;

    private int playerIndex;
    private Player curPlayer;
    private Tile curTile;

    public GameSystem(String... newPlayers){
        gBoard = new GameBoard();
        tileDeck = TilePile.initTiles();
        curTile = tileDeck.pop();
        gBoard.placeTile(curTile,35,35);

        players = new Player[newPlayers.length];
        for(int i=0;i<newPlayers.length;i++){
            players[i] = new Player(newPlayers[i]);
        }
        playerIndex=0;
        curPlayer = players[playerIndex];
    }

    /**
     * draws a new tile
     */
    public void drawTile(){
        curTile = tileDeck.pop();
    }

    /**
     * rotate method for the current tile
     */
    public void rotate(){
        curTile.rotate();
    }

    /**
     * method for placing the current tile onto the board
     * @param x x coord
     * @param y y coord
     * @return if the tile is succesfully placed onto board
     */
    public boolean placeTile(int x, int y){
        if(gBoard.placeTile(curTile,x,y)){
            //place follower
            return true;
        }
        return false;
    }

    /**
     * places follower on current tile
     * @param index the index that references which feature to put the follower on
     * @return if follower was successfully placed
     */
    public boolean placeFollower(int index){
        return curTile.placeFollower(index,curPlayer);
    }

    public boolean placeRoadFollower(int index){
        return curTile.placeRoadFollower(index,curPlayer);
    }

    public boolean placeCloisterFollower(){
        return curTile.placeCloisterFollower(curPlayer);
    }

    /**
     * getter methods
     * @return what it is getting
     */
    public GameBoard getGBoard() {
        return gBoard;
    }

    public Tile getCurTile(){return curTile;}

    public boolean next(){
        curTile.score(gBoard.getBoard());
        if(tileDeck.empty()){
            gBoard.scoreIncomplete();
            return false;
        }
        drawTile();
        playerIndex = (playerIndex+1)%players.length;
        curPlayer = players[playerIndex];
        return true;
    }

    public int getPlayerIndex(){
        return playerIndex;
    }

    public Player[] getPlayers(){
        return players;
    }

    public ArrayList<Player> getWinner(){
        int maxScore=0;
        ArrayList<Player> winners = new ArrayList<>();
        for(Player p:players){
            if(p.getScore()>maxScore) maxScore = p.getScore();
        }
        for(Player p:players){
            if(p.getScore()==maxScore) winners.add(p);
        }
        return winners;
    }
}
