package com.github.moleskicoder.swarm;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public final class Missile implements Sprite {

    private static final String MISSILE = "missile.png";

    private static final Image IMAGE;

    private static final int WIDTH;
    private static final int HEIGHT;

    private final int fieldWidth;
    private final int speed;
    private final int lifespan;

    private int x;
    private final int y;

    private int ticks = 0;

    private boolean visible;

    static {
        final ImageIcon icon = new ImageIcon(Missile.class.getResource(Game.RESOURCE_IMAGE_FOLDER + MISSILE));
        IMAGE = icon.getImage();
        WIDTH = IMAGE.getWidth(null);
        HEIGHT = IMAGE.getHeight(null);
    }

    public Missile(final int boardWidth, final int missileSpeed, final int missileX, final int missileY) {

        this.fieldWidth = boardWidth;
        this.speed = missileSpeed;

        this.lifespan = this.fieldWidth / (2 * this.speed);

        this.x = missileX;
        this.y = missileY;

        this.visible = true;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void move() {

        ++this.ticks;

        this.x += this.speed;

        // Over the edge of the screen, or dying of old age...
        if (this.x > this.fieldWidth || this.ticks > this.lifespan) {
            this.destroy();
        }
    }

    @Override
    public void draw(final Graphics graphics, final ImageObserver observer) {

        if (graphics == null) {
            throw new IllegalArgumentException("graphics");
        }

        if (!this.isVisible()) {
            throw new IllegalStateException("Missile is invisible!");
        }

        graphics.drawImage(getImage(), this.getX(), this.getY(), observer);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, WIDTH, HEIGHT);
    }

    @Override
    public void destroy() {
        this.setVisible(false);
    }

    private static Image getImage() {
        return IMAGE;
    }

    private void setVisible(final boolean value) {
        this.visible = value;
    }

    private int getX() {
        return this.x;
    }

    private int getY() {
        return this.y;
    }
}