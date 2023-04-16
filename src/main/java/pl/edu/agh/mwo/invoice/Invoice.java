package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private final int MAX_INVOICE_NUMBER = 9999;
    private int number;
    private Map<Product, Integer> products = new HashMap<Product, Integer>();

    public Invoice() {
        Random random = new Random();
        this.number = random.nextInt(MAX_INVOICE_NUMBER) + 1;
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        if (products.keySet().contains(product)) {
            quantity += products.get(product);
        }
        products.put(product, quantity);
    }

    public int getNumber() {
        return number;
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public void printProducts() {
        System.out.println("Numer faktury: " + getNumber());
        for (Product product:products.keySet()) {
            System.out.println(
                    product.getName() + " " + products.get(product) + " " + product.getPrice());
        }
        System.out.println("Liczba pozycji: " + products.keySet().size());
    }
}
