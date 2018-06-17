package data7.bugcollector.callable;

/*-
 * #%L
 * Data7
 * %%
 * Copyright (C) 2018 University of Luxembourg, Matthieu Jimenez
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import data7.model.change.Commit;
import data7.project.Project;
import gitUtilitaries.GitActions;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static data7.Utils.generateCommitOfInterest;

public class CommitRegExp implements Callable<Commit> {

    private final Project project;
    private final RevCommit revCommit;
    private final GitActions git;

    public CommitRegExp(Project project, RevCommit revCommit, GitActions git) {
        this.revCommit = revCommit;
        this.project = project;
        this.git = git;
    }

    @Override
    public Commit call() {
        Pattern pattern = Pattern.compile("[.|\\r|\\n]*" + project.getPatchInCommitessageRegexp());
        Matcher m = pattern.matcher(revCommit.getFullMessage());
        if (m.find() && !revCommit.getFullMessage().contains("Merge") && !revCommit.getFullMessage().contains("Revert")) {
            return generateCommitOfInterest(git, revCommit, true);
        }
        return null;
    }
}
