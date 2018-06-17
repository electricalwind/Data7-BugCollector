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

import data7.model.Data7;
import data7.model.change.Commit;
import data7.model.vulnerability.Vulnerability;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Utils {

    public static Set<String> listOfCommitsFromData7(Data7 data7) {
        Set<String> commits = new HashSet<>();
        for (Map.Entry<String, Vulnerability> vuln : data7.getVulnerabilitySet().getVulnerabilityDataset().entrySet()) {
            if (vuln.getValue().getPatchingCommits().size() > 0) {
                for (Map.Entry<String, Commit> comm : vuln.getValue().getPatchingCommits().entrySet()) {
                    commits.add(comm.getKey());
                }

            }
        }
        return commits;
    }



}
