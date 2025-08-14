package io.github.yangdaowan.surfear.core.openapi.bean;

import lombok.Data;

@Data
public abstract class FileUpload {

   public abstract void uploadPart(Object builder);

}
