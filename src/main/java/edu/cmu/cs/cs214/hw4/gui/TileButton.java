package edu.cmu.cs.cs214.hw4.gui;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * class to represent tiles in the gui
 */
public class TileButton extends JButton {
    private BufferedImage tileImage;
    private BufferedImage rotatedTileImage;
    private ImageIcon icon;
    private String id;
    private int rotateCounter;

    /**
     * constructor for tiles in the gui
     * @param id the tile type, as a single letter
     * @throws IOException
     */
    public TileButton(String id) throws IOException {
        this.id = id;
        setPreferredSize(new Dimension(150,150));
        if(id!=null){
            tileImage = ImageIO.read(new File(String.format("src\\main\\resources\\%s.PNG",
                    id)));
            rotatedTileImage = ImageIO.read(new File(String.format("src\\main\\resources\\%s.PNG",
                    id)));
            icon = new ImageIcon(rotatedTileImage);
            setIcon(icon);
        }
        rotateCounter=0;
    }

    public Icon getIcon(){
        return icon;
    }

    public BufferedImage getRotatedTileImage() {
        return rotatedTileImage;
    }

    public void setTileImage(BufferedImage newImage){
        rotatedTileImage = newImage;
        icon = new ImageIcon(rotatedTileImage);
    }

    /**
     * rotates the image of the tile
     */
    public void rotate(){
        rotateCounter=(rotateCounter+1)%4;
        rotatedTileImage = rotateClockwise(tileImage,rotateCounter);
        icon = new ImageIcon(rotatedTileImage);
        setIcon(icon);
    }

    public void resetIcon(){
        rotatedTileImage = rotateClockwise(tileImage,rotateCounter);
        icon = new ImageIcon(rotatedTileImage);
    }

    private static BufferedImage rotateClockwise(BufferedImage src, int n) {
        int w = src.getWidth();
        int h = src.getHeight();

        AffineTransform at = AffineTransform.getQuadrantRotateInstance(n, w / 2.0, h / 2.0);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage dest = new BufferedImage(w, h, src.getType());
        op.filter(src, dest);
        return dest;
    }
}
