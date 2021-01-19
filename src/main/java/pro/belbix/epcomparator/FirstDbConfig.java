package pro.belbix.epcomparator;

import static pro.belbix.epcomparator.DbConfigAbstract.createEntityManager;

import java.util.HashMap;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = {"pro.belbix.epcomparator.repositories1"},
    entityManagerFactoryRef = "em1",
    transactionManagerRef = "tm1"
)
public class FirstDbConfig {

    @Autowired
    private Environment env;

    @Bean("em1")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManager() {
        return createEntityManager(dataSource(), "pro.belbix.epcomparator");
    }

    @Primary
    @Bean("ds1")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("db1.datasource.driver-class-name")));
        dataSource.setUrl(env.getProperty("db1.datasource.jdbcUrl"));
        dataSource.setUsername(env.getProperty("db1.datasource.username"));
        dataSource.setPassword(env.getProperty("db1.datasource.password"));
        return dataSource;
    }

    @Primary
    @Bean("tm1")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager().getObject());
        return transactionManager;
    }
}
