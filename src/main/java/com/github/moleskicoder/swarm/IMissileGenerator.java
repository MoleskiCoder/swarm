package com.github.moleskicoder.swarm;

@FunctionalInterface
interface IMissileGenerator {
    Sprite generate(int x, int y);
}
