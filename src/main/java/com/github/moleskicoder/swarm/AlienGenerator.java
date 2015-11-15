package com.github.moleskicoder.swarm;

import java.security.SecureRandom;

final class AlienGenerator implements IAlienGenerator {

    private final int width;
    private final int height;
    private final int maximumSpeed;
    private final SecureRandom generator = new SecureRandom();

    AlienGenerator(final int boardWidth, final int boardHeight, final int maximumAlienSpeed) {
        this.width = boardWidth - 20;
        this.height = boardHeight - 20;
        this.maximumSpeed = maximumAlienSpeed;
    }

    @Override
    public Actor generate() {
        final int x = this.generator.nextInt(this.width) + this.width;
        final int y = this.generator.nextInt(this.height);
        return this.generate(x,y);
    }

    private Actor generate(final int x, final int y) {
        final int speed = this.generator.nextInt(this.maximumSpeed) + 1;
        return new Alien(this.width, this.height, speed, x, y);
    }
}
