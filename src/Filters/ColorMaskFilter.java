package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class ColorMaskFilter implements PixelFilter {
    short targetRed;
    short targetGreen;
    short targetBlue;
    int threshold;
    static int[] lastCenter;

    public ColorMaskFilter() {
        targetRed = 29;
        targetGreen = 203;
        targetBlue = 45;
        threshold = 50;
    }

    public static int[] getLastCenter() {
        return lastCenter;
    }


    @Override
    public DImage processImage(DImage img) {
        DImage star = new DImage("images/7.jpg");
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        short[][] red1 = img.getRedChannel();
        short[][] green1 = img.getGreenChannel();
        short[][] blue1 = img.getBlueChannel();

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

        lastCenter = findCenter(red);
        drawStar(red, red1, green1, blue1, star);
        img.setColorChannels(red1, green1, blue1);
        if (lastCenter != null) {
            drawStar(red, red1, green1, blue1, img);
        }
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

        int distance = dr * dr + dg * dg + db * db;
        return distance < threshold * threshold;
    }

    public boolean closeRatios(short r1, short g1, short b1, short r2, short g2, short b2) {
        if (g1 == 0 || g2 == 0 || b1 == 0 || b2 == 0) {
            return false;
        }
        double sum1 = r1 + b1 + g1;
        double sum2 = r2 + b2 + g2;
        double rRatio1 = (double) r1 / sum1;
        double rRatio2 = (double) r2 / sum2;
        double gRatio1 = (double) g1 / sum1;
        double gRatio2 = (double) g2 / sum2;

        return (Math.abs(rRatio1 - rRatio2) < 0.3 && Math.abs(gRatio1 - gRatio2) < 0.3);
    }

    public static int[] findCenter(short[][] red) {
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

//    public void drawCenterCircle(short[][] r, int cx, int cy, short[][] r1, short[][] g1, short[][] b1) {
//        int rows = r.length;
//        int cols = r[0].length;
//
//        int r2 = 10 * 10;
//
//        for (int y1 = -10; y1 <= 10; y1++) {
//            for (int x1 = -10; x1 <= 10; x1++) {
//
//                int x = cx + x1;
//                int y = cy + y1;
//
//                if (x < 0 || x >= cols || y < 0 || y >= rows) continue;
//
//                int dist2 = x1 * x1 + y1 * y1;
//                if (Math.abs(dist2 - r2) < 10) {
//                    r1[y][x] = 255;
//                    g1[y][x] = 0;
//                    b1[y][x] = 0;
//                }
//            }
//        }
//    }

    public void drawStar(short[][]red, short[][] r1, short[][] g1, short[][] b1, DImage image) {
        DImage image1 = Downsample(image);
        int height = image1.getHeight();
        int width = image1.getWidth();
        int[] center = findCenter(red);
        if (center == null) return;
        int startY = center[1] - (height/2);
        int startX = center[0] - (width/2);
        short[][] imgRed = image1.getRedChannel();
        short[][] imgGreen = image1.getGreenChannel();
        short[][] imgBlue = image1.getBlueChannel();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                r1[startY+r][startX+c] = imgRed[r][c];
                g1[startY+r][startX+c] = imgGreen[r][c];
                b1[startY+r][startX+c] = imgBlue[r][c];
            }

        }
    }

    public DImage Downsample(DImage img){
        short[][] grid = img.getBWPixelGrid();
        short[][] small = new short[(grid.length)/2][(grid[0].length)/2];

        for (int r = 0; r < small.length; r++) {
            for (int c = 0; c < small[r].length; c++) {
                small[r][c] = grid[r*2][c*2];
            }
        }
        DImage out = new DImage(small.length, small[0].length);
        out.setPixels(small);
        return out;
    }

    }

