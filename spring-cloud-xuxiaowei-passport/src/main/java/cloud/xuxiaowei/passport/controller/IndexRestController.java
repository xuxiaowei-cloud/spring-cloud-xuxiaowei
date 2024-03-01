package cloud.xuxiaowei.passport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@RestController
public class IndexRestController {

	@RequestMapping
	public Map<String, Object> index(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		return map;
	}

}
