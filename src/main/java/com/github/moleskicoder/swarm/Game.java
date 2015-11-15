package com.github.moleskicoder.swarm;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

final class Game extends JFrame {

    public static final String RESOURCE_IMAGE_FOLDER = "/images/";

    private static final long serialVersionUID = -1462560687481476312L;

    private Game() {

        final int tick = 10;
        final int width = 800;
        final int height = 600;
        final int missileLimit = 100;
        final int missileSpeed = 5;
        final int alienSpeed = 4;
        final int alienCount = 100;

        this.add(new Board(tick, width, height, missileLimit, missileSpeed, alienCount, alienSpeed));

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setTitle("Swarm");
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(final String[] args) {
        new Game();
    }
}