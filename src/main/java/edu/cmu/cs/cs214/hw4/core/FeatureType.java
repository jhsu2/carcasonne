package edu.cmu.cs.cs214.hw4.core;

/**
 * enum for feature types
 */
public enum FeatureType {
    City{
        @Override
        public String toString() {
            return "city";
        }
    },
    Farm{
        @Override
        public String toString() {
            return "farm";
        }
    },
    Cloister{
        @Override
        public String toString() {
            return "cloister";
        }
    },Road{
        @Override
        public String toString() {
            return "road";
        }
    }
}
