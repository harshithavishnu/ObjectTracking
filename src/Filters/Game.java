package Filters;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Game extends PApplet {

    ArrayList<Fruit> fruits;
    PImage watermelonImg;

    public void settings() {
        size(800, 600);
    }

    public void setup() {
        fruits = new ArrayList<>();
        watermelonImg = loadImage("Watermelon.png");
    }

    public void draw() {
        //background(0);

        if (random(1) < 0.03) {
            fruits.add(new Fruit(this, 50, 5, watermelonImg));
        }

        for (int i = fruits.size() - 1; i >= 0; i--) {
            Fruit f = fruits.get(i);
            f.update();
            f.draw(this);

            if (f.isOffScreen(this)) {
                fruits.remove(i);
            }
        }
    }

    public void updateBlade(int x, int y) {
        for (int i = fruits.size() - 1; i >= 0; i--) {
            Fruit f = fruits.get(i);

            float dx = x - f.getX();
            float dy = y - f.getY();
            float dist = sqrt(dx * dx + dy * dy);

            if (dist < (float) f.getSize() / 2) {
                fruits.remove(i);
            }
        }
    }

}

