package cloud.xuxiaowei.core.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Documented
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerAnnotation {

	/**
	 * 接口名
	 */
	String value();

	class Annotation {

		public static ControllerAnnotation get(JoinPoint joinPoint) {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();
			return method.getAnnotation(ControllerAnnotation.class);
		}

	}

}
