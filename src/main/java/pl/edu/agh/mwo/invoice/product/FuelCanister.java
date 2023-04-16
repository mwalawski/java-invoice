package pl.edu.agh.mwo.invoice.product;

import java.time.LocalDate;
import java.math.BigDecimal;

public class FuelCanister extends ExciseProduct {

    public static LocalDate DATE = LocalDate.now();

    public FuelCanister(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public BigDecimal getPriceWithTax() {
        LocalDate currentDate = DATE;
        if (currentDate.getMonthValue() == 3 && currentDate.getDayOfMonth() == 5){
            this.excise = BigDecimal.ZERO;

        }
        return price.multiply(taxPercent).add(price).add(excise);
    }
}
