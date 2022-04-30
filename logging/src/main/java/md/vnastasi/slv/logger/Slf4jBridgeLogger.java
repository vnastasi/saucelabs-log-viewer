package md.vnastasi.slv.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class Slf4jBridgeLogger implements System.Logger {

    private final String name;
    private final Logger logger;

    public Slf4jBridgeLogger(String name) {
        this.name = name;
        this.logger = LoggerFactory.getLogger(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLoggable(Level level) {
        return switch (level) {
            case ALL -> true;
            case DEBUG -> logger.isDebugEnabled();
            case INFO -> logger.isInfoEnabled();
            case ERROR -> logger.isErrorEnabled();
            case WARNING -> logger.isWarnEnabled();
            case TRACE -> logger.isTraceEnabled();
            case OFF, null -> false;
        };
    }

    @Override
    public void log(Level level, String msg) {
        doLogSimpleMessage(level, msg);
    }

    @Override
    public void log(Level level, Supplier<String> msgSupplier) {
        doLogSimpleMessage(level, msgSupplier.get());
    }

    @Override
    public void log(Level level, Object obj) {
        doLogSimpleMessage(level, Objects.toString(obj));
    }

    @Override
    public void log(Level level, String msg, Throwable thrown) {
        doLogThrowable(level, msg, thrown);
    }

    @Override
    public void log(Level level, Supplier<String> msgSupplier, Throwable thrown) {
        doLogThrowable(level, msgSupplier.get(), thrown);
    }

    @Override
    public void log(Level level, String format, Object... params) {
        doLogParameterizedMessage(level, format, params);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        doLogThrowable(level, msg, thrown);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        doLogParameterizedMessage(level, format, params);
    }

    private void doLogSimpleMessage(Level level, String message, Object... params) {
        if (!isLoggable(level)) {
            return;
        }

        switch (level) {
            case ALL:
            case TRACE:
                logger.trace(message, params);
                break;
            case DEBUG:
                logger.debug(message, params);
                break;
            case INFO:
                logger.info(message, params);
                break;
            case ERROR:
                logger.error(message, params);
                break;
            case WARNING:
                logger.warn(message, params);
                break;
            case OFF, null:
                break;
        }
    }

    private void doLogParameterizedMessage(Level level, String message, Object... params) {
        if (!isLoggable(level)) {
            return;
        }

        doLogSimpleMessage(level, MessageFormat.format(message, params));
    }

    private void doLogThrowable(Level level, String message, Throwable throwable) {
        if (!isLoggable(level)) {
            return;
        }

        switch (level) {
            case ALL:
            case TRACE:
                logger.trace(message, throwable);
                break;
            case DEBUG:
                logger.debug(message, throwable);
                break;
            case INFO:
                logger.info(message, throwable);
                break;
            case ERROR:
                logger.error(message, throwable);
                break;
            case WARNING:
                logger.warn(message, throwable);
                break;
            case OFF, null:
                break;
        }
    }
}
