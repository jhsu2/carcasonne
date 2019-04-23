package edu.cmu.cs.cs214.hw4.core;

import static edu.cmu.cs.cs214.hw4.core.FeatureType.Cloister;

/**
 * feature class for cloisters
 */
public class CloisterFeature extends Feature {
    Tile centerTile;
    /**
     * constructor for cloisterfeature
     * @param tile the tile it first belonged on
     */
    public CloisterFeature(Tile tile) {
        super(tile);
        centerTile = tile;
        setType(Cloister);
    }

    /**
     * calculates the score of a cloister
     * @param board the current game board state
     * @return how much was scored
     */
    public int score(Tile[][] board){
        System.out.println(complete(board));
        if(complete(board)) {
            scored();
            if(isHasFollower()) {
                centerTile.follower().owner.addScore(9);
                centerTile.follower().owner.returnFollower(1);
                return 9;
            }
        }
        return 0;
    }

    /**
     * calculates score of incomplete cloister
     * @return how much was scored
     */
    public int scoreIncomplete(){
        if(!isScored()) {
            int score = getTiles().size();
            for (Player p : owners(Cloister)) p.addScore(score);
            return score;
        }
        return 0;
    }

    private boolean complete(Tile[][] board){
        return getTiles().size()==9;
    }
}
