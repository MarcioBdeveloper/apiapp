package com.marciobarbosa.apiapp.util;

import lombok.Data;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;

@Data
public class JRElementConfig {

    private String pattern;
    private String fontName;
    private int x;
    private int y;
    private String text;
    private boolean bold;
    private boolean removeLineWhenBlank;
    private HorizontalTextAlignEnum textHorizontallAlign;
    private VerticalTextAlignEnum verticalTextAlignEnum;
    private int width;
    private int height;
    private float fontSize;
    private float borderWidth;
    private String expression;
    private int rightIdent;
    private int leftIdent;
    private StretchTypeEnum stretchType;

    public JRElementConfig(){
   	this.stretchType = StretchTypeEnum.NO_STRETCH;
       }

       public JRElementConfig(JRElementConfig config){
   	this.fontName = config.fontName;
   	this.x = config.x;
   	this.y = config.y;
   	this.text = config.text;
   	this.bold = config.bold;
   	this.removeLineWhenBlank = config.removeLineWhenBlank;
   	this.textHorizontallAlign = config.textHorizontallAlign;
   	this.verticalTextAlignEnum = config.verticalTextAlignEnum;
   	this.width = config.width;
   	this.height = config.height;
   	this.fontSize = config.fontSize;
   	this.borderWidth = config.borderWidth;
   	this.expression = config.expression;
   	this.rightIdent = config.rightIdent;
   	this.leftIdent = config.leftIdent;
   	this.stretchType = config.stretchType;
   	this.pattern = config.pattern;
       }

}
