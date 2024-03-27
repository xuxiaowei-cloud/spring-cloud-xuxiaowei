package cloud.xuxiaowei.aop.config;

import cloud.xuxiaowei.core.annotation.ControllerAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Aspect
@Component
public class ControllerLogAspect {

	@Pointcut("execution(* cloud.xuxiaowei.*.controller.*.*(..)) ")
	public void pointcut() {

	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Instant start = Instant.now();

		ControllerAnnotation annotation = ControllerAnnotation.Annotation.get(joinPoint);
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String method = signature.getMethod().toString();
		String methodName = annotation == null ? method : annotation.value();

		log.info("环绕通知开始: {}", methodName);

		try {
			Object proceed = joinPoint.proceed();

			Instant end = Instant.now();

			Duration between = Duration.between(start, end);

			String duration = between.toString();
			long millis = between.toMillis();

			log.info("环绕通知结束: {}, 执行耗时: {}ms({})", methodName, millis, duration);

			return proceed;
		}
		catch (Exception e) {
			Instant end = Instant.now();
			Duration between = Duration.between(start, end);
			String duration = between.toString();
			long millis = between.toMillis();

			log.error("异常通知: {} 执行耗时: {}ms({})", methodName, millis, duration, e);
			throw e;
		}
	}

}
