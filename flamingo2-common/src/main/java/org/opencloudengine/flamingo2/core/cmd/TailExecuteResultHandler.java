/**
 * Copyright (C) 2011 Flamingo Project (http://www.opencloudengine.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.core.cmd;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.Executor;

/**
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class TailExecuteResultHandler implements ExecuteResultHandler {

    /**
     * the interval polling the result
     */
    private static final int SLEEP_TIME_MS = 50;

    /**
     * Keep track if the process is still running
     */
    private volatile boolean hasResult;

    /**
     * The exit value of the finished process
     */
    private volatile int exitValue;

    /**
     * Any offending exception
     */
    private volatile ExecuteException exception;

    /**
     * Constructor.
     */
    public TailExecuteResultHandler() {
        this.hasResult = false;
        this.exitValue = Executor.INVALID_EXITVALUE;
    }

    /**
     * @see org.apache.commons.exec.ExecuteResultHandler#onProcessComplete(int)
     */
    public void onProcessComplete(int exitValue) {
        this.exitValue = exitValue;
        this.exception = null;
        this.hasResult = true;
    }

    /**
     * @see org.apache.commons.exec.ExecuteResultHandler#onProcessFailed(org.apache.commons.exec.ExecuteException)
     */
    public void onProcessFailed(ExecuteException e) {
        this.exitValue = e.getExitValue();
        this.exception = e;
        this.hasResult = true;
    }

    /**
     * Get the <code>exception<code> causing the process execution to fail.
     *
     * @return Returns the exception.
     * @throws IllegalStateException if the process has not exited yet
     */
    public ExecuteException getException() {
        if (!hasResult) {
            throw new IllegalStateException("The process has not exited yet therefore no result is available ...");
        }
        return exception;
    }

    /**
     * Get the <code>exitValue<code> of the process.
     *
     * @return Returns the exitValue.
     * @throws IllegalStateException if the process has not exited yet
     */
    public int getExitValue() {
        if (!hasResult) {
            throw new IllegalStateException("The process has not exited yet therefore no result is available ...");
        }

        return exitValue;
    }

    /**
     * Has the process exited and a result is available, i.e. exitCode or exception?
     *
     * @return true if a result of the execution is available
     */
    public boolean hasResult() {
        return hasResult;
    }

    /**
     * Causes the current thread to wait, if necessary, until the
     * process has terminated. This method returns immediately if
     * the process has already terminated. If the process has
     * not yet terminated, the calling thread will be blocked until the
     * process exits.
     *
     * @throws InterruptedException if the current thread is
     *                              {@linkplain Thread#interrupt() interrupted} by another
     *                              thread while it is waiting, then the wait is ended and
     *                              an {@link InterruptedException} is thrown.
     */
    public void waitFor() throws InterruptedException {
        while (!hasResult()) {
            Thread.sleep(SLEEP_TIME_MS);
        }
    }

    /**
     * Causes the current thread to wait, if necessary, until the
     * process has terminated. This method returns immediately if
     * the process has already terminated. If the process has
     * not yet terminated, the calling thread will be blocked until the
     * process exits.
     *
     * @param timeout the maximum time to wait in milliseconds
     * @throws InterruptedException if the current thread is
     *                              {@linkplain Thread#interrupt() interrupted} by another
     *                              thread while it is waiting, then the wait is ended and
     *                              an {@link InterruptedException} is thrown.
     */
    public void waitFor(long timeout) throws InterruptedException {
        long until = System.currentTimeMillis() + timeout;
        while (!hasResult() && (System.currentTimeMillis() < until)) {
            Thread.sleep(SLEEP_TIME_MS);
        }
    }
}