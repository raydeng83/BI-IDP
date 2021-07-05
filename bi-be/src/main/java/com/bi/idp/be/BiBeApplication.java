package com.bi.idp.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Locale;
import java.util.TimeZone;

@EntityScan("com.bi.idp.be.model")
@SpringBootApplication
public class BiBeApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UCT"));

        SpringApplication.run(BiBeApplication.class, args);
    }

}
