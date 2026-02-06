package Filters;

import Interfaces.Drawable;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PApplet;

import java.util.ArrayList;

/***
 * A basic example showing how to draw on top the display window
 */
public class DrawingFilter implements PixelFilter, Drawable {
    private int threshold;
    private ArrayList<Fruit> fruits;

    @Override
    public DImage processImage(DImage img) {
        // We don't change the input image at all!
        // This could be your filter code if you want
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        draw(window);
//        // load red fill color
//        window.fill(255, 0, 0);
//        // draw a diameter 10 circle centered at the lower right corner of the screen
//        window.ellipse(original.getWidth(), original.getHeight(), 10, 10);
//
//        // load green color
//        window.fill(0, 255, 0);
//        // draw a dimeter 10 circle centered at upper left corner of screen
//        window.ellipse(0, 0, 10, 10);
    }

    public DrawingFilter(ArrayList<Fruit> fruits) {
        this.fruits = fruits;
    }

    @Override
    public void setup(PApplet window) {
        // this method is run once after your object is constructed.
        DImage watermelon = new DImage("images/Watermelon.png");
        Fruit f = new Fruit(window, 30, 30, watermelon);
        fruits.add(f);
        // use it to load images from disk and save them as attributes.
    }

    public void draw(PApplet window){
        for (int i = 0; i < fruits.size(); i++) {
            Fruit f = fruits.get(i);
            f.draw(window);
        }
    }
}