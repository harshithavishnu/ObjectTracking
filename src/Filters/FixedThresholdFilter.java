package Filters;

import Interfaces.Drawable;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PApplet;

public class FixedThresholdFilter implements PixelFilter, Drawable {
    private int threshold;

    public FixedThresholdFilter() {
        threshold = 127;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] > threshold) {
                    grid[r][c] = 255;
                } else {
                    grid[r][c] = 0;
                }
            }
        }

        img.setPixels(grid);
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        window.fill(window.color(255, 0, 0));
        window.ellipse(0, 0, 30, 30);
    }
}

