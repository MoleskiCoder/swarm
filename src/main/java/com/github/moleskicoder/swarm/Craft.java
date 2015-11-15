package com.github.moleskicoder.swarm;

import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

final class Craft implements ICraft {

    private static final String CRAFT = "craft.png";

    private static final Image IMAGE;

    private static final int WIDTH;
    private static final int HEIGHT;

    private final IMissileGenerator missileGenerator;
    private final List<Sprite> missiles;
    private final int maximumMissiles;

    private final int fieldWidth;
    private final int fieldHeight;

    private int dx = 0;
    private int dy = 0;

    private int x;
    private int y;

    private boolean visible;

    static {
        final ImageIcon icon = new ImageIcon(Craft.class.getResource(Game.RESOURCE_IMAGE_FOLDER + CRAFT));
        IMAGE = icon.getImage();
        WIDTH = IMAGE.getWidth(null);
        HEIGHT = IMAGE.getHeight(null);
    }

    Craft(final int boardWidth, final int boardHeight, final int missileSpeed, final int missileLimit) {

        this.fieldWidth = boardWidth - WIDTH;
        this.fieldHeight = boardHeight - (HEIGHT << 1);

        this.missileGenerator = new MissileGenerator(boardWidth, missileSpeed);
        this.missiles = new ArrayList<>();
        this.maximumMissiles = missileLimit;

        this.x = this.fieldWidth / 10;
        this.y = this.fieldHeight / 2;

        this.visible = true;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, WIDTH, HEIGHT);
    }

    @Override
    public void move() {

        this.x += this.dx;
        this.y += this.dy;

        if (this.x < 1) {
            this.x = 1;
        }

        if (this.x > this.fieldWidth) {
            this.x = this.fieldWidth;
        }

        if (this.y < 1) {
            this.y = 1;
        }

        if (this.y > this.fieldHeight) {
            this.y = this.fieldHeight;
        }
    }

    @Override
    public void draw(final Graphics graphics, final ImageObserver observer) {

        if (graphics == null) {
            throw new IllegalArgumentException("graphics");
        }

        if (!this.isVisible()) {
            throw new IllegalStateException("com.github.moleskicoder.swarm.Craft is invisible!");
        }

        graphics.drawImage(getImage(), this.getX(), this.getY(), observer);
    }

    @Override
    public Iterable<Sprite> getMissiles() {
        return this.missiles;
    }

    @Override
    public void destroy() {
        this.setVisible(false);
    }

    @Override
    public void keyPressed(final KeyEvent e) {

        final int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            this.fire();
        }

        switch (key) {

        case KeyEvent.VK_LEFT:
            this.dx = -1;
            break;

        case KeyEvent.VK_RIGHT:
            this.dx = 1;
            break;

        case KeyEvent.VK_UP:
            this.dy = -1;
            break;

        case KeyEvent.VK_DOWN:
            this.dy = 1;
            break;
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {

        switch (e.getKeyCode()) {

        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:
            this.dx = 0;
            break;

        case KeyEvent.VK_UP:
        case KeyEvent.VK_DOWN:
            this.dy = 0;
            break;
        }
    }

    @Override
    public void controlMissiles() {
        int numberOfMissiles = this.missiles.size();
        for (int i = 0; i < numberOfMissiles; ++i) {
            final Sprite missile = this.missiles.get(i);
            if (missile.isVisible()) {
                missile.move();
            } else {
                this.missiles.remove(i);
                --numberOfMissiles;
            }
        }
    }

    @Override
    public boolean checkCollision(final Rectangle targetArea) {
        return targetArea.intersects(this.getBounds());
    }

    private static Image getImage() {
        return IMAGE;
    }

    private void fire() {
        if (this.missiles.size() < this.maximumMissiles) {
            final int missileX = this.x + WIDTH;
            final int missileY = this.y + HEIGHT / 2;
            final Sprite missile = this.missileGenerator.generate(missileX, missileY);
            this.missiles.add(missile);
        }
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