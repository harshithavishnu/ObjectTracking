package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class ColorMaskFilter implements PixelFilter {
    short targetRed; short targetGreen; short targetBlue;
    int threshold;

    public ColorMaskFilter(){
        targetRed = 29;
        targetGreen = 203;
        targetBlue = 45;
        threshold = 50;
    }
    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {
                boolean close = closeColors(red[r][c], green[r][c], blue[r][c], targetRed, targetGreen, targetBlue);
                boolean closeR = closeRatios(red[r][c], green[r][c], blue[r][c], targetRed, targetGreen, targetBlue);
                if (close || closeR) {
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

        int[] center = findCenter(red);
        if (center != null) {
            drawCenterCircle(red, green, blue, center[0], center[1]);
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

    public boolean closeRatios(short r1, short g1, short b1, short r2, short g2, short b2){
        if (g1 == 0 || g2 == 0 || b1 == 0 || b2 == 0) {
            return false;
        }

        double rRatio1 = (double) r1 / (r1 + b1 +g1);
        double rRatio2 = (double) r2 / (r2 + b2 +g2);
        double gRatio1 = (double) g1 / (r1 + b1 +g1);
        double gRatio2 = (double) g2 / (r2 + b2 +g2);

        return (Math.abs(rRatio1 - rRatio2) < 0.3 && Math.abs(gRatio1 - gRatio2) < 0.3);
    }

    public int[] findCenter(short[][]red) {
        int sumX = 0;
        int sumY = 0;
        int count = 0;

        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {
                if (red[r][c] == 255) {
                    sumX += c;  // x = column
                    sumY += r;  // y = row
                    count++;
                }
            }
        }
        if (count < 120) return null;

        int centerX = sumX / count;
        int centerY = sumY / count;

        return new int[]{centerX, centerY};

    }

    public void drawCenterCircle(short[][] r, short[][] g, short[][] b, int cx, int cy){
        int rows = r.length;
        int cols = r[0].length;

        int r2 = 10 * 10;

        for (int y1 = -10; y1 <= 10; y1++) {
            for (int x1 = -10; x1 <= 10; x1++) {

                int x = cx + x1;
                int y = cy + y1;

                if (x < 0 || x >= cols || y < 0 || y >= rows) continue;

                int dist2 = x1 * x1 + y1 * y1;
                if (Math.abs(dist2 - r2) < 10) {
                    r[y][x] = 255;
                    g[y][x] = 0;
                    b[y][x] = 0;
                }
            }
        }
    }


}
