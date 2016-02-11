package com.sastraxi.scxlcbo.model;

import java.math.BigDecimal;

/**
 * Represents a beer at the local LCBO.
 */
public class Beer {

    private static final BigDecimal fromCents = new BigDecimal("100.00");

    private Long productNumber;
    private String name;
    private String style;
    private String tastingNote;
    private String imageURI;
    private Long cadCents;
    private Long mL;

    public Beer() { }

    public Beer(Long productNumber, String name, String style, String tastingNote, String imageURI, Long cadCents, Long mL) {
        this.productNumber = productNumber;
        this.name = name;
        this.style = style;
        this.imageURI = imageURI;
        this.tastingNote = tastingNote;
        this.cadCents = cadCents;
        this.mL = mL;
    }

    public long getProductNumber() {
        return productNumber;
    }

    public String getName() {
        return name;
    }

    public String getStyle() {
        return style;
    }

    public String getTastingNote() {
        return tastingNote;
    }

    public String getImageURI() {
        return imageURI;
    }

    public BigDecimal getCadPrice() {
        return new BigDecimal(cadCents).divide(fromCents);
    }

    public long getmL() {
        return mL;
    }

    public long getCadCents() {
        return cadCents;
    }

    public void setProductNumber(long productNumber) {
        this.productNumber = productNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setTastingNote(String tastingNote) {
        this.tastingNote = tastingNote;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public void setCadCents(Long cadCents) {
        this.cadCents = cadCents;
    }

    public void setmL(Long mL) {
        this.mL = mL;
    }

}
