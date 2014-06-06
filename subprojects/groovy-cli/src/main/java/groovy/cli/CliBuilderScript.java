/*
 * Copyright 2003-2014 the original author or authors.
 *
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
 */

package groovy.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;
import com.beust.jcommander.ParameterException;
import groovy.lang.MissingPropertyException;
import groovy.util.CliBuilder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.join;

/**
 * Base script that provides JCommander declarative (annotation-based) argument processing for scripts.
 *
 * @author Jim White
 */

abstract public class CliBuilderScript extends AbstractCommandScript {
    /**
     * The script body
     * @return The result of the script evaluation.
     */
    protected abstract Object runScriptBody();

    @Override
    public Object run() {
        String[] args = getScriptArguments();
        CliBuilder jc = new CliBuilder();
        try {
            jc.parseFromInstance(this, args);

            //TODO: How is help option handled?
//            for (ParameterDescription pd : jc.getParameters()) {
//                if (pd.isHelp() && pd.isAssigned()) return exitCode(printHelpMessage(jc, args));
//            }

            //TODO: How are subcommands handled?
//            runScriptCommand(jc);

            return exitCode(runScriptBody());
        } catch (ParameterException pe) {
            return exitCode(handleParameterException(jc, args, pe));
        }
    }

    /**
     * TODO: Fix up this API for how CliParser deals with errors.
     * 
     * If an error occurs during parseScriptArguments, runScriptCommand, or runScriptBody
     * then this gets called to report the problem.
     * The default behavior is to show the exception message using printErrorMessage, then call printHelpMessage.
     * The return value becomes the return value for the Script.run which will be the exit code
     * if we've been called from the command line.
     *
     * @param jc The CliBuilder instance
     * @param args The argument array
     * @param pe The ParameterException that occurred
     * @return The value that Script.run should return (2 by default).
     */
    public Object handleParameterException(CliBuilder jc, String[] args, Exception pe) {
        StringBuilder sb = new StringBuilder();

        sb.append("args: [");
        sb.append(join(args, ", "));
        sb.append("]");
        sb.append("\n");

        sb.append(pe.getMessage());

        printErrorMessage(sb.toString());

        printHelpMessage(jc, args);

        return 3;
    }

    /**
     * If a @Parameter whose help attribute is annotated as true appears in the arguments.
     * then the script body is not run and this printHelpMessage method is called instead.
     * The default behavior is to show the arguments and the JCommander.usage using printErrorMessage.
     * The return value becomes the return value for the Script.run which will be the exit code
     * if we've been called from the command line.
     *
     * @param jc The CliBuilder instance
     * @param args The argument array
     * @return The value that Script.run should return (1 by default).
     */
    public Object printHelpMessage(CliBuilder jc, String[] args) {
        StringWriter sb = new StringWriter();
        PrintWriter pw = new PrintWriter(sb);

        //TODO: Make this whatever the API is expecting...
        jc.getParser().displayHelp(pw, "???", "???");

        pw.flush();

        printErrorMessage(sb.toString());

        return 2;
    }
    
}
