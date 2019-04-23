package edu.cmu.cs.cs214.hw4.core;

/**
 * a wrapper class made for features so merging features is cleaner and easier
 * basically like a pointer, where the pointer can be changed to point to a diff feat
 */
public class FeatureWrap {
    private Feature feat;

    /**
     * constructor
     * @param newFeat the feature to be stored in the featurewrap
     */
    FeatureWrap(Feature newFeat){
        feat = newFeat;
    }

    /**
     * change the feat stored in the featurewrap
     * @param feat the new feat
     */
    public void setFeat(Feature feat) {
        this.feat = feat;
    }

    /**
     * getter method
     * @return the feat currently stored
     */
    public Feature getFeat() {
        return feat;
    }

    /**
     * merger method
     * @param other the featurewrap to be merged with
     * @return the merged featurewrap, containing the merged feature objects
     */
    public FeatureWrap merge(FeatureWrap other){
        return new FeatureWrap(feat.merge(other.feat));
    }
}
