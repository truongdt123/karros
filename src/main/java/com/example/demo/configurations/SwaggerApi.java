package com.example.demo.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.example.demo.gpxdata.GpxType;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.xml.sax.SAXException;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerApi {
	@Bean
	public Docket demoApi() {
	    return new Docket(DocumentationType.SWAGGER_2)//<3>
	            .select()//<4>
	            .apis(RequestHandlerSelectors.any())//<5>
	            .paths(Predicates.not(PathSelectors.regex("/error.*")))//<6>, regex must be in double quotes.
	            .build();
	}
	@Value("${gpx.xsd.file}")
	  Resource xsdFile;

	  @Bean("gpxJaxbContext")
	  JAXBContext jaxbContext() throws JAXBException {
	    return JAXBContext.newInstance(GpxType.class);
	  }
	  
	  @Bean("unmarshaller")
	  Unmarshaller jaxbUnmarshaller() throws SAXException, IOException, JAXBException {
	    //Create Unmarshaller
	    Unmarshaller jaxbUnmarshaller = jaxbContext().createUnmarshaller();
	     
	    //Setup schema validator
	    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    Schema gpxSchema = sf.newSchema(xsdFile.getFile());
	    jaxbUnmarshaller.setSchema(gpxSchema);
	    return jaxbUnmarshaller;
	  }

}
