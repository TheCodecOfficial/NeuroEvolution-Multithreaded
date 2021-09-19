package Agents;
import NeuralNet.*;
import processing.core.PApplet;

public class Drone extends Agent {

    public double x, y, vx, vy;

    double tx, ty;

    double multiplier;

    int hidden;

    public Drone(PApplet P) {
        super(P);
        x = Math.random() * 800;
        y = Math.random() * 800;
        alive = true;
        hidden = (int) (Math.random() * 4) + 1;
        brain = new NeuralNetwork(4, hidden, 2);
        brain.setMutationRate(0.01);
    }

    public void setTarget(double tx, double ty) {
        this.tx = tx;
        this.ty = ty;
    }

    @Override
    public void update() {
        if (!alive)
            return;
        x += vx;
        y += vy;

        multiplier += vx * vx + vy * vy;

        if (x < 0 || x > 800 || y < 0 || y > 800)
            alive = false;
    }

    @Override
    public void think() {
        Matrix result = brain.evaluate(new double[] { x / 800d, y / 800d, tx / 800d, ty / 800d });
        // Matrix result = brain.evaluate(new double[] { x / 800d, tx / 800d });
        vx = result.get(0, 0) * 20;
        vy = result.get(1, 0) * 20;
    }

    @Override
    public void draw() {
        if (!alive)
            return;
        P.fill(0, 55, 255 * hidden / 4f);
        P.ellipse((float) x, (float) y, 10, 10);
    }

    @Override
    public void calculateScore() {
        double dx = x - tx;
        double dy = y - ty;
        dx /= 400;
        dy /= 400;
        double distanceSqr = (dx * dx + dy * dy);
        if (distanceSqr > 0.2) {
            alive = false;
        }

        score = 1 / (distanceSqr);
    }

}