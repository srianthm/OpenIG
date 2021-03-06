/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyright [year] [name of copyright owner]".
 *
 * Copyright 2010-2011 ApexIdentity Inc.
 * Portions Copyright 2011-2014 ForgeRock AS.
 */

package org.forgerock.openig.log;

import org.forgerock.openig.heap.Name;

/**
 * Wraps a log sink and exposes a set of convenience methods for various logging activities.
 */
public class Logger {

    /** The sink to write log entries to. */
    private final LogSink sink;

    /** The base source to write all log entries with. */
    private final Name source;

    /**
     * Constructs a new logger. If the supplied sink is {@code null}, then a
     * {@link NullLogSink} will be used.
     *  @param sink the sink to write log entries to.
     * @param source the base source to write all log entries with.
     */
    public Logger(LogSink sink, Name source) {
        this.sink = (sink != null ? sink : new NullLogSink());
        this.source = source;
    }

    /**
     * Logs the message at the specified log level.
     *
     * @param level the log level to set in the log entry.
     * @param message the message to be logged.
     */
    public void logMessage(LogLevel level, String message) {
        log(createEntry("log", level, message));
    }

    /**
     * Logs the specified exception.
     *
     * @param level the log level to set in the log entry.
     * @param throwable the exception to be logged.
     * @param <T> type of logged throwable
     * @return the exception being logged.
     */
    public <T extends Throwable> T logException(LogLevel level, T throwable) {
        log(createEntry("throwable", level, throwable.getMessage(), throwable));
        return throwable;
    }

    /**
     * Logs the specified message at the {@code ERROR} log level.
     *
     * @param message the message to be logged.
     */
    public void error(String message) {
        logMessage(LogLevel.ERROR, message);
    }

    /**
     * Logs the specified exception at the {@code ERROR} log level.
     *
     * @param throwable the exception to be logged.
     * @param <T> type of logged throwable
     * @return the exception being logged.
     */
    public <T extends Throwable> T error(T throwable) {
        return logException(LogLevel.ERROR, throwable);
    }

    /**
     * Logs the specified message at the {@code WARNING} log level.
     *
     * @param message the message to be logged.
     */
    public void warning(String message) {
        logMessage(LogLevel.WARNING, message);
    }

    /**
     * Logs the specified exception at the {@code WARNING} log level.
     *
     * @param throwable the exception to be logged.
     * @param <T> type of logged throwable
     * @return the exception being logged.
     */
    public <T extends Throwable> T warning(T throwable) {
        return logException(LogLevel.WARNING, throwable);
    }

    /**
     * Logs the specified message at the {@code INFO} log level.
     *
     * @param message the message to be logged.
     */
    public void info(String message) {
        logMessage(LogLevel.INFO, message);
    }

    /**
     * Logs the specified exception at the {@code INFO} log level.
     *
     * @param throwable the exception to be logged.
     * @param <T> type of logged throwable
     * @return the exception being logged.
     */
    public <T extends Throwable> T info(T throwable) {
        return logException(LogLevel.INFO, throwable);
    }

    /**
     * Logs the specified message at the {@code CONFIG} log level.
     *
     * @param message the message to be logged.
     */
    public void config(String message) {
        logMessage(LogLevel.CONFIG, message);
    }

    /**
     * Logs the specified exception at the {@code CONFIG} log level.
     *
     * @param throwable the exception to be logged.
     * @param <T> type of logged throwable
     * @return the exception being logged.
     */
    public <T extends Throwable> T config(T throwable) {
        return logException(LogLevel.CONFIG, throwable);
    }

    /**
     * Logs the specified message at the {@code DEBUG} log level.
     *
     * @param message the message to be logged.
     */
    public void debug(String message) {
        logMessage(LogLevel.DEBUG, message);
    }

    /**
     * Logs the specified exception at the {@code DEBUG} log level.
     *
     * @param throwable the exception to be logged.
     * @param <T> type of logged throwable
     * @return the exception being logged.
     */
    public <T extends Throwable> T debug(T throwable) {
        return logException(LogLevel.DEBUG, throwable);
    }

    /**
     * Logs the specified message at the {@code TRACE} log level.
     *
     * @param message the message to be logged.
     */
    public void trace(String message) {
        logMessage(LogLevel.TRACE, message);
    }

    /**
     * Logs the specified exception at the {@code TRACE} log level.
     *
     * @param throwable the exception to be logged.
     * @param <T> type of logged throwable
     * @return the exception being logged.
     */
    public <T extends Throwable> T trace(T throwable) {
        return logException(LogLevel.TRACE, throwable);
    }

    /**
     * Returns a new timer to measure elapsed time. Entries are written to the log with a
     * {@code STAT} log level.
     * @return A timer to measure elapsed time.
     */
    public LogTimer getTimer() {
        return new LogTimer(this, LogLevel.STAT);
    }

    /**
     * Returns a new timer to measure elapsed time for a specified event. The event is
     * appended to the source in hierarchical fashion. Entries are written to the log with a
     * {@code STAT} log level.
     *
     * @param event the event that is being timed.
     * @return A timer to measure elapsed time for a specified event.
     */
    public LogTimer getTimer(String event) {
        return new LogTimer(this, LogLevel.STAT, event);
    }

    /**
     * Creates a {@link LogEntry} with the given parameters and no attached data.
     * The created entry will inherit the source name of this logger.
     *
     * @param type
     *         entry type (free form tag String like {@literal log}, {@literal started} or {@literal elapsed}).
     * @param level
     *         entry's level
     * @param message
     *         entry's message
     * @return a new entry with no attached data
     */
    LogEntry createEntry(final String type, final LogLevel level, final String message) {
        return createEntry(type, level, message, null);
    }

    /**
     * Creates a {@link LogEntry} with the given parameters and attached data (possibly {@code null} data).
     * The created entry will inherit the source name of this logger.
     *
     * @param type
     *         entry type (free form tag String like {@literal log}, {@literal started} or {@literal elapsed}).
     * @param level
     *         entry's level
     * @param message
     *         entry's message
     * @param data
     *         entry's attached data
     * @return a new entry with attached data (possibly {@code null} data)
     */
    LogEntry createEntry(final String type, final LogLevel level, final String message, final Object data) {
        return new LogEntry(source, type, level, message, data);
    }

    /**
     * Logs an entry. This implementation will prepend the logger source to all log entries.
     *
     * @param entry the entry to be logged.
     */
    public void log(LogEntry entry) {
        sink.log(entry);
    }

    /**
     * Returns {@code true} if the entry may be logged, given the specified source name and log
     * level.
     *
     * @param source the source name that is intended to be logged.
     * @param level the log level of the entry to be logged.
     * @return {@code true} if the entry may be logged.
     */
    public boolean isLoggable(Name source, LogLevel level) {
        return sink.isLoggable(source, level);
    }

    /**
     * Returns {@code true} if the entry may be logged, given the source of this logger and
     * the specified log level.
     *
     * @param level the log level of the entry to be logged.
     * @return {@code true} if the entry may be logged.
     */
    public boolean isLoggable(LogLevel level) {
        return sink.isLoggable(this.source, level);
    }
}
