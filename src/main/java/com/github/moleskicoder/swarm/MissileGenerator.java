package com.github.moleskicoder.swarm;

final class MissileGenerator implements IMissileGenerator {

    private final int width;
    private final int speed;

    MissileGenerator(final int boardWidth, final int missileSpeed) {
        this.width = boardWidth;
        this.speed = missileSpeed;
    }

    @Override
    public Sprite generate(final int x, final int y) {
        return new Missile(this.width, this.speed, x, y);
    }
}
