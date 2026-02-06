package Filters;

import core.DImage;
import processing.core.PApplet;
import processing.core.PImage;

public class Fruit {
    private int x;
    private int y;
    private int size;
    private int speed;
    private DImage img;

    public Fruit(PApplet window, int size, int speed, DImage img) {
        this.size = size;
        this.speed = speed;
        this.img = img;
        this.x = (int) window.random(0, window.width - size);
        this.y = -size;
    }

    public void update() {
        y += speed;
    }

    public void draw(PApplet window) {
        if (img != null) {
            window.image(img, x, y, size, size);
        } else {
            window.rect(x, y, size, size);
        }
    }

    public boolean isOffScreen(PApplet window) {
        return y > window.height;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
}
