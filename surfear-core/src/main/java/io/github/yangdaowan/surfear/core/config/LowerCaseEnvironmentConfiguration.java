package io.github.yangdaowan.surfear.core.config;

import io.github.yangdaowan.surfear.core.util.MessageConstant;
import org.apache.commons.configuration2.MapConfiguration;

import java.util.HashMap;

/**
 * @author ycf
 **/
public class LowerCaseEnvironmentConfiguration extends MapConfiguration {

    private static final HashMap<String, String> snapshot = init();

    public LowerCaseEnvironmentConfiguration() {
        super(snapshot);
    }

    private static HashMap<String, String> init(){
        HashMap<String, String> map = new HashMap<>();
        System.getenv().forEach((k, v) -> {
            if(k.toLowerCase().startsWith(MessageConstant.base)){
                String envK = envToDotNotation(k);
                map.put(envK, v);
            }
        });
        return map;
    }

    /**
     * 将环境变量的大写下划线转为小写点分隔
     */
    public static String envToDotNotation(String envKey) {
        return envKey.toLowerCase().replace('_', '.');
    }

    protected void addPropertyDirect(String key, Object value) {
        throw new UnsupportedOperationException("LowerCaseEnvironmentConfiguration is read-only!");
    }

    protected void clearInternal() {
        throw new UnsupportedOperationException("LowerCaseEnvironmentConfiguration is read-only!");
    }

    protected void clearPropertyDirect(String key) {
        throw new UnsupportedOperationException("LowerCaseEnvironmentConfiguration is read-only!");
    }
}
