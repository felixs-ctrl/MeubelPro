package meubelpro.config;

import meubelpro.model.Produk;
import meubelpro.model.BahanBaku;
import meubelpro.model.Admin;
import meubelpro.model.RencanaProduksi;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "meubelpro")
@org.springframework.context.annotation.Configuration
public class SpringConfig {

    @Bean(destroyMethod = "close")
    public SessionFactory sessionFactory() {

        return new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Produk.class)
                .addAnnotatedClass(BahanBaku.class)
                .addAnnotatedClass(Admin.class)
                .addAnnotatedClass(RencanaProduksi.class)
                .buildSessionFactory();
    }
}
