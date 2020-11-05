package net.projectdf.GUI.Sprites;

public class Sprite {

    private int xPos;
    private int yPos;
    private int width;
    private int height;

    public Sprite(int xPos, int yPos, int width, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }
}
