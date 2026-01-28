package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class ColorMaskFilter implements PixelFilter {
    short targetRed; short targetGreen; short targetBlue;
    int threshold;

    public ColorMaskFilter(){
        targetRed = 31;
        targetGreen = 139;
        targetBlue = 14;
        threshold = 80;
    }
    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {
                boolean close = closeColors(red[r][c], green[r][c], blue[r][c], targetRed, targetGreen, targetBlue);
                if (close) {
                    red[r][c] = 255;
                    green[r][c] = 255;
                    blue[r][c] = 255;
                } else {
                    red[r][c] = 0;
                    green[r][c] = 0;
                    blue[r][c] = 0;
                }
            }
        }
        img.setColorChannels(red, green, blue);
        return img;
    }

//    public void keyPressed(char key) {
//        if (key == '+') threshold+= 10;
//        if (key == '-') threshold-= 10;
//        threshold = Math.max(threshold, 0);
//        threshold = Math.min(threshold, 255);
//    }

    public boolean closeColors(short r1, short g1, short b1, short r2, short g2, short b2) {

        int dr = r1 - r2;
        int dg = g1 - g2;
        int db = b1 - b2;

        int distance = dr*dr + dg*dg + db*db;
        return distance < threshold * threshold;
    }



}
