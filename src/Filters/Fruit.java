package Filters;

import processing.core.PApplet;
import processing.core.PImage;

public class Fruit {
    private int x;
    private int y;
    private int size;
    private int speed;
    private PImage img;

    public Fruit(PApplet window, int size, int speed, PImage img) {
        this.size = size;
        this.speed = speed;
        this.img = img;
        this.x = (int) window.random(20, window.width - size - 20);
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
        return y > window.height + size;
    }

    public int centerX() {
        return x+size/2;
    }

    public int centerY() {
        return y+size/2;
    }

    public int getSize() {
        return size/2;
    }

    public float getRadius(){
        return size / 2f;
    }
}
