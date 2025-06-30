package hn.shadowcore.mercadoxcontext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "hn.shadowcore.mercadoxlibrary")
public class MercadoXContextApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercadoXContextApplication.class, args);
    }

}
