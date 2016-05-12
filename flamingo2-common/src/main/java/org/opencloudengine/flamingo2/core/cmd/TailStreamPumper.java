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

import org.apache.commons.exec.util.DebugUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class TailStreamPumper implements Runnable {

    /**
     * the default size of the internal buffer for copying the streams
     */
    private static final int DEFAULT_SIZE = 1024;

    /**
     * the input stream to pump from
     */
    private final InputStream is;

    /**
     * the output stream to pmp into
     */
    private final OutputStream os;

    /**
     * the size of the internal buffer for copying the streams
     */
    private final int size;

    /**
     * was the end of the stream reached
     */
    private boolean finished;

    /**
     * close the output stream when exhausted
     */
    private final boolean closeWhenExhausted;

    /**
     * Create a new stream pumper.
     *
     * @param is                 input stream to read data from
     * @param os                 output stream to write data to.
     * @param closeWhenExhausted if true, the output stream will be closed when the input is exhausted.
     */
    public TailStreamPumper(final InputStream is, final OutputStream os,
                            final boolean closeWhenExhausted) {
        this.is = is;
        this.os = os;
        this.size = DEFAULT_SIZE;
        this.closeWhenExhausted = closeWhenExhausted;
    }

    /**
     * Create a new stream pumper.
     *
     * @param is                 input stream to read data from
     * @param os                 output stream to write data to.
     * @param closeWhenExhausted if true, the output stream will be closed when the input is exhausted.
     * @param size               the size of the internal buffer for copying the streams
     */
    public TailStreamPumper(final InputStream is, final OutputStream os,
                            final boolean closeWhenExhausted, final int size) {
        this.is = is;
        this.os = os;
        this.size = (size > 0 ? size : DEFAULT_SIZE);
        this.closeWhenExhausted = closeWhenExhausted;
    }

    /**
     * Create a new stream pumper.
     *
     * @param is input stream to read data from
     * @param os output stream to write data to.
     */
    public TailStreamPumper(final InputStream is, final OutputStream os) {
        this(is, os, false);
    }

    /**
     * Copies data from the input stream to the output stream. Terminates as
     * soon as the input stream is closed or an error occurs.
     */
    public void run() {
        synchronized (this) {
            // Just in case this object is reused in the future
            finished = false;
        }
        final byte[] buf = new byte[this.size];

        int length;
        try {
            while ((length = is.read(buf)) > 0) {
                os.write(buf, 0, length);
            }
        } catch (Exception e) {
            // nothing to do - happens quite often with watchdog
        } finally {
            if (closeWhenExhausted) {
                try {
                    os.close();
                } catch (IOException e) {
                    String msg = "Got exception while closing exhausted output stream";
                    DebugUtils.handleException(msg, e);
                }
            }
            synchronized (this) {
                finished = true;
                notifyAll();
            }
        }
    }

    /**
     * Tells whether the end of the stream has been reached.
     *
     * @return true is the stream has been exhausted.
     */
    public synchronized boolean isFinished() {
        return finished;
    }

    /**
     * This method blocks until the stream pumper finishes.
     *
     * @see #isFinished()
     */
    public synchronized void waitFor() throws InterruptedException {
        while (!isFinished()) {
            wait();
        }
    }
}
