package edu.cmu.cs.cs214.hw4.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import org.yaml.snakeyaml.Yaml;

public class TilePile {
    private String name;
    public void setName(String newName){name=newName;}
    public String getName(){return name;}
    private TileBean[] pile;

    public TileBean[] getPile(){return pile;}

    public void setPile(TileBean[] tiles){pile = tiles;}

    private static TilePile parse(String fileName) {
        Yaml yaml = new Yaml();
        try (InputStream is = new FileInputStream(fileName)) {
            return yaml.loadAs(is, TilePile.class);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File " + fileName + " not found!");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when reading " + fileName + "!");
        }
    }

    public static Stack<Tile> initTiles(){
        TilePile result = parse("src\\main\\resources\\tileConfig.yml");
        //index 7 is starting tile
        ArrayList<Tile> converted = new ArrayList<>();
        for(int i=0;i<result.pile.length;i++){
            if(i==7) continue; //7 should be first card
            converted.add(new Tile(result.pile[i]));
        }
        //shuffle deck
        int index;
        Stack<Tile> tileStack = new Stack<>();
        while(converted.size()>0){
            index = (int)(Math.random()*converted.size());
            tileStack.push(converted.remove(index));
        }
        tileStack.push(new Tile(result.pile[7])); //put first tile on top of deck
        return tileStack;
    }

    public static ArrayList<Tile> makeTileArray(){
        TilePile result = parse("src/main/resources/tileConfig.yml");
        //index 7 is starting tile
        ArrayList<Tile> converted = new ArrayList<>();
        for(int i=0;i<result.pile.length;i++){
            converted.add(new Tile(result.pile[i]));
        }
        return converted;
    }
}
