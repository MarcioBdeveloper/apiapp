package com.marciobarbosa.apiapp.util;

import lombok.Data;
import net.sf.jasperreports.engine.JasperReport;

@Data
public class RelatorioPorFonteAnualBundle {

    private JasperReport mainReport;

    private JasperReport compiledDetailSectionSubreport;

}
