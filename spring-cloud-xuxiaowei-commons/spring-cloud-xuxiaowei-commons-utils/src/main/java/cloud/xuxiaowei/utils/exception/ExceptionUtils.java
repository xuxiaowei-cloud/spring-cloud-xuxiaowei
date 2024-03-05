package cloud.xuxiaowei.utils.exception;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class ExceptionUtils {

	/**
	 * 以 String 形式从 Throwable 中获取堆栈信息。
	 * @param throwable 要检查的 {@link Throwable}, 可能为 null
	 * @see org.apache.commons.lang3.exception.ExceptionUtils#getStackTrace(Throwable)
	 * @return 返回 String 形式的堆栈信息
	 */
	public static String getStackTrace(final Throwable throwable) {
		if (throwable == null) {
			return StringUtils.EMPTY;
		}
		final StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw, true));
		return sw.toString();
	}

}
