package pl.edu.agh.mwo.invoice.product;

import org.hamcrest.Matchers;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static pl.edu.agh.mwo.invoice.product.FuelCanister.DATE;


public class ProductTestMotherInLawDay {

    @BeforeClass
    public static void setUpDateToMotherInLawDay() {
        DATE = LocalDate.of(2023,3,5);
    }
    @Test
    public void testBottleOfWinePriceOnMotherInLawDay() {
        BottleOfWine product = new BottleOfWine("Riesling", new BigDecimal("90"));
        //90*1.23+5.56=116.26
        Assert.assertThat(new BigDecimal("116.26"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test
    public void testBottleOfWineOnlyExciseOnMotherInLawDay() {
        BottleOfWine product = new BottleOfWine("Lesny dzban", BigDecimal.ZERO);
        //0*1.23+5.56=5.56
        Assert.assertThat(new BigDecimal("5.56"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test
    public void testFuelCanisterPriceOnMotherInLawDay() {
        FuelCanister product = new FuelCanister("Diesel ON", new BigDecimal("40"));
        //40*1.23+0=49.20
//        Assert.assertEquals(new Date(DateTimeUtils.currentTimeMillis()), new Date(2023, 3, 5));
        Assert.assertThat(new BigDecimal("49.20"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test
    public void testFuelCanisterOnlyExciseOnMotherInLawDay() {
        FuelCanister product = new FuelCanister("Vpower Racing PB100", BigDecimal.ZERO);
        //0*1.23+0=0
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(product.getPriceWithTax()));
    }
}
