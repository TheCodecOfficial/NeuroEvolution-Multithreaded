package ForkJoin;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import Agents.Agent;

public class NeuroEvolutionFJ extends RecursiveAction {

    Agent[] agents;
    int start, length, steps;

    final int CUTOFFF = 512;

    public NeuroEvolutionFJ(Agent[] agents, int start, int length, int steps) {
        this.agents = agents;
        this.start = start;
        this.length = length;
        this.steps = steps;
    }

    public static void update(Agent[] agents, int steps) {
        ForkJoinPool fjp = new ForkJoinPool();
        NeuroEvolutionFJ task = new NeuroEvolutionFJ(agents, 0, agents.length, steps);
        fjp.invoke(task);
    }

    protected void compute() {

        if (length <= CUTOFFF) {
            updateSeq(start, length);
        } else {

            int mid = length / 2;

            NeuroEvolutionFJ left = new NeuroEvolutionFJ(agents, start, mid, steps);
            NeuroEvolutionFJ right = new NeuroEvolutionFJ(agents, start + mid, length - mid, steps);

            left.fork();
            right.compute();
            left.join();
        }

    }

    void updateSeq(int start, int length) {
        for (int i = start; i < start + length; i++) {
            for (int k = 0; k < steps; k++) {
                if (agents[i].alive) {
                    agents[i].think();
                    agents[i].update();
                }
            }
        }
    }

}