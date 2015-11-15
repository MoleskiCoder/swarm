package com.github.moleskicoder.swarm;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.Rectangle;
import java.security.SecureRandom;

import javax.swing.ImageIcon;

public final class Alien implements Actor {

    private static final String[] ALIENS = { "alien1.png", "alien2.png" };
    private static final Image[] IMAGES = new Image[ALIENS.length];

    private static final int WIDTH;
    private static final int HEIGHT;

    private static final SecureRandom NUMBER_GENERATOR = new SecureRandom();

    private final int fieldWidth;
    private final int fieldHeight;
    private final int speed;

    private int ticks = 0;
    private int selectedImage = 0;

    private int x;
    private int y;

    private boolean visible;

    static  {

        if (ALIENS.length < 1) {
            throw new IllegalStateException("Not enough alien images!");
        }

        final int numberOfAliens = ALIENS.length;
        for (int i = 0; i < numberOfAliens; ++i) {
            final ImageIcon icon = new ImageIcon(Alien.class.getResource(Game.RESOURCE_IMAGE_FOLDER + ALIENS[i]));
            IMAGES[i] = icon.getImage();
        }

        WIDTH = IMAGES[0].getWidth(null);
        HEIGHT = IMAGES[0].getHeight(null);
    }

    public Alien(final int boardWidth, final int boardHeight, final int alienSpeed, final int alienX, final int alienY) {

        this.x = alienX;
        this.y = alienY;

        this.visible = true;

        this.fieldWidth = boardWidth;
        this.fieldHeight = boardHeight;

        this.speed = alienSpeed;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, WIDTH, HEIGHT);
    }

    @Override
    public void move() {

        if (this.x < 0) {
            this.x = this.fieldWidth;
        }

        if (this.y < 0) {
            this.y = this.fieldHeight;
        }

        this.x -= this.speed;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void draw(final Graphics graphics, final ImageObserver observer) {

        if (graphics == null) {
            throw new IllegalArgumentException("graphics");
        }

        if (!this.isVisible()) {
            throw new IllegalStateException("Alien is invisible!");
        }

        ++this.ticks;

        final int twinkle = NUMBER_GENERATOR.nextInt(20) + 10;
        if (this.ticks % twinkle == 0) {
            ++this.selectedImage;
            this.selectedImage %= ALIENS.length;
        }

        graphics.drawImage(IMAGES[this.selectedImage], this.getX(), this.getY(), observer);
    }

    @Override
    public boolean checkCollision(final Rectangle targetArea) {
        if (targetArea == null) {
            throw new IllegalArgumentException("targetArea");
        }
        return targetArea.intersects(this.getBounds());
    }

    @Override
    public void destroy() {
        this.setVisible(false);
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