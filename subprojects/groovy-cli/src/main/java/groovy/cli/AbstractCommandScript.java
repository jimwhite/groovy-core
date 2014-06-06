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
import groovy.lang.Script;

import java.util.List;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.join;

/**
 * Created by jim on 6/6/14.
 */
public abstract class AbstractCommandScript extends Script {

    /**
     * If the given code is numeric and non-zero, then return that from this process using System.exit.
     * Non-numeric values (including null) are taken to be zero and returned as-is.
     *
     * @param code
     * @return the given code
     */
    public Object exitCode(Object code) {
        if (code instanceof Number) {
            int codeValue = ((Number) code).intValue();
            if (codeValue != 0) System.exit(codeValue);
        }
        return code;
    }

    /**
     * Return the script arguments as an array of strings.
     * The default implementation is to get the "args" property.
     *
     * @return the script arguments as an array of strings.
     */
    public String[] getScriptArguments() {
        return (String[]) getProperty("args");
    }

    /**
     * Error messages that arise from command line processing call this.
     * The default is to use the Script's println method (which will go to the
     * 'out' binding, if any, and System.out otherwise).
     * If you want to use System.err, a logger, or something, this is the thing to override.
     *
     * @param message
     */
    public void printErrorMessage(String message) {
        println(message);
    }

}
