package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import static edu.cmu.cs.cs214.hw4.core.FeatureType.City;

/**
 * feature class for cities
 */
public class CityFeature extends Feature {
    /**
     * constructor for city feature
     * @param tile the tile it first belongs on
     */
    CityFeature(Tile tile){
        super(tile);
        setType(City);
    }

    /**
     * calculates the score of the city once it's scored
     * @param board the current game board state
     * @return how much was scored
     */
    public int score(Tile[][] board){
        if(complete(board)){
            System.out.println("complete city");
            if(!isScored()){
                scored();
                int score = 0;
                if(isHasFollower()){
                    for(Tile t:getTiles()){
                        score+=2;
                        if(t.getHasPennant()) score+=2;
                        if(t.follower()!=null&&t.follower().getFeat().equals(this)){
                            t.follower().owner.returnFollower(1);
                            System.out.println("follower returned to "+t.follower().owner);
                            t.returnFollower();

                        }

                    }
                    for(Player p:owners(City)){
                        p.addScore(score);
                    }
                }
                return score;
            }
        }
        return 0;
    }

    /**
     * scores incomplete cities at end of game
     * @return
     */
    public int scoreIncomplete(){
        if(!isScored() && isHasFollower()){
            int score=0;
            for(Tile t:getTiles()){
                score++;
                if(t.getHasPennant()) score++;
            }

            for(Player p:owners(City)) p.addScore(score);
            return score;
        }
        return 0;
    }

    private boolean complete(Tile[][] board){
        for(Tile t:getTiles()){
            boolean[] wallSides = t.getWallSides();
            FeatureWrap[] feats = t.getFeatures();
            int[] pos = t.getPos();
            if(feats[0].getFeat().equals(this)&&feats[1].getFeat().equals(this)){
                if(board[pos[0]-1][pos[1]]==null) return false;
                else if(!board[pos[0]-1][pos[1]].getWallSides()[2]){
                return false;
                }
            }
            if(feats[4].getFeat().equals(this)&&feats[5].getFeat().equals(this)){
                if(board[pos[0]+1][pos[1]]==null) return false;
                else if(!board[pos[0]+1][pos[1]].getWallSides()[0]){
                    return false;
                }
            }
            if(feats[2].getFeat().equals(this)&&feats[3].getFeat().equals(this)){
                if(board[pos[0]][pos[1]+1]==null) return false;

                else if(!board[pos[0]][pos[1]+1].getWallSides()[3]) {
                    return false;
                }
            }
            if(feats[6].getFeat().equals(this)&&feats[7].getFeat().equals(this)){
                if(board[pos[0]][pos[1]-1]==null) return false;
                else if(!board[pos[0]][pos[1]-1].getWallSides()[1]) {
                    return false;
                }
            }
        }
        return true;
    }
}
