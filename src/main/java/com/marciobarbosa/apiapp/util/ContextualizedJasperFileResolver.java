package com.marciobarbosa.apiapp.util;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.util.FileResolver;

@Component
public class ContextualizedJasperFileResolver implements FileResolver {

    @Autowired
    private ServletContext servletContext;

    @Override
    public File resolveFile(String fileName) {
	return new File(servletContext.getRealPath("/jasper")+fileName);
    }

}
