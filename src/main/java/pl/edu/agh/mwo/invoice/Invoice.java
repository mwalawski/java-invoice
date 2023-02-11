package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.BiFunction;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private HashMap<Product, Integer> products = new HashMap<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
            products.put(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0){
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        products.put(product, quantity);
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            subtotal = subtotal.add(product.getPrice().multiply(new BigDecimal(products.get(product))));
        }

        return subtotal;
    }

    public BigDecimal getTax() {
        BigDecimal tax = new BigDecimal(0);
        for (Product product : products.keySet()) {
            tax = tax.add(product.getPrice().multiply(product.getTaxPercent()));
        }
        return tax;
    }

    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        for (Product product : products.keySet()) {
            BigDecimal price = product.getPrice();
            BigDecimal priceWithTax = price.add(price.multiply(product.getTaxPercent()));
            total = total.add(priceWithTax.multiply(new BigDecimal(products.get(product))));
        }
        return total;
    }
}
