package com.github.moleskicoder.swarm;

import java.awt.event.KeyEvent;

interface ICraft extends Actor {

    Iterable<Sprite> getMissiles();
    void controlMissiles();

    void keyPressed(KeyEvent e);
    void keyReleased(KeyEvent e);
}
