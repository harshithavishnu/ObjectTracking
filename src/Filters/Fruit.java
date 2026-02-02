import processing.core.PApplet;
import processing.core.PImage;

public class Fruit {
    private int x = 800;
    private int y = 575;
    private int size;
    private int speed;
    private PImage img;

    public Fruit(int size, int speed, PImage img) {
        this.size = size;
        this.speed = speed;
        this.img = img;
    }

    public void update() {
        y += speed;
    }

    public void draw(PApplet window) {
        if (img != null) {
            window.image(img, x, y - size, size, size);
        } else {
            window.rect(x, y - size, size, size);
        }
    }

    public boolean isOffScreen() {
        return x + size < 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return size; }
    public int getSize() { return size; }
}
