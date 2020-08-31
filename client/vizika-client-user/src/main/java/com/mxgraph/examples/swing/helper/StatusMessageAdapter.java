package com.mxgraph.examples.swing.helper;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;

public class StatusMessageAdapter extends AppenderSkeleton {
    private final JTextArea jTextA;

    public StatusMessageAdapter(JTextArea jTextA) {
        this.jTextA = jTextA;
    }

    protected void append(LoggingEvent event)
    {
        if(event.getLevel().equals(Level.INFO)){
            jTextA.append("INFO > " + event.getLoggerName() + ": " + event.getMessage().toString().concat("\n"));
        }
        if (event.getLevel().equals(Level.WARN)) {
            jTextA.append("WARN > " + event.getLoggerName() + ": " + event.getMessage().toString().concat("\n"));
        }
    }
    public void close() {
    }
    public boolean requiresLayout()
    {
        return false;
    }
}
