package io.github.yangdaowan.surfear.core.interceptor.support.template.enums;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum TemplateMode {

    HTML, TXT, JSON, MD;

    public static String of(String suffix){
        if(suffix.equalsIgnoreCase(HTML.name())){
            return HTML.name().toLowerCase();
        }

        if(suffix.equalsIgnoreCase(TXT.name()) || suffix.equalsIgnoreCase(JSON.name()) || suffix.equalsIgnoreCase(MD.name())){
            return TXT.name().toLowerCase();
        }

        log.warn("未支持的模板模式："+suffix);

        return null;
    }

}
