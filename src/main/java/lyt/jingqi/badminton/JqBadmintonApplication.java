package lyt.jingqi.badminton;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("lyt.jingqi.badminton.dao")
@SpringBootApplication
public class JqBadmintonApplication {

    public static void main(String[] args) {
        SpringApplication.run(JqBadmintonApplication.class, args);
    }

}
