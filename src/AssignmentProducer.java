import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Contains the data and behavior of an assignment producer
 */
public class AssignmentProducer implements Runnable {

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
     * Creates a fully initialized assignment producer using the given data
     *
     * @param assignmentMap        Concurrent hash map for assignments
     * @param isProductionDone     Maintains state about whether production of assignments is complete
     * @param numberOfAssignments  Final number of assignments produced
     */
    public AssignmentProducer(ConcurrentHashMap<Integer, Assignment> assignmentMap,
                              AtomicBoolean isProductionDone,
                              AtomicInteger numberOfAssignments) {

        if (assignmentMap == null) {
            throw new IllegalArgumentException("Cannot produce assignments into a null data structure");
        }

        if (isProductionDone == null) {
            throw new IllegalArgumentException("Unable to notify if assignment production is done");
        }

        if (numberOfAssignments == null) {
            throw new IllegalArgumentException("Unable to report number of assignments produced");
        }

        this.assignmentMap = assignmentMap;
        this.isProductionDone = isProductionDone;
        this.numberOfAssignments = numberOfAssignments;
    }

    /**
     * Override of the Runnable interface's run method that
     * inserts assignments into a concurrent hash map.
     */
    @Override
    public void run() {

        List<Assignment> assignments = null;
        try {

            // Create a list of assignments (which are unordered relative to due date/priority level)
            assignments = createAssignments();

            // Iterate over the unordered assignments and
            // insert them into the concurrent hash map
            int transactionId = 1;
            for (Assignment assignment : assignments) {

                System.out.println("Producing assignment #" + transactionId + ": " + assignment);
                assignmentMap.put(transactionId, assignment);
                transactionId++;
                Thread.sleep(500); // simulating some work
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Notify that production is complete and the final count of assignments produced
        isProductionDone.set(true);
        numberOfAssignments.set(assignments.size());
    }

    /**
     * Creates and returns a list of assignments
     */
    public List<Assignment> createAssignments() {

        List<Assignment> assignments = new ArrayList<>();

        // Insert assignments into the list (note that insertion order
        // is without consideration to due date/priority level rules)
        assignments.add(new Assignment("Buy milk and eggs",
                getDateXNumberOfDaysFromNow(2),
                Assignment.Priority.MEDIUM));

        assignments.add(new Assignment("Find new show on Netflix",
                getDateXNumberOfDaysFromNow(6),
                Assignment.Priority.LOW));

        assignments.add(new Assignment("Continue Udemy course",
                getDateXNumberOfDaysFromNow(5),
                Assignment.Priority.MEDIUM));

        assignments.add(new Assignment("Finish work assignment #1",
                getDateXNumberOfDaysFromNow(4),
                Assignment.Priority.HIGH));

        assignments.add(new Assignment("Finish work assignment #2",
                getDateXNumberOfDaysFromNow(2),
                Assignment.Priority.HIGH));

        assignments.add(new Assignment("Check out new restaurant",
                getDateXNumberOfDaysFromNow(13),
                Assignment.Priority.LOW));

        return assignments;
    }

    /**
     * Returns a date object that is set for X number of days from now
     *
     * @param daysFromNow X number of days from now
     */
    public static Date getDateXNumberOfDaysFromNow(long daysFromNow) {

        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Compute the future time in milliseconds as follows: daysFromNow * 24 * 3600 * 1000
        // This comes from:
        //     [daysFromNow] * 24 hours * 3600 seconds * 1000 milliseconds    -----> days from now in milliseconds
        //                     ________   ____________   _________________
        //                     1 day      1 hour         1 second
        long futureTimeMillis = currentTimeMillis + (daysFromNow * 24 * 3600 * 1000);

        // Create a Date object representing the future time
        return new Date(futureTimeMillis);
    }
}
