package data7.bugcollector.model;

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

import data7.project.Project;
import gitUtilitaries.GitActions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static data7.Utils.generateCommitOfInterest;

/**
 * BUg dataset implementation for project that mention bug id in their commits
 */
public class BugIdDataset extends BugDataset implements Serializable {
    private static final long serialVersionUID = 20180608L;

    public BugIdDataset(Project project) {
        super(project);
    }

    /**
     * Method to create or update the bug dataset
     *
     * @param mapOfBug  from the data7
     * @param mapOfVuln vulnerability to bug map
     * @param git       object to perform operation
     */
    public void updateDataset(Map<String, List<String>> mapOfBug, Map<String, List<String>> mapOfVuln, GitActions git) {
        mapOfBug.entrySet()
                .parallelStream()
                .forEach(
                        entry -> {
                            if (!mapOfVuln.containsKey(entry.getKey())) {
                                for (String hash : entry.getValue()) {
                                    if (!dataset.containsKey(hash)) {
                                        dataset.put(hash, generateCommitOfInterest(git, hash, true));
                                    }
                                }
                            } else {
                                for (String id : mapOfVuln.get(entry.getKey())) {
                                    if (mapOfBug.containsKey(id)) {
                                        for (String hash : mapOfBug.get(id)) {
                                            dataset.remove(hash);
                                        }
                                    }
                                }
                            }
                        });
    }

}
