package summer.mrplaylist.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 적용 위치
@Retention(RetentionPolicy.RUNTIME) // 유효 시점
public @interface CurrentUser {
}
