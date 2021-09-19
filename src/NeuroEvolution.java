
import Agents.Agent;
import Agents.Drone;
import ForkJoin.NeuroEvolutionFJ;
import processing.core.PApplet;

public class NeuroEvolution extends PApplet {

    Agent[] population;
    final int POPULATION_SIZE = 8192;

    int gen;

    int time;
    final int timePerGeneration = 5 * 120; // 5 seconds * 120 fps

    double tx = 400;
    double ty = 400;

    public static void main(String[] args) {

        String[] processingArgs = { "Neuroevolution" };
        NeuroEvolution mySketch = new NeuroEvolution();
        PApplet.runSketch(processingArgs, mySketch);

    }

    // #region Processing Methods
    public void settings() {
        size(800, 800);
    }

    public void setup() {

        setupAgents();

        long t0 = System.nanoTime();
        simulateToGen(25);
        long t1 = System.nanoTime();
        System.out.println("Took " + (t1 - t0) / 1.0e9 + " seconds to simulate.");
    }
    // #endregion

    void setupAgents() {
        population = new Agent[POPULATION_SIZE];

        for (int i = 0; i < POPULATION_SIZE; i++) {
            Drone drone = new Drone(this);
            drone.setTarget(tx, ty);
            population[i] = drone;
        }
    }

    // #region Simulation Methods
    public void simulateToGen(int g) {
        while (gen < g)
            simulate();
    }

    public void simulate() {

        NeuroEvolutionFJ.update(population, timePerGeneration);

        time += timePerGeneration;
        if (time >= timePerGeneration) {
            time = 0;
            makeNextGen();
        }

    }
    // #endregion

    // #region Draw Methods
    public void draw() {
        frameRate(120);
        background(25);

        drawInfo();

        fill(0, 255, 0);
        ellipse((float) tx, (float) ty, 5, 5);

        for (int k = 0; k < 2; k++) {
            for (Agent agent : population) {
                agent.update();
                agent.think();
            }
            time++;
        }

        int i = 0;
        for (Agent agent : population) {
            // Drawing every 10th Agent to reduce lag
            if ((i++) % 10 == 0 && agent.alive)
                agent.draw();
        }

        fill(0, 255, 0);
        rect(0, 790, 800f * time / timePerGeneration, 800);

        if (time >= timePerGeneration) {
            time = 0;
            makeNextGen();
        }
    }

    void drawInfo() {
        fill(155);
        text("Generation " + gen, 10, 20);
        text("FPS: " + floor(frameRate), 10, 35);
    }
    // #endregion

    // #region Next Gen Methods
    void makeNextGen() {

        // Place random target
        tx = Math.random() * 800;
        ty = Math.random() * 800;

        // Calculate Scores
        for (Agent a : population)
            a.calculateScore();

        // Get survivor count
        int survivorCount = 0;
        for (Agent a : population)
            if (a.alive)
                survivorCount++;

        // Put survivors into new array
        // And sum all the scores
        Agent[] survivors = new Agent[survivorCount];
        int index = 0;
        double scoreSum = 0;
        for (Agent a : population) {
            if (a.alive) {
                survivors[index++] = a;
                scoreSum += a.score;
            }
        }

        // System.out.println("SCORE SUM = "+scoreSum);

        // Normalize all scores
        for (Agent a : survivors)
            a.score /= scoreSum;

        // Pick agents for new population
        Agent[] newPopulation = new Agent[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Drone clone = (Drone) pickAgent(survivors).clone();
            newPopulation[i] = clone;
            clone.setTarget(tx, ty);
            newPopulation[i].mutate();
        }

        population = newPopulation;
        gen++;
        System.out.println("Starting Generation " + gen);
    }

    public <T extends Agent> T pickAgent(T[] array) {
        int index = 0;
        double r = Math.random();
        while (r > 0) {
            r = r - array[index].score;
            index++;
        }
        index--;
        return array[index];
    }
    // #endregion

    public void mouseDragged() {
        tx = mouseX;
        ty = mouseY;

        for (Agent a : population) {
            Drone d = (Drone) a;
            d.setTarget(tx, ty);
        }
    }

}