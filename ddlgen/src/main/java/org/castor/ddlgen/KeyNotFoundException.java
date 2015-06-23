/*
 * Copyright 2006 Le Duc Bao, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.ddlgen;

/**
 * Configuration Key does not find exception.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.1
 */
public class KeyNotFoundException extends GeneratorException {
    //--------------------------------------------------------------------------

    /** Serial version UID. */
    private static final long serialVersionUID = -5312841848483797975L;

    //--------------------------------------------------------------------------

    /**
     * Construct KeyNotFoundException with given message.
     * 
     * @param message Exception message.
     */
    public KeyNotFoundException(final String message) {
        super(message);
    }

    /**
     * Construct KeyNotFoundException with given message and cause.
     * 
     * @param message Exception message.
     * @param ex Original exception cause.
     */
    public KeyNotFoundException (final String message, final Exception ex) {
        super(message, ex);
    }

    //--------------------------------------------------------------------------
}