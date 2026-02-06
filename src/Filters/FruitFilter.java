package Filters;

import Interfaces.PixelFilter;
import Interfaces.Drawable;
import core.DImage;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class FruitFilter implements PixelFilter, Drawable {

    private ArrayList<Fruit> fruits = new ArrayList<>();
    private PImage fruitImg;

    public FruitFilter() {

    }

    @Override
    public DImage processImage(DImage img) {
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {

        if (fruitImg == null) {
            fruitImg = window.loadImage("Watermelon.png");
        }

        if (Math.random() < 0.03) {
            fruits.add(new Fruit(window, 60, 4, fruitImg));
        }

        for (int i = fruits.size() - 1; i >= 0; i--) {
            Fruit f = fruits.get(i);
            f.update();
            f.draw(window);

            if (f.isOffScreen(window)) {
                fruits.remove(i);
            }
        }
    }
}
