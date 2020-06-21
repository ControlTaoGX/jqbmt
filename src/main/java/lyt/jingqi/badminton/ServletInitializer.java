package lyt.jingqi.badminton;

import lyt.jingqi.badminton.entity.JqBadmintonOrderItem;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        //Application的类名
        return application.sources(JqBadmintonApplication.class);
    }
}
