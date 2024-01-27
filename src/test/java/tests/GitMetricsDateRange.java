package tests;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GitMetricsDateRange {
    public static void main(String[] args) {
        // Specify the path to your Git repository
        String repoPath = "/path/to/your/git/repo";

        // Specify the branch name you want to analyze
        String branchToAnalyze = "main"; // Change this to the desired branch

        // Specify the developer's name
        String developerName = "John Doe"; // Change this to the desired developer

        // Specify the date range
        String startDateString = "2022-01-01";
        String endDateString = "2022-12-31";

        try (Git git = Git.open(new File(repoPath))) {
            // Get the list of branches
            Iterable<Ref> branches = git.branchList().call();

            System.out.println("Branches:");
            for (Ref branch : branches) {
                System.out.println(branch.getName());
            }

            // Get the commits on the specified branch within the date range for a specific developer
            Iterable<RevCommit> branchCommits = git.log()
                    .add(git.getRepository().resolve("refs/heads/" + branchToAnalyze))
                    .call();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            int commitCount = 0;

            System.out.println("\nCommits by " + developerName + " on " + branchToAnalyze + " branch between " +
                    startDateString + " and " + endDateString + ":");
            for (RevCommit commit : branchCommits) {
                PersonIdent author = commit.getAuthorIdent();
                if (author.getName().equals(developerName) && isDateInRange(commit.getAuthorIdent().getWhen(), startDate, endDate)) {
                    commitCount++;
                    System.out.println("Commit ID: " + commit.getId().getName());
                    System.out.println("Author: " + commit.getAuthorIdent().getName());
                    System.out.println("Message: " + commit.getFullMessage());
                    System.out.println("-----------------------");
                }
            }

            System.out.println("\nTotal number of commits by " + developerName + " on " + branchToAnalyze + " branch between " +
                    startDateString + " and " + endDateString + ": " + commitCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isDateInRange(Date date, Date startDate, Date endDate) {
        return date.after(startDate) && date.before(endDate);
    }
}
