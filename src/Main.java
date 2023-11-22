import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main class for the concurrent hash map example console app.
 */
public class Main {

    /**
     * Entry point for the concurrent hash map example console app.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {

        // Creating a concurrent hash map where each key is a transaction ID and each value is an assignment
        ConcurrentHashMap<Integer, Assignment> assignmentMap = new ConcurrentHashMap<>();

        // Create an atomic boolean that the producer thread will be able to use to
        // signal the consumer thread that production is done
        AtomicBoolean isProductionDone = new AtomicBoolean(false);

        // Create an atomic integer that the producer thread will be able ot use to
        // signal the consumer thread how many assignments have been produced.  This
        // will be initialized to the max value and then will be updated to a real
        // value once production is complete so the consumer thread will know how
        // many assignments are left to consume
        AtomicInteger numberOfAssignments = new AtomicInteger(Integer.MAX_VALUE);

        // Creating producer and consumer threads
        Thread producerThread = new Thread(
                new AssignmentProducer(assignmentMap, isProductionDone, numberOfAssignments));
        Thread consumerThread = new Thread(
                new AssignmentConsumer(assignmentMap, isProductionDone, numberOfAssignments));

        // Starting the threads
        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        System.out.println("All assignments produced and consumed");
    }
}