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

import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.InputStreamPumper;
import org.apache.commons.exec.util.DebugUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class TailPumpStreamHandler implements ExecuteStreamHandler {

    private Thread outputThread;

    private Thread errorThread;

    private Thread inputThread;

    private final OutputStream out;

    private final OutputStream err;

    private final InputStream input;

    private InputStreamPumper inputStreamPumper;

    /**
     * Construct a new <CODE>PumpStreamHandler</CODE>.
     */
    public TailPumpStreamHandler() {
        this(System.out, System.err);
    }

    /**
     * Construct a new <CODE>PumpStreamHandler</CODE>.
     *
     * @param outAndErr the output/error <CODE>OutputStream</CODE>.
     */
    public TailPumpStreamHandler(final OutputStream outAndErr) {
        this(outAndErr, outAndErr);
    }

    /**
     * Construct a new <CODE>PumpStreamHandler</CODE>.
     *
     * @param out the output <CODE>OutputStream</CODE>.
     * @param err the error <CODE>OutputStream</CODE>.
     */
    public TailPumpStreamHandler(final OutputStream out, final OutputStream err) {
        this(out, err, null);
    }

    /**
     * Construct a new <CODE>PumpStreamHandler</CODE>.
     *
     * @param out   the output <CODE>OutputStream</CODE>.
     * @param err   the error <CODE>OutputStream</CODE>.
     * @param input the input <CODE>InputStream</CODE>.
     */
    public TailPumpStreamHandler(final OutputStream out, final OutputStream err,
                                 final InputStream input) {

        this.out = out;
        this.err = err;
        this.input = input;
    }

    /**
     * Set the <CODE>InputStream</CODE> from which to read the standard output
     * of the process.
     *
     * @param is the <CODE>InputStream</CODE>.
     */
    public void setProcessOutputStream(final InputStream is) {
        if (out != null) {
            createProcessOutputPump(is, out);
        }
    }

    /**
     * Set the <CODE>InputStream</CODE> from which to read the standard error
     * of the process.
     *
     * @param is the <CODE>InputStream</CODE>.
     */
    public void setProcessErrorStream(final InputStream is) {
        if (err != null) {
            createProcessErrorPump(is, err);
        }
    }

    /**
     * Set the <CODE>OutputStream</CODE> by means of which input can be sent
     * to the process.
     *
     * @param os the <CODE>OutputStream</CODE>.
     */
    public void setProcessInputStream(final OutputStream os) {
        if (input != null) {
            if (input == System.in) {
                inputThread = createSystemInPump(input, os);
            } else {
                inputThread = createPump(input, os, true);
            }
        } else {
            try {
                os.close();
            } catch (IOException e) {
                String msg = "Got exception while closing output stream";
                DebugUtils.handleException(msg, e);
            }
        }
    }

    /**
     * Start the <CODE>Thread</CODE>s.
     */
    public void start() {
        if (outputThread != null) {
            outputThread.start();
        }
        if (errorThread != null) {
            errorThread.start();
        }
        if (inputThread != null) {
            inputThread.start();
        }
    }

    /**
     * Stop pumping the streams.
     */
    public void stop() {

        if (inputStreamPumper != null) {
            inputStreamPumper.stopProcessing();
        }

        if (outputThread != null) {
            try {
                outputThread.join();
                outputThread = null;
            } catch (InterruptedException e) {
                // ignore
            }
        }

        if (errorThread != null) {
            try {
                errorThread.join();
                errorThread = null;
            } catch (InterruptedException e) {
                // ignore
            }
        }

        if (inputThread != null) {
            try {
                inputThread.join();
                inputThread = null;
            } catch (InterruptedException e) {
                // ignore
            }
        }

        if (err != null && err != out) {
            try {
                err.flush();
            } catch (IOException e) {
                String msg = "Got exception while flushing the error stream : " + e.getMessage();
                DebugUtils.handleException(msg, e);
            }
        }

        if (out != null) {
            try {
                out.flush();
            } catch (IOException e) {
                String msg = "Got exception while flushing the output stream";
                DebugUtils.handleException(msg, e);
            }
        }
    }

    /**
     * Get the error stream.
     *
     * @return <CODE>OutputStream</CODE>.
     */
    protected OutputStream getErr() {
        return err;
    }

    /**
     * Get the output stream.
     *
     * @return <CODE>OutputStream</CODE>.
     */
    protected OutputStream getOut() {
        return out;
    }

    /**
     * Create the pump to handle process output.
     *
     * @param is the <CODE>InputStream</CODE>.
     * @param os the <CODE>OutputStream</CODE>.
     */
    protected void createProcessOutputPump(final InputStream is,
                                           final OutputStream os) {
        outputThread = createPump(is, os);
    }

    /**
     * Create the pump to handle error output.
     *
     * @param is the <CODE>InputStream</CODE>.
     * @param os the <CODE>OutputStream</CODE>.
     */
    protected void createProcessErrorPump(final InputStream is,
                                          final OutputStream os) {
        errorThread = createPump(is, os);
    }

    /**
     * Creates a stream pumper to copy the given input stream to the given
     * output stream.
     *
     * @param is the input stream to copy from
     * @param os the output stream to copy into
     * @return the stream pumper thread
     */
    protected Thread createPump(final InputStream is, final OutputStream os) {
        return createPump(is, os, false);
    }

    /**
     * Creates a stream pumper to copy the given input stream to the given
     * output stream.
     *
     * @param is                 the input stream to copy from
     * @param os                 the output stream to copy into
     * @param closeWhenExhausted close the output stream when the input stream is exhausted
     * @return the stream pumper thread
     */
    protected Thread createPump(final InputStream is, final OutputStream os,
                                final boolean closeWhenExhausted) {
        final Thread result = new Thread(new TailStreamPumper(is, os, closeWhenExhausted));
        result.setDaemon(true);
        return result;
    }


    /**
     * Creates a stream pumper to copy the given input stream to the given
     * output stream.
     *
     * @param is the System.in input stream to copy from
     * @param os the output stream to copy into
     * @return the stream pumper thread
     */
    private Thread createSystemInPump(InputStream is, OutputStream os) {
        inputStreamPumper = new InputStreamPumper(is, os);
        final Thread result = new Thread(inputStreamPumper);
        result.setDaemon(true);
        return result;
    }

}

