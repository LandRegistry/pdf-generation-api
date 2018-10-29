package pdfgenerationapi.utils;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * This class provides a Marker object that can be passed to the SLF4J logger.
 * The logging configuration will then ensure that the log is logged as an audit log,
 * not a normal log message. Always use info level to ensure it does not get filtered out.
 *
 * Usage:
 *
 * import static pdfgenerationapi.utils.AuditMarker.auditMarker;
 * ...
 * logger.info(auditMarker, "This is an audit point");
 */
public final class AuditMarker {
    public static final Marker auditMarker = MarkerFactory.getMarker("AUDIT");
}


