package Filters;

import Interfaces.Drawable;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;


public class DrawingFilter implements PixelFilter, Drawable {
    private final short targetRed = 29;
    private final short targetGreen = 203;
    private final short targetBlue = 45;
    private final int threshold = 50;
    static int[] ballCenter;
    private ArrayList<Fruit> fruits = new ArrayList<>();
    private ArrayList<int[]> trail = new ArrayList<>();
    private PImage watermelon;
    private PImage ninjaStar;
    private int frameCount = 0;
    private int counter = 0;
    private boolean gameStarted = false;



    @Override
    public void setup(PApplet window) {
        watermelon = window.loadImage("images/Watermelon.png");
        ninjaStar = window.loadImage("images/Ninjastar.png");
    }


    public static int[] getLastCenter() {
        return ballCenter;
    }

    private void drawStartScreen(PApplet window) {
        window.fill(0, 150);
        window.rect(0, 0, window.width, window.height);

        window.fill(255);
        window.textAlign(PApplet.CENTER);
        window.textSize(40);
        window.text("Fruit Ninja Camera", (float) window.width / 2, (float) window.height / 2 - 40);

        int btnX = window.width / 2 - 100;
        int btnY = window.height / 2;
        int btnW = 200;
        int btnH = 50;

        window.fill(50, 200, 50);
        window.rect(btnX, btnY, btnW, btnH, 10); // rounded corners

        window.fill(255);
        window.textSize(20);
        window.text("START", window.width / 2f, btnY + btnH / 2f + 7);
    }



    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {
                boolean close = closeColors(red[r][c], green[r][c], blue[r][c]);
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

        ballCenter = findCenter(red);
        img.setColorChannels(red, green, blue);
        return img;
    }

    public boolean closeColors(short r1, short g1, short b1) {

        int dr = r1 - targetRed;
        int dg = g1 - targetGreen;
        int db = b1 - targetBlue;

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
                    sumX += c;
                    sumY += r;
                    count++;
                }
            }
        }
        if (count < 120) return null;

        int centerX = sumX / count;
        int centerY = sumY / count;

        return new int[]{centerX, centerY};

    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        if (!gameStarted){
            drawStartScreen(window);
            mousePressed(window);
            return;
        }
        else {
            spawnFruit(window);
            updateTrail();
            drawTrail(window);
            updateAndDrawFruits(window);
            drawNinjaStar(window);
            drawCounter(window);
        }
    }

    private void drawCounter(PApplet window) {
        window.textSize(20);
        window.fill(0);
        window.text("Current Streak: " + counter, 431, 436);
        window.fill(255);
    }

    private void spawnFruit(PApplet window) {
        frameCount++;
        if (frameCount%60 == 0) {
            fruits.add(new Fruit(window, 70, 5, watermelon));
        }
    }

    private void updateTrail() {
        if (ballCenter != null) {
            trail.add(new int[]{ballCenter[0], ballCenter[1]});
            if (trail.size() > 15) trail.remove(0);
        }
    }

    private void drawTrail(PApplet window) {
        window.stroke(255, 0, 0);
        window.strokeWeight(3);

        for (int i = 1; i < trail.size(); i++) {
            int[] a = trail.get(i - 1);
            int[] b = trail.get(i);
            window.line(a[0], a[1], b[0], b[1]);
        }
    }

    private void updateAndDrawFruits(PApplet window) {
        for (int i = fruits.size() - 1; i >= 0; i--) {
            Fruit f = fruits.get(i);
            f.update();
            f.draw(window);

            if (checkCollision(f)) {
                fruits.remove(i);
                continue;
            }

            if (f.isOffScreen(window)) {
                fruits.remove(i);
            }
        }
    }

    private boolean checkCollision(Fruit f) {
        if (ballCenter == null) return false;

        float dx = ballCenter[0] - f.centerX();
        float dy = ballCenter[1] - f.centerY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if(dist < f.getRadius()){
            counter++;
            return true;
        }
        return false;
    }


    private void drawNinjaStar(PApplet window) {
        if (ballCenter == null) return;

        window.imageMode(PApplet.CENTER);
        window.image(ninjaStar, ballCenter[0], ballCenter[1], 60, 60);
        window.imageMode(PApplet.CORNER);
    }

    public void mousePressed(PApplet window) {
        int btnX = window.width / 2 - 100;
        int btnY = window.height / 2;
        int btnW = 200;
        int btnH = 50;

        if (window.mouseX >= btnX && window.mouseX <= btnX + btnW &&
                window.mouseY >= btnY && window.mouseY <= btnY + btnH) {
            gameStarted = true;
        }
    }
}



