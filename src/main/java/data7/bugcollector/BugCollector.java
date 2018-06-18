package data7.bugcollector;

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

import data7.bugcollector.model.BugDataset;
import data7.bugcollector.model.BugIdDataset;
import data7.bugcollector.model.BugRegExpDataset;
import data7.model.Data7;
import data7.project.CProjects;
import data7.project.Project;
import gitUtilitaries.GitActions;


import java.io.IOException;
import java.util.Set;

import static data7.bugcollector.Utils.listOfCommitsFromData7;


/**
 * Bug Collector Class
 */
public class BugCollector {

    private final ResourcesPathExtended resourcesPathExtended;
    private final ExporterExtended exporterExtended;


    public BugCollector(ResourcesPathExtended resourcesPathExtended){
        this.resourcesPathExtended = resourcesPathExtended;
        this.exporterExtended = new ExporterExtended(resourcesPathExtended);
    }

    public BugDataset updateOrCreateBugDataset(String projectName) throws IOException, ClassNotFoundException {
        Data7 datasetOfVuln = exporterExtended.loadDataset(projectName);
        if (datasetOfVuln == null) {
            throw new RuntimeException("invalid Project");
        }
        Project project = datasetOfVuln.getProject();
        GitActions git = new GitActions(datasetOfVuln.getProject().getOnlineRepository(), resourcesPathExtended.getGitPath() + project);


        BugDataset dataset = exporterExtended.loadBugDataset(project.getName());

        Set<String> commits = listOfCommitsFromData7(datasetOfVuln);
        if (dataset == null) {
            switch (project.getIndexOfBugIdinCommitMessage()) {
                case 0:
                    BugRegExpDataset datasetR = new BugRegExpDataset(project);
                    datasetR.updateDataset(commits, git);
                    git.close();
                    return datasetR;
                default:
                    BugIdDataset datasetI = new BugIdDataset(project);
                    datasetI.updateDataset(datasetOfVuln.getBugToHash(), datasetOfVuln.getBugToCve(), git);
                    git.close();
                    return datasetI;
            }
        } else {
            switch (project.getIndexOfBugIdinCommitMessage()) {
                case 0:
                    ((BugRegExpDataset) dataset).updateDataset(commits, git);
                    break;
                default:
                    ((BugIdDataset) dataset).updateDataset(datasetOfVuln.getBugToHash(), datasetOfVuln.getBugToCve(), git);
                    break;
            }
            exporterExtended.saveBugDataset(dataset);
            git.close();
            return dataset;
        }

    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long time = System.currentTimeMillis();
        ResourcesPathExtended path = new ResourcesPathExtended("/Users/matthieu/Desktop/data7/");
        BugCollector bugCollector = new BugCollector(path);
        System.out.println("Start Linux");
        bugCollector.updateOrCreateBugDataset(CProjects.LINUX_KERNEL.getName());
        System.out.println("End Linux : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        System.out.println("Start SystemD");
        bugCollector.updateOrCreateBugDataset(CProjects.SYSTEMD.getName());
        System.out.println("End SystemD : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        System.out.println("Start Wireshark");
        bugCollector.updateOrCreateBugDataset(CProjects.WIRESHARK.getName());
        System.out.println("End Wireshark : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        System.out.println("Start SSL");
        bugCollector.updateOrCreateBugDataset(CProjects.OPEN_SSL.getName());
        System.out.println("End SSL : " + (System.currentTimeMillis() - time));
    }
}
