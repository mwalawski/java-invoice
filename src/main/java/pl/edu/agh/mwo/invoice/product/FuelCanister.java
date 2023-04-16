package pl.edu.agh.mwo.invoice.product;

import java.time.LocalDate;
import java.math.BigDecimal;

public class FuelCanister extends ExciseProduct {
    private final int motherInLawDay = 3;
    private final int motherInLawMonth = 5;
    public static LocalDate DATE = LocalDate.now();

    public FuelCanister(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public BigDecimal getPriceWithTax() {
        LocalDate currentDate = DATE;
        if (currentDate.getMonthValue() == motherInLawMonth
                && currentDate.getDayOfMonth() == motherInLawDay) {
            this.excise = BigDecimal.ZERO;

        }
        return price.multiply(taxPercent).add(price).add(excise);
    }
}
