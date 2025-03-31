package com.ruoyi.web.domain;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


public class ClipSealRequest implements Serializable {

    private static final long serialVersionUID = 7532064967450524188L;


    private String image ;


    private Integer colorRange ;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getColorRange() {
        return colorRange;
    }

    public void setColorRange(Integer colorRange) {
        this.colorRange = colorRange;
    }

    public ClipSealRequest(String image, Integer colorRange) {
        this.image = image;
        this.colorRange = colorRange;
    }

    public ClipSealRequest() {
    }
}
