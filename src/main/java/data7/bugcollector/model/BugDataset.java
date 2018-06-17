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

import data7.model.change.Commit;
import data7.project.Project;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class containing the bug dataset
 */
public abstract class BugDataset implements Serializable {

    private static final long serialVersionUID = 20180608L;

    //bug dataset
    protected final Map<String, Commit> dataset;
    //project from the bug dataset
    private final Project project;

    protected BugDataset(Project project) {
        this.dataset = new ConcurrentHashMap<>();
        this.project = project;
    }


    public Map<String, Commit> getDataset() {
        return dataset;
    }

    public Project getProject() {
        return project;
    }
}
