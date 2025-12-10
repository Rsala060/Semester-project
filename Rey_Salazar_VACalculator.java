import java.util.Arrays;
import java.util.Scanner;

/**
 * VACompensationCalculator
 *
 * This program lets a user enter VA disability ratings for
 * current and proposed scenarios. It calculates a combined
 * rating using a simplified version of the VA "whole person"
 * formula and looks up an estimated monthly compensation.
 *
 * It demonstrates arrays, loops, methods, and formatted output.
 */
public class Rey_Salazar_VACalculator {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int[] currentRatings = null;
        int[] proposedRatings = null;

        int choice;

        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            while (!input.hasNextInt()) {
                System.out.print("Please enter a number from the menu: ");
                input.next(); // clear invalid input
            }
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    currentRatings = enterRatings(input, "current");
                    break;
                case 2:
                    proposedRatings = enterRatings(input, "proposed");
                    break;
                case 3:
                    System.out.println("\n=== View Current Scenario ===");
                    showScenario("Current", currentRatings);
                    break;
                case 4:
                    System.out.println("\n=== Compare Current vs Proposed ===");
                    compareScenarios(currentRatings, proposedRatings);
                    break;
                case 5:
                    System.out.println("Exiting program. Thank you for using the VA Calculator.");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose 1–5.");
            }

            System.out.println(); // blank line for spacing
        } while (choice != 5);

        input.close();
    }

    /**
     * Displays the main menu options.
     */
    public static void displayMenu() {
        System.out.println("===== VA Disability Compensation Calculator =====");
        System.out.println("1. Enter CURRENT ratings");
        System.out.println("2. Enter PROPOSED ratings");
        System.out.println("3. Show combined rating & estimated pay (CURRENT)");
        System.out.println("4. Compare CURRENT vs PROPOSED");
        System.out.println("5. Exit");
    }

    /**
     * Prompts the user to enter a list of ratings and stores them in an array.
     *
     * @param input Scanner for user input
     * @param label String to show whether this is current or proposed
     * @return array of ratings
     */
    public static int[] enterRatings(Scanner input, String label) {
        System.out.print("How many " + label + " conditions do you want to enter? ");
        int count = input.nextInt();

        while (count <= 0) {
            System.out.print("Please enter a positive number of conditions: ");
            count = input.nextInt();
        }

        int[] ratings = new int[count];

        System.out.println("Enter each " + label + " rating as a whole number (10, 20, 30, etc.):");
        for (int i = 0; i < ratings.length; i++) {
            System.out.print("Rating " + (i + 1) + ": ");
            ratings[i] = input.nextInt();
            if (ratings[i] < 0 || ratings[i] > 100) {
                System.out.println("Rating should be between 0 and 100. Setting this one to 0.");
                ratings[i] = 0;
            }
        }

        return ratings;
    }

    /**
     * Calculates the combined disability rating using a simplified
     * version of the VA "whole person" formula.
     *
     * Steps (simplified):
     * 1. Sort ratings from highest to lowest.
     * 2. Start from 0%. For each rating r:
     *    combined = combined + (100 - combined) * (r / 100.0)
     * 3. Round the final result to the nearest 10.
     *
     * @param ratings array of condition ratings
     * @return combined rating rounded to nearest 10
     */
    public static int calculateCombinedRating(int[] ratings) {
        if (ratings == null || ratings.length == 0) {
            return 0;
        }

        int[] sorted = Arrays.copyOf(ratings, ratings.length);
        Arrays.sort(sorted); // ascending

        double combined = 0.0;

        // process from largest to smallest
        for (int i = sorted.length - 1; i >= 0; i--) {
            int r = sorted[i];
            combined = combined + (100 - combined) * (r / 100.0);
        }

        int combinedInt = (int) Math.round(combined);

        // round to nearest 10
        int remainder = combinedInt % 10;
        if (remainder >= 5) {
            combinedInt += (10 - remainder);
        } else {
            combinedInt -= remainder;
        }

        if (combinedInt > 100) {
            combinedInt = 100;
        }

        return combinedInt;
    }

    /**
     * Looks up a sample monthly compensation amount for a given combined rating.
     * NOTE: These values are only examples and not official VA rates.
     *
     * @param combined combined rating
     * @return estimated monthly compensation in dollars
     */
    public static int getMonthlyCompensation(int combined) {
        switch (combined) {
            case 10:
                return 171;
            case 20:
                return 338;
            case 30:
                return 524;
            case 40:
                return 755;
            case 50:
                return 1075;
            case 60:
                return 1361;
            case 70:
                return 1716;
            case 80:
                return 1995;
            case 90:
                return 2241;
            case 100:
                return 3737;
            default:
                return 0; // 0% or anything not in the table
        }
    }

    /**
     * Shows the combined rating and estimated compensation for a single scenario.
     *
     * @param name    label for the scenario (Current/Proposed)
     * @param ratings ratings array
     */
    public static void showScenario(String name, int[] ratings) {
        if (ratings == null || ratings.length == 0) {
            System.out.println("No " + name.toLowerCase() + " ratings have been entered yet.");
            return;
        }

        System.out.print(name + " ratings: ");
        for (int r : ratings) {
            System.out.print(r + "% ");
        }
        System.out.println();

        int combined = calculateCombinedRating(ratings);
        int pay = getMonthlyCompensation(combined);

        System.out.printf("%s combined rating: %d%%%n", name, combined);
        System.out.printf("%s estimated monthly compensation: $%d%n", name, pay);
        System.out.println("(Compensation amounts are sample values, not official VA numbers.)");
    }

    /**
     * Compares the current and proposed scenarios, showing the difference in pay.
     *
     * @param current  current ratings array
     * @param proposed proposed ratings array
     */
    public static void compareScenarios(int[] current, int[] proposed) {
        if (current == null || current.length == 0) {
            System.out.println("You have not entered CURRENT ratings yet.");
            return;
        }
        if (proposed == null || proposed.length == 0) {
            System.out.println("You have not entered PROPOSED ratings yet.");
            return;
        }

        showScenario("Current", current);
        System.out.println();
        showScenario("Proposed", proposed);
        System.out.println();

        int currentCombined = calculateCombinedRating(current);
        int proposedCombined = calculateCombinedRating(proposed);

        int currentPay = getMonthlyCompensation(currentCombined);
        int proposedPay = getMonthlyCompensation(proposedCombined);

        int difference = proposedPay - currentPay;

        System.out.println("=== Difference ===");
        System.out.printf("Change in combined rating: %d%% -> %d%%%n",
                currentCombined, proposedCombined);
        System.out.printf("Change in monthly pay: $%d -> $%d%n",
                currentPay, proposedPay);

        if (difference > 0) {
            System.out.printf("Estimated increase: $%d per month%n", difference);
        } else if (difference < 0) {
            System.out.printf("Estimated decrease: $%d per month%n", Math.abs(difference));
        } else {
            System.out.println("No change in estimated monthly compensation.");
        }
    }
}

