import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
     * Creates a fully initialized assignment producer using the given data
     *
     * @param assignmentMap Concurrent hash map for assignments
     */
    public AssignmentProducer(ConcurrentHashMap<Integer, Assignment> assignmentMap) {
        this.assignmentMap = assignmentMap;
    }

    /**
     * Override of the Runnable interface's run method that
     * inserts assignments into a concurrent hash map.
     */
    @Override
    public void run() {
        try {

            // Create a list of assignments (which are unordered relative to due date/priority level)
            List<Assignment> assignments = createAssignments();

            // Iterate over the unordered assignments and
            // insert them into the concurrent hash map
            int transactionId = 1;
            for (Assignment assignment : assignments) {

                System.out.println("Producing assignment #" + transactionId + ": " + assignment);
                assignmentMap.put(transactionId, assignment);
                Thread.sleep(500); // simulating some work
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
