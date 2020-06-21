package lyt.jingqi.badminton;

import lyt.jingqi.badminton.entity.IndexConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BadmintonApplicationTests {

    @Test
    void contextLoads() {

    }
    @Test
    void test1(){
        IndexConfig indexConfig=new IndexConfig();
        indexConfig.toString();
    }

}
