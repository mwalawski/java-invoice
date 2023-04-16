package pl.edu.agh.mwo.invoice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.mwo.invoice.product.*;

public class InvoiceTest {
    private Invoice invoice;
    private PrintStream standardOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }
    @Before
    public void setUpStream() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasNumber() {
        Assert.assertThat(invoice.getNumber(), Matchers.instanceOf(int.class));
    }

    @Test
    public void testInvoiceNumberIsBiggerThanZero() {
        Assert.assertThat(invoice.getNumber(), Matchers.greaterThanOrEqualTo(1));
    }

    @Test
    public void testInvoiceNumberIsLessOrEqualTenThousand() {
        Assert.assertThat(invoice.getNumber(), Matchers.lessThanOrEqualTo(10000));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void testPrintingEmptyInvoiceInvoiceNumber() {
        invoice.printProducts();
        List<String> outputLines = Arrays.asList(outputStreamCaptor.toString()
                .trim()
                .split(System.getProperty("line.separator")));

        Assert.assertThat(outputLines.get(0), Matchers.containsString("Numer faktury:"));
    }

    @Test
    public void testPrintingEmptyInvoiceProductCount() {
        invoice.printProducts();
        List<String> outputLines = Arrays.asList(outputStreamCaptor.toString()
                .trim()
                .split(System.getProperty("line.separator")));

        Assert.assertThat(outputLines.get(1), Matchers.comparesEqualTo("Liczba pozycji: 0"));
    }

    @Test
    public void testPrintingOneProductInvoiceNumber() {
        invoice.addProduct(new OtherProduct("Chlebek tostowy", new BigDecimal("3.15")), 2);
        invoice.printProducts();
        List<String> outputLines = Arrays.asList(outputStreamCaptor.toString()
                .trim()
                .split(System.getProperty("line.separator")));

        Assert.assertThat(outputLines.get(0), Matchers.containsString("Numer faktury:"));
    }

    @Test
    public void testPrintingOneProductListProduct() {
        invoice.addProduct(new OtherProduct("Kopytko", new BigDecimal("7.77")), 7);
        invoice.printProducts();
        List<String> outputLines = Arrays.asList(outputStreamCaptor.toString()
                .trim()
                .split(System.getProperty("line.separator")));

        Assert.assertThat(outputLines.get(1), Matchers.comparesEqualTo("Kopytko 7 7.77"));
    }

    @Test
    public void testPrintingOneProductCount() {
        invoice.addProduct(new OtherProduct("Masełko", new BigDecimal("5.00")), 4);
        invoice.printProducts();
        List<String> outputLines = Arrays.asList(outputStreamCaptor.toString()
                .trim()
                .split(System.getProperty("line.separator")));

        Assert.assertThat(outputLines.get(2), Matchers.comparesEqualTo("Liczba pozycji: 1"));
    }

    @Test
    public void testPrintingTwoTheSameProductsSamePriceListProduct() {
        OtherProduct sampleProduct = new OtherProduct("Kopytko", new BigDecimal("7.77"));
        invoice.addProduct(sampleProduct, 7);
        invoice.addProduct(sampleProduct, 5);
        invoice.printProducts();
        List<String> outputLines = Arrays.asList(outputStreamCaptor.toString()
                .trim()
                .split(System.getProperty("line.separator")));

        Assert.assertThat(outputLines.get(1), Matchers.comparesEqualTo("Kopytko 12 7.77"));
    }

    @Test
    public void testPrintingTwoTheSameProductsSamePriceProductCount() {
        OtherProduct sampleProduct = new OtherProduct("Makaron", new BigDecimal("3.23"));
        invoice.addProduct(sampleProduct, 2);
        invoice.addProduct(sampleProduct, 4);
        invoice.printProducts();
        List<String> outputLines = Arrays.asList(outputStreamCaptor.toString()
                .trim()
                .split(System.getProperty("line.separator")));

        Assert.assertThat(outputLines.get(2), Matchers.comparesEqualTo("Liczba pozycji: 1"));
    }

    @Test
    public void testPrintingTwoTheSameProductsSamePriceOtherInstancesListProduct() {
        invoice.addProduct(new OtherProduct("Zupka chinska", new BigDecimal("1.50")), 5);
        invoice.addProduct(new OtherProduct("Zupka chinska", new BigDecimal("1.50")), 1);
        invoice.printProducts();
        List<String> outputLines = Arrays.asList(outputStreamCaptor.toString()
                .trim()
                .split(System.getProperty("line.separator")));

        Assert.assertTrue(outputLines.contains("Zupka chinska 1 1.50"));
        Assert.assertTrue(outputLines.contains("Zupka chinska 5 1.50"));
    }

    @Test
    public void testPrintingTwoTheSameProductsSamePriceOtherInstancesProductCount() {
        invoice.addProduct(new OtherProduct("Soczek", new BigDecimal("4.52")), 3);
        invoice.addProduct(new OtherProduct("Soczek", new BigDecimal("4.52")), 7);
        invoice.printProducts();
        List<String> outputLines = Arrays.asList(outputStreamCaptor.toString()
                .trim()
                .split(System.getProperty("line.separator")));

        Assert.assertThat(outputLines.get(3), Matchers.comparesEqualTo("Liczba pozycji: 2"));
    }

    @Test
    public void testPrintingMultipleProductsListProducts() {
        invoice.addProduct(new OtherProduct("Szyneczka", new BigDecimal("3.15")), 5);
        invoice.addProduct(new DairyProduct("Serek", new BigDecimal("2.33")), 2);
        invoice.printProducts();
        List<String> outputLines = Arrays.asList(outputStreamCaptor.toString()
                .trim()
                .split(System.getProperty("line.separator")));

        Assert.assertTrue(outputLines.contains("Szyneczka 5 3.15"));
        Assert.assertTrue(outputLines.contains("Serek 2 2.33"));
    }


    @Test
    public void testPrintingMultipleProductsCount() {
        invoice.addProduct(new OtherProduct("Ketchup", new BigDecimal("6.66")), 1);
        invoice.addProduct(new DairyProduct("Majonez", new BigDecimal("7.22")), 2);
        invoice.printProducts();
        List<String> outputLines = Arrays.asList(outputStreamCaptor.toString()
                .trim()
                .split(System.getProperty("line.separator")));

        Assert.assertThat(outputLines.get(3), Matchers.comparesEqualTo("Liczba pozycji: 2"));
    }

    @After
    public void tearDown() {
        System.setOut(standardOut);
    }
}
