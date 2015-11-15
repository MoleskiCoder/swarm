package com.github.moleskicoder.swarm;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public interface Sprite {
    Rectangle getBounds();
    void move();
    boolean isVisible();
    void destroy();
    void draw(final Graphics graphics, final ImageObserver observer);
}
