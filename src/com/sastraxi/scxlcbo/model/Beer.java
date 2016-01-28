package com.sastraxi.scxlcbo.model;

import java.math.BigDecimal;

/**
 * Represents a beer at the local LCBO.
 */
public class Beer {

    private static final BigDecimal fromCents = new BigDecimal("100.00");

    private final int productNumber;
    private final String name;
    private final String style;
    private final String tastingNote;
    private final String imageURI;
    private final int cadCents;
    private final int mL;

    public Beer(int productNumber, String name, String style, String tastingNote, String imageURI, int cadCents, int mL) {
        this.productNumber = productNumber;
        this.name = name;
        this.style = style;
        this.imageURI = imageURI;
        this.tastingNote = tastingNote;
        this.cadCents = cadCents;
        this.mL = mL;
    }

    public int getProductNumber() {
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

    public int getmL() {
        return mL;
    }

    public int getCadCents() {
        return cadCents;
    }
}
