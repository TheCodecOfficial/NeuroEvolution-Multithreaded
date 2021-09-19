package Agents;

import java.lang.reflect.InvocationTargetException;

import NeuralNet.NeuralNetwork;
import processing.core.PApplet;

public abstract class Agent implements Cloneable {

    public NeuralNetwork brain;

    public boolean alive;
    public double score;

    protected PApplet P;

    public Agent(PApplet P) {
        this.P = P;
        alive = true;
        score = 0;
    }

    public abstract void update();

    public abstract void think();

    public abstract void calculateScore();

    public abstract void draw();

    public void mutate() {
        brain.mutate();
    }

    public Agent clone() {
        try {
            Agent b = getClass().getDeclaredConstructor(PApplet.class).newInstance(P);
            NeuralNetwork brainCopy = brain.copy();
            b.brain = brainCopy;
            return b;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

}