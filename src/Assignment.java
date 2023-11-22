import java.util.Date;
import java.util.Objects;

/**
 * Defines the characteristics of an assignment.
 */
public class Assignment implements Comparable<Assignment> {

    /**
     * Defines priority enum values
     */
    public enum Priority {
        HIGH(1),
        MEDIUM(2),
        LOW(3);

        /**
         * Numerical value associated with a priority enum.
         * Note: a lower numerical value corresponds to a higher priority enum
         */
        private final int value;

        /**
         * Facilitates the association between a numerical value and a priority enum
         *
         * @param value A numerical value
         */
        Priority(int value) {
            this.value = value;
        }

        /**
         * Returns the numerical value associated with a priority enum
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * Description of the assignment
     */
    private String description;

    /**
     * When the assignment is due
     */
    private Date dueDate;

    /**
     * Priority enum for the assignment
     */
    private Priority priority;

    /**
     * Creates a fully initialized assignment instance using the given data
     *
     * @param description
     * @param dueDate
     * @param priority
     */
    public Assignment(String description, Date dueDate, Priority priority) {

        // Null check the parameters that could possibly be null
        if (description == null) {
            throw new IllegalArgumentException("Cannot create an assignment with a null description");
        }

        if (dueDate == null) {
            throw new IllegalArgumentException("Cannot create an assignment with a null due date");
        }

        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    /**
     * Returns the assignment's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the assignment's due date
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Returns the assignments priority enum value
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Override of Comparable.compareTo to specify that elements should be compared based on:
     * 1. due date
     * 2. priority
     * @param o the object to be compared.
     */
    @Override
    public int compareTo(Assignment o) {

        // Compare by due date first
        int dateComparison = this.dueDate.compareTo(o.dueDate);

        // If due dates are different return the result
        if (dateComparison != 0) {
            return dateComparison;
        }

        // Since due dates are equal break the tie with priority
        return this.priority.compareTo(o.priority);
    }

    /**
     * Returns the string representation of this assignment instance
     */
    @Override
    public String toString() {
        return "Assignment{" +
                "description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", priority=" + priority +
                '}';
    }

    /**
     * Determines if this assignment instance equals the other assignment instance
     * @param o The other assignment instance
     * @return True if this assignment instance equals the other assignment instance, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(dueDate, that.dueDate) &&
                priority == that.priority;
    }

    /**
     * Computes and returns this assignment instance's hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, dueDate, priority);
    }
}
