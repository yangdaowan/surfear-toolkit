package io.github.yangdaowan.surfear.im.wecom.framework.model.msg.featured;

import io.github.yangdaowan.surfear.core.spi.client.Config;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.custom.CustomRobotMsg;

/**
 * @author ycf
 **/
public interface Auto<T extends CustomRobotMsg> {

    T auto(Config config);
}
