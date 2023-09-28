package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.memory.MemoryItemRepository;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class MemoryConfig {

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new MemoryItemRepository();
    }

    /**
     * 임베디드 모드
     * 테스트 케이스에서 사용
     * 근데 뭔가 이것도 귀찮다....
     * 스프링 부트에서는 뭔가 이것을 해결할 수 있는 방법이 있을 거 같다
     * src/test/resources/application.properties => 테스트 디렉토리에 설정파일을 확인해보자
     */
    @Bean
//    @Profile("test")
    @Profile("test2")
    public DataSource dataSource() {
        log.info("메모리 데이터베이스 초기화");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"); // 임베디드 모드(메모리 모드)로 동작하는 H2 데이터베이스를 사용할 수 있음
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

}
