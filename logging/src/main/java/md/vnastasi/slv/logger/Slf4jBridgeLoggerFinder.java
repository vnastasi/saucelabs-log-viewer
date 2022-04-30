package md.vnastasi.slv.logger;

public class Slf4jBridgeLoggerFinder extends System.LoggerFinder {

    @Override
    public System.Logger getLogger(String name, Module module) {
        return new Slf4jBridgeLogger(name);
    }
}
