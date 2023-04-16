package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class ExciseProduct extends Product {

    protected BigDecimal excise;

    public ExciseProduct(String name, BigDecimal price) {
        super(name, price, new BigDecimal("0.23"));
        this.excise = new BigDecimal("5.56");
    }

    @Override
    public BigDecimal getPriceWithTax() {
        return price.multiply(taxPercent).add(price).add(excise);
    }
}
