package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;

import static edu.cmu.cs.cs214.hw4.core.FeatureType.Road;

/**
 * class for road features
 */
public class RoadFeature extends Feature {
    /**
     * constructor
     * @param tile the tile it first belonged on
     */
    RoadFeature(Tile tile) {
        super(tile);
        setType(Road);
    }

    /**
     * scored a road after its completed
     * @param board the current game board state
     * @return how many pts were scored
     */
    public int score(Tile[][] board){
        if(!isScored() && complete(board)) {
            System.out.println("road complete");
            if (isHasFollower()) {
                ArrayList<Player> owners = owners(Road);
                int result = 0;
                for (Tile t : getTiles()) {
                    result++;
                    System.out.println("this: "+this);
                    if (t.follower() != null && t.follower().getFeat().equals(this)) {
                        System.out.println(t.follower().getFeat()==this);
                        t.follower().owner.returnFollower(1);
                        System.out.println("follower returned to "+t.follower().owner);
                        t.returnFollower();
                    }
                }
                for(Player p:owners) p.addScore(result);
                return result;
            }
        }
        return 0;
    }

    /**
     * scores roads after game ends
     * @return how much was scored
     */
    public int scoreIncomplete(){
        if(!isScored()) {
            if (isHasFollower()) {
                ArrayList<Player> owners = owners(Road);
                int result = 0;
                for (Tile t : getTiles()) {
                    result++;
                }
                for(Player p:owners) p.addScore(result);
                return result;
            }
            scored();
        }
        return 0;
    }

    private boolean complete(Tile[][] board){
        int intersectionCount = 2;
        for(Tile t:getTiles()){
            if(t.getHasIntersect()){
                intersectionCount--;}
        }
        return intersectionCount==0;
    }
}
