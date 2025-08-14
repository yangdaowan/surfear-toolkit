package io.github.yangdaowan.surfear.core.spi.client;

import io.github.yangdaowan.surfear.core.exception.ConfigException;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

/**
 * @author ycf
 **/
@NoArgsConstructor
public abstract class AbsConfig implements Config {

    /**
     * 获取唯一配置项
     * @return
     */
    public String uniqueConfigurationItem(){
        Class<?> clazz = this.getClass();
        String uniqueKey = null;
        int count = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Unique.class)) {
                if(count == 0){
                    field.setAccessible(true);
                    try {
                        Object value = field.get(this);
                        if (value == null) {
                            throw new ConfigException( "唯一配置项不能为空");
                        }
                        uniqueKey = value.toString();
                    } catch (IllegalAccessException e) {
                        throw new ConfigException("当前的安全管理器不允许对私有成员进行访问", e);
                    }
                }

                count++;
            }
        }

        if (count > 1) {
            throw new ConfigException("配置对象失败：注解 @Unique 只能在类中出现一次！");
        } else if (count == 0) {
            throw new ConfigException("配置对象失败：类中必须有一个字段使用 @Unique 注解");
        }
        return uniqueKey;
    }
}
