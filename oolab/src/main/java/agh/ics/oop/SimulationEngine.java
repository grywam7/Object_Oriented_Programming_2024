package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simulations;

    public SimulationEngine(List<Simulation> simulations){
        this.simulations = simulations;
    }

    public void runSync(){
        for(Simulation simulation : simulations ){
            simulation.run();
        }
    }

    public void runAsync() {
        for (Simulation simulation : simulations) {
            Thread thread = new Thread(simulation);
            thread.start();
        }
    }

    public void awaitSimulationsEnd() {
        try {
            List<Thread> threads = new ArrayList<>();
            for (Simulation simulation : simulations) {
                Thread thread = new Thread(simulation);
                threads.add(thread);

                thread.start();
            }
            for(Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted thread: " + e );
            Thread.currentThread().interrupt();
        }
    }

    public void runAsyncInThreadPool(){
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(4);
            for (Simulation simulation : simulations) {
                executorService.submit(simulation);
            }
            executorService.shutdown();
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Thread pool did not terminate in 10 seconds. Forcing shutdown.");
                executorService.shutdownNow(); // Force shutdown
            }

        } catch (InterruptedException e) {
            System.err.println("Interrupted thread: " + e );
            Thread.currentThread().interrupt();
        }
    }

}
