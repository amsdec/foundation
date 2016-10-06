package io.carvill.foundation.data;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "io.carvill.foundation.data")
@EnableTransactionManagement(proxyTargetClass = true)
public class TestConfiguration {

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:test.sql").build();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(this.getDataSource());
        entityManagerFactoryBean.setPackagesToScan("io.carvill.foundation.data");

        final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(false);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");

        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        return entityManagerFactoryBean;
    }

    // private DatabasePopulator createDatabasePopulator() {
    // ResourceDatabasePopulator databasePopulator = new
    // ResourceDatabasePopulator();
    // databasePopulator.setContinueOnError(true);
    // databasePopulator.addScript(new ClassPathResource("schema.sql"));
    // return databasePopulator;
    // }

}
