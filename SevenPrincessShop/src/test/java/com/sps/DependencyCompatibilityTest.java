package com.sps;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

public class DependencyCompatibilityTest {
    public interface ProbeMapper {
        @Select("SELECT 1")
        int probe();
    }

    @Test
    public void loggingUsesLog4j2() {
        assertEquals("org.apache.logging.slf4j.Log4jLoggerFactory",
                LoggerFactory.getILoggerFactory().getClass().getName());
        LoggerFactory.getLogger(DependencyCompatibilityTest.class).info("Logging compatibility check");
    }

    @Test
    public void jdbcAndMyBatisSpringCanQueryTestDatabase() throws Exception {
        String url = System.getenv("MH_CI_JDBC_URL");
        assumeTrue("Requires isolated CI database", url != null && !url.isEmpty());
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(System.getenv("MH_CI_DB_USER"));
        dataSource.setPassword(System.getenv("MH_CI_DB_PASSWORD"));

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.afterPropertiesSet();
        org.apache.ibatis.session.SqlSessionFactory factory = factoryBean.getObject();
        assertNotNull(factory);
        factory.getConfiguration().addMapper(ProbeMapper.class);
        try (SqlSession session = factory.openSession()) {
            assertEquals(1, session.getMapper(ProbeMapper.class).probe());
        }
    }
}
