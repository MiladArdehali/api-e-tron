package com.milad.pi4led;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.PropertySource;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@PropertySource("classpath:swagger.properties")
//@ComponentScan(basePackageClasses = ProductsController.class)
@EnableSwagger2
public class SwaggerConfig {

    private static final String SWAGGER_API_VERSION = "1.0";
    private static final String title = "API E-Tron";
    private static final String description = "API Java pour Commande de E-tron";
    private static final String contact = "milad.ardehali@gmail.com";


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(title)
                .description(description)
                .contact(contact)
                .version(SWAGGER_API_VERSION)
                .build();
    }

    @Bean
    public Docket productsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .select()
                .paths(PathSelectors.regex( "/api.*"))
                .build();
    }



}