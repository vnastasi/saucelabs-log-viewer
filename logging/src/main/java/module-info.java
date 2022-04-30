module saucelabs.log.viewer.logging {

    requires org.slf4j;

    provides java.lang.System.LoggerFinder with md.vnastasi.slv.logger.Slf4jBridgeLoggerFinder;

    exports md.vnastasi.slv.logger;
}
