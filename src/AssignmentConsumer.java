import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Contains the data and behavior of an assignment consumer
 */
public class AssignmentConsumer implements Runnable {

    /**
     * Concurrent hash map for assignments where the key is a
     * transaction ID and the value is an assignment.
     */
    private ConcurrentHashMap<Integer, Assignment> assignmentMap;

    /**
     * Maintains state about whether production of assignments is complete
     */
    private AtomicBoolean isProductionDone;

    /**
     * Final number of assignments produced
     */
    private AtomicInteger numberOfAssignments;

    /**
     * Creates a fully initialized assignment consumer using the given data
     *
     * @param assignmentMap Concurrent hash map for assignments
     */
    public AssignmentConsumer(ConcurrentHashMap<Integer, Assignment> assignmentMap,
                              AtomicBoolean isProductionDone,
                              AtomicInteger numberOfAssignments) {

        if (assignmentMap == null) {
            throw new IllegalArgumentException("Cannot consume assignments from a null data structure");
        }

        if (isProductionDone == null) {
            throw new IllegalArgumentException("Unable to receive notification about assignment production being done");
        }

        if (numberOfAssignments == null) {
            throw new IllegalArgumentException("Unable to receive a report about the number of assignments produced");
        }

        this.assignmentMap = assignmentMap;
        this.isProductionDone = isProductionDone;
        this.numberOfAssignments = numberOfAssignments;
    }

    /**
     * Override of the Runnable interface's run method that
     * consumes assignments from a concurrent hash map
     */
    @Override
    public void run() {

        // Keep cycling until production is done and the all assignments are consumed
        int numberOfAssignmentsConsumed = 0;
        while (!isProductionDone.get() || (numberOfAssignmentsConsumed < numberOfAssignments.get())) {

            // Reading data from the ConcurrentHashMap
            for (Map.Entry<Integer, Assignment> entry : assignmentMap.entrySet()) {

                // Consume the assignment
                Integer transactionId = entry.getKey();
                Assignment assignment = entry.getValue();
                System.out.println("Consumer received assignment #" + transactionId + ": " + assignment);

                // Remove the entry after reading
                assignmentMap.remove(transactionId);

                // Increment the number of assignments consumed
                numberOfAssignmentsConsumed++;

                try {
                    Thread.sleep(1500); // Simulating some work being done
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
