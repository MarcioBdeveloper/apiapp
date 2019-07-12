package com.marciobarbosa.apiapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.marciobarbosa.apiapp.util.ContextualizedJasperFileResolver;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 01/06/2019
 *
 * */

@EnableSwagger2
@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class ApiappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiappApplication.class, args);
	}
	
	@Autowired
    private ContextualizedJasperFileResolver fileResolver;
	
	@Autowired
    private LocalJasperReportsContext reportContext;
	
	@Bean
	public LocalJasperReportsContext getJasperLocalContext() {

		LocalJasperReportsContext ctx = new LocalJasperReportsContext(DefaultJasperReportsContext.getInstance());
		ctx.setFileResolver(fileResolver);

		return ctx;
	}
	
	@Bean
	public JasperFillManager contextualizedFillManager() {
		return JasperFillManager.getInstance(reportContext);
	}

	@Bean
	public JasperExportManager contextualizedExportManager() {
		return JasperExportManager.getInstance(reportContext);
	}

	@Bean
	public JRXlsxExporter contextualizedXlsExporter() {
		return new JRXlsxExporter(reportContext);
	}
	
	@Bean
    public Docket swagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
