package edu.cmu.cs.cs214.hw4.core;

public class Follower {
    final Player owner;
    private FeatureWrap feat;
    public Follower(Player newOwner,FeatureWrap feat){
        owner = newOwner;
        this.feat = feat;
    }

//    public void changeFeature(Feature feat){
//        feat = this.feat;
//    }
    public Feature getFeat(){ return feat.getFeat();}

    public FeatureType getType(){
        return feat.getFeat().getType();
    }
}
