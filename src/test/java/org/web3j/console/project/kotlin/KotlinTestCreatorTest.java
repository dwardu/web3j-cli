/*
 * Copyright 2019 Web3 Labs Ltd.
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
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import org.web3j.console.Web3j;
import org.web3j.console.project.utils.ClassExecutor;
import org.web3j.console.project.utils.Folders;

import static java.io.File.separator;

public class KotlinTestCreatorTest extends ClassExecutor {
    private String formattedPath =
            new File(String.join(separator, "src", "test", "resources", "Solidity"))
                    .getAbsolutePath();
    private String tempDirPath;

    @BeforeEach
    void setup() {
        this.tempDirPath = Folders.tempBuildFolder().getAbsolutePath();
    }

    @Test
    public void testThatCorrectArgumentsArePassed() {
        final String[] args = {"-i=" + formattedPath, "-o=" + tempDirPath};
        final KotlinTestCLIRunner kotlinTestCLIRunner = new KotlinTestCLIRunner();
        new CommandLine(kotlinTestCLIRunner).parseArgs(args);
        assert kotlinTestCLIRunner.unitTestOutputDir.equals(tempDirPath);
        assert kotlinTestCLIRunner.javaWrapperDir.equals(formattedPath);
    }

    @Test
    public void verifyThatTestsAreGenerated() throws IOException, InterruptedException {
        final String[] args = {
            "import",
            "--kotlin",
            "-p=org.com",
            "-n=Testing",
            "-o=" + tempDirPath,
            "-s=" + formattedPath
        };
        final String pathToJavaWrappers =
                new File(
                                String.join(
                                        separator,
                                        tempDirPath,
                                        "Testing",
                                        "build",
                                        "generated",
                                        "sources",
                                        "web3j",
                                        "main",
                                        "java"))
                        .getCanonicalPath();
        int exitCode =
                executeClassAsSubProcessAndReturnProcess(
                                Web3j.class, Collections.emptyList(), Arrays.asList(args), true)
                        .inheritIO()
                        .start()
                        .waitFor();
        Assertions.assertEquals(0, exitCode);
        final String[] unitTestsArgs = {
            "generate", "tests", "kotlin", "-i=" + pathToJavaWrappers, "-o=" + tempDirPath
        };
        int testsExitCode =
                executeClassAsSubProcessAndReturnProcess(
                                Web3j.class,
                                Collections.emptyList(),
                                Arrays.asList(unitTestsArgs),
                                true)
                        .inheritIO()
                        .start()
                        .waitFor();
        Assertions.assertEquals(0, testsExitCode);
    }
}
