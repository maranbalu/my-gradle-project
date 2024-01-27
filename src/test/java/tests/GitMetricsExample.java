package tests;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;

public class GitMetricsExample {
    public static void main(String[] args) {
        // Specify the path to your Git repository
        String repoPath = "C:\\Users\\Mani\\AppData\\Local\\Temp\\JavaPlayground";
        String branchToAnalyze = "refs/heads/sprint_30_1";

        try (Git git = Git.open(new File(repoPath))) {
            // Get the list of branches
            Iterable<Ref> branches = git.branchList().call();

            System.out.println("Branches:");
            for (Ref branch : branches) {
                System.out.println(branch.getName());
            }

            // Get the latest commit on the master branch
            RevWalk revWalk = new RevWalk(git.getRepository());
            RevCommit latestCommit = revWalk.parseCommit(git.getRepository().resolve(branchToAnalyze));

            System.out.println("\nLatest commit on specified branch:");
            System.out.println("Commit ID: " + latestCommit.getId().getName());
            System.out.println("Author: " + latestCommit.getAuthorIdent().getName());
            System.out.println("Message: " + latestCommit.getFullMessage());

            // Get the total number of commits on the master branch
            Iterable<RevCommit> allCommits = git.log().add(git.getRepository().resolve(branchToAnalyze)).call();

            int commitCount = 0;
            for (RevCommit commit : allCommits) {
                System.out.println("commit ID: " + commit.getFullMessage());
                commitCount++;
            }

            System.out.println("\nTotal number of commits on master branch: " + commitCount);

        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }
}
