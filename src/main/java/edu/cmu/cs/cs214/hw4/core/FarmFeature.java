package edu.cmu.cs.cs214.hw4.core;

import java.util.TreeSet;

import static edu.cmu.cs.cs214.hw4.core.FeatureType.City;
import static edu.cmu.cs.cs214.hw4.core.FeatureType.Farm;

/**
 * class for farm features
 */
public class FarmFeature extends Feature {
    /**
     * constructor
     * @param tile the tile it first belonged on
     */
    FarmFeature(Tile tile) {
        super(tile);
        setType(Farm);
    }

    /**
     * calculates the score of a farm in game
     * @param board the current game board state
     * @return 0, since farms can't be scored in game
     */
    public int score(Tile[][] board){
        return 0;//farms shouldnt be scored during game
    }

    /**
     * calculates the score of farms after the game ends
     * @return how much was scored
     */
    public int scoreIncomplete(){
        TreeSet<Feature> seenFeats = new TreeSet<>();
        int score=0;
        if(isHasFollower() && !isScored()) {
            for (Tile t : getTiles()) {
                if(t.noRoads()) {
                    for (FeatureWrap feat : t.getFeatures()) {
                        if (feat.getFeat().getType() == City && feat.getFeat().isScored() && !seenFeats.contains(feat.getFeat())) {
                            score += 3;
                            seenFeats.add(feat.getFeat());
                        }
                    }
                }
                else{
                    for(int i=0;i< t.getFeatures().length;i++){
                        FeatureWrap feat = t.getFeatures()[i];
                        if(feat.getFeat().equals(this)) {
                            Feature nextFeat = t.getFeatures()[(i+1)%8].getFeat();
                            if (nextFeat.getType() == City && nextFeat.isScored() && !seenFeats.contains(nextFeat)) {
                                score += 3;
                                seenFeats.add(nextFeat);
                            }
                        }


                    }
                }
            }

            for (Player p : owners(Farm)) {
                p.addScore(score);
            }
        }
        scored();
        return score;
    }
}
