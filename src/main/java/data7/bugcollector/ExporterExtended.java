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

import data7.Exporter;
import data7.bugcollector.model.BugDataset;


import java.io.*;

import static data7.Utils.checkFolderDestination;

public class ExporterExtended extends Exporter {

    private final ResourcesPathExtended resourcesPathExtended;

    public ExporterExtended(ResourcesPathExtended resourcesPathExtended) {
        super(resourcesPathExtended);
        this.resourcesPathExtended = resourcesPathExtended;
    }

    public void saveBugDataset(BugDataset dataset) throws IOException {
        checkFolderDestination(resourcesPathExtended.getBugDatasetPath());
        FileOutputStream fos = new FileOutputStream(resourcesPathExtended.getBugDatasetPath() + dataset.getProject().getName() + "-bugdataset.obj", false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dataset);
        oos.close();
        fos.close();
    }

    public BugDataset loadBugDataset(String project) throws IOException, ClassNotFoundException {
        File file = new File(resourcesPathExtended.getBugDatasetPath() + project + "-bugdataset.obj");
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream read = new ObjectInputStream(fileIn);
            BugDataset data = (BugDataset) read.readObject();
            read.close();
            fileIn.close();
            return data;
        } else return null;
    }
}
