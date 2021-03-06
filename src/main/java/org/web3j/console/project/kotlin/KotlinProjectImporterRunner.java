/*
 * Copyright 2020 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.console.project.kotlin;

import java.io.File;
import java.util.Optional;

import org.web3j.console.project.ProjectImporterConfig;

public class KotlinProjectImporterRunner extends KotlinProjectRunner {

    public String solidityImportPath;
    public boolean shouldGenerateTests;

    public KotlinProjectImporterRunner(final ProjectImporterConfig projectImporterConfig) {
        super(projectImporterConfig);
        solidityImportPath = projectImporterConfig.getSolidityImportPath();
        shouldGenerateTests = projectImporterConfig.shouldGenerateTests();
    }

    protected void createProject() {
        generateKotlin(
                shouldGenerateTests,
                Optional.of(new File(solidityImportPath)),
                false,
                false,
                "import");
    }
}
