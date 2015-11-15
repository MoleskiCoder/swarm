package com.github.moleskicoder.swarm;

import java.awt.Rectangle;

public interface Actor extends Sprite {
    boolean checkCollision(Rectangle targetArea);
}
