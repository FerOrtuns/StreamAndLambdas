package space.gavinklfong.demo.streamapi;

import space.gavinklfong.demo.streamapi.models.Product;

import java.util.Comparator;

public class ProductComparatorByPrice implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return Double.compare(p1.getPrice(), p2.getPrice());
    }
}
