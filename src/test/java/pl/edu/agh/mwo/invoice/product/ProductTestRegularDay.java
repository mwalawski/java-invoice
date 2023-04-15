package pl.edu.agh.mwo.invoice.product;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

import static pl.edu.agh.mwo.invoice.product.FuelCanister.DATE;

public class ProductTestRegularDay {

    @Before
    public void setUpDateToRegularDay() {
        int randomDay = new Random().nextInt(27) + 1;
        int randomMonth = new Random().nextInt(11) + 1;
        randomDay = (randomDay == 5 && randomMonth == 3) ? 1 : randomDay;

        DATE = LocalDate.of(2023,randomMonth,randomDay);
    }
    @Test
    public void testBottleOfWinePriceOnRegularDay() {
        BottleOfWine product = new BottleOfWine("Riesling", new BigDecimal("90"));
        //90*1.23+5.56=116.26
        Assert.assertThat(new BigDecimal("116.26"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test
    public void testBottleOfWineOnlyExciseOnRegularDay() {
        BottleOfWine product = new BottleOfWine("Lesny dzban", BigDecimal.ZERO);
        //0*1.23+5.56=5.56
        Assert.assertThat(new BigDecimal("5.56"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test
    public void testFuelCanisterPriceOnRegularDay() {
        BottleOfWine product = new BottleOfWine("Diesel ON", new BigDecimal("40"));
        //40*1.23+5.56=54.76
        Assert.assertThat(new BigDecimal("54.76"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test
    public void testFuelCanisterOnlyExciseOnRegularDay() {
        BottleOfWine product = new BottleOfWine("Vpower Racing PB100", BigDecimal.ZERO);
        //0*1.23+5.56=5.56
        Assert.assertThat(new BigDecimal("5.56"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

}
