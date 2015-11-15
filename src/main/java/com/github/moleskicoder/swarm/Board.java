package com.github.moleskicoder.swarm;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

final class Board extends JPanel implements ActionListener {

    private static final long serialVersionUID = 6926381550552475356L;

    private final transient IAlienGenerator alienGenerator;
    private final transient List<Actor> aliens = new ArrayList<>();

    private final transient ICraft craft;
    private final Timer timer;

    private boolean running;
    private int boardWidth;
    private int boardHeight;

    Board(
            final int tickTime,
            final int width,
            final int height,
            final int missileLimit,
            final int missileSpeed,
            final int alienCount,
            final int alienSpeed) {

        this.addKeyListener(new TAdapter());
        this.setFocusable(true);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.running = true;

        this.setSize(width, height);

        this.alienGenerator = new AlienGenerator(width, height, alienSpeed);
        this.initialiseAliens(alienCount);

        this.craft = new Craft(width, height, missileSpeed, missileLimit);

        this.timer = new Timer(tickTime, this);
        this.timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        this.boardWidth = this.getWidth();
        this.boardHeight = this.getHeight();
    }

    @Override
    public void paint(final Graphics g) {

        super.paint(g);

        if (this.running) {
            this.craft.draw(g, this);
            this.drawMissiles(g);
            this.drawAliens(g);
            this.showStatus(g);
        } else {
            this.showGameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    // This is the game loop
    @Override
    public void actionPerformed(final ActionEvent e) {

        // Game over if no more aliens
        if (this.aliens.isEmpty()) {
            this.running = false;
        }

        // the craft is responsible for it's missiles
        this.craft.controlMissiles();

        // the board is responsible for the aliens
        this.controlAliens();

        // the board is responsible for the craft
        this.craft.move();

        this.evaluateCollisions();

        this.repaint();
    }

    private void initialiseAliens(final int count) {
        final List<Actor> alienList = this.aliens;
        final IAlienGenerator generator = this.alienGenerator;
        for (int i = 0; i < count; ++i) {
            alienList.add(generator.generate());
        }
    }

    private void showGameOver(final Graphics graphics) {

        assert graphics != null;

        graphics.setColor(Color.white);

        final Font small = new Font("Helvetica", Font.BOLD, 14);
        graphics.setFont(small);

        final String message = "Game Over";
        final FontMetrics metrics = this.getFontMetrics(small);
        graphics.drawString(message,
                (this.boardWidth - metrics.stringWidth(message)) / 2,
                this.boardHeight / 2);
    }

    private void drawAliens(final Graphics graphics) {
        for (final Sprite alien : this.aliens) {
            if (alien.isVisible()) {
                alien.draw(graphics, this);
            }
        }
    }

    private void drawMissiles(final Graphics graphics) {
        for (final Sprite missile : this.craft.getMissiles()) {
            if (missile.isVisible()) {
                missile.draw(graphics, this);
            }
        }
    }

    private void showStatus(final Graphics graphics) {
        assert graphics != null;
        graphics.setColor(Color.white);
        graphics.drawString("Aliens left: " + this.aliens.size(), 5, 15);
    }

    private void controlAliens() {
        final List<Actor> alienList = this.aliens;
        int numberOfAliens = alienList.size();
        for (int i = 0; i < numberOfAliens ; i++) {
            final Sprite alien = alienList.get(i);
            if (alien.isVisible()) {
                alien.move();
            } else {
                alienList.remove(i);
                --numberOfAliens;
            }
        }
    }

    private void evaluateCollisions() {

        // Check whether the aliens have caught the craft
        this.evaluateAlienCollisions();

        // Check whether any missiles have shot any aliens
        this.evaluateMissileCollisions();
    }

    private void evaluateMissileCollisions() {
        for (final Sprite missile : this.craft.getMissiles()) {
            final Rectangle missileBounds = missile.getBounds();
            for (final Actor alien : this.aliens) {
                if (alien.checkCollision(missileBounds)) {
                    missile.destroy();
                    alien.destroy();
                    break;
                }
            }
        }
    }

    private void evaluateAlienCollisions() {
        for (final Sprite alien : this.aliens) {
            if (this.craft.checkCollision(alien.getBounds())) {
                this.craft.destroy();
                alien.destroy();
                this.running = false;
                break;
            }
        }
    }

    private final class TAdapter extends KeyAdapter {

        TAdapter() {
        }

        @Override
        public void keyReleased(final KeyEvent e) {
            super.keyReleased(e);
            craft.keyReleased(e);
        }

        @Override
        public void keyPressed(final KeyEvent e) {
            super.keyPressed(e);
            craft.keyPressed(e);
        }
    }
}