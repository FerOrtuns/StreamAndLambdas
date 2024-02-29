package space.gavinklfong.demo.streamapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import java.time.LocalDate;
import java.util.*;

@DataJpaTest
public class TestExercises {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Test
    //@DisplayName("Lista de productos que son libros y tengan un precio mayor a 100€.")
    public void exercise1() {
        List<Product> bookList = productRepo.findAll()
                .stream()
                .filter(product -> product.getCategory().equalsIgnoreCase("Books"))
                .filter(product -> product.getPrice() > 100)
                .toList();
        System.out.println();
        System.out.println("Lista de productos que son libros y tengan un precio mayor a 100€.");
        bookList.forEach(System.out::println);
        System.out.println();
    }

    @Test
    //@DisplayName("Lista de pedidos que son cosas de bebés.")
    public void exercise2() {
        List<Order> babyOrders = orderRepo.findAll()
                .stream()
                .filter(order -> order.getProducts()
                        .stream()
                        .anyMatch(product -> product.getCategory().equalsIgnoreCase("Baby")))
                .distinct()
                .toList();

        System.out.println();
        System.out.println("Lista de pedidos que son cosas de bebés.");
        babyOrders.forEach(System.out::println);
        System.out.println();
    }

    @Test
    //@DisplayName("Lista de productos que sean juguetes con un descuento del 10%")
    public void exercise3() {
        List<Product> discountToyList = productRepo.findAll()
                .stream().filter(product -> product.getCategory().equalsIgnoreCase("Toys"))
                .map(product -> product.withPrice(product.getPrice() * 0.9))
                .toList();
        System.out.println();
        System.out.println("Lista de productos que sean juguetes con un descuento del 10%");
        discountToyList.forEach(System.out::println);
        System.out.println();
    }

    @Test
    //@DisplayName("Lista de productos ordenados por clientes de tier 2 y entre las fechas 01-02-2021 y 01-04-2021")
    public void exercise4() {
        List<Product> ordersByDatesANDConsumerTier = orderRepo.findAll()
                .stream()
                .filter(order -> order.getCustomer().getTier() == 2)
                .filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 2, 1)) && order.getOrderDate().isBefore(LocalDate.of(2021, 4, 1)))
                //.filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 2, 1)))
                //.filter(order -> order.getOrderDate().isBefore(LocalDate.of(2021, 4, 1)))
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .toList();
        System.out.println();
        System.out.println("Lista de productos ordenados por clientes de tier 2 y entre las fechas 01-02-2021 y 01-04-2021");
        ordersByDatesANDConsumerTier.forEach(System.out::println);
        System.out.println();
    }

    @Test
    //@DisplayName("El producto de tipo `libro` más barato")
    public void exercise5() {
        Optional<Product> minBook = productRepo.findAll()
                .stream()
                .filter(product -> product.getCategory().equalsIgnoreCase("Books"))
                .min(Comparator.comparing(Product::getPrice));

        System.out.println();
        System.out.println("El producto de tipo `libro` más barato");
        if (minBook.isPresent()) System.out.println(minBook.get());
        else System.out.println("No hay producto libro mas barato");
        System.out.println();
    }

    @Test
    //@DisplayName("Los 3 pedidos relizados más recientes")
    public void exercise6() {
        List<Order> recentOrders = orderRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(3)
                .toList();
        System.out.println();
        System.out.println("Los 3 pedidos realizados más recientes");
        recentOrders.forEach(System.out::println);
        System.out.println();
    }

    @Test
    //@DisplayName("Lista de productos de los pedidos que fueron realizados el 15/03/2021")
    public void exercise7() {
        System.out.println("Pedidos");
        List<Product> productListWithOrders = orderRepo.findAll()
                .stream()
                .filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
                .peek(System.out::println)
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .toList();
        System.out.println();
        System.out.println("Lista de productos de los pedidos que fueron realizados el 15/03/2021");
        productListWithOrders.forEach(System.out::println);
        System.out.println();
    }

    @Test
    //
    public void exercise8() {
        double totalSumOrdersInFeb2021 = orderRepo.findAll()
                .stream()
                .filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 2, 1)) && order.getOrderDate().isBefore(LocalDate.of(2021, 3, 1)))
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .sum();
        System.out.println();
        System.out.println("Suma total de todos los precios de los productos pedidos en el mes de febrero de 2021");
        System.out.println(totalSumOrdersInFeb2021 + "€");
        System.out.println();
    }

    @Test
    //
    public void exercise9() {
        Double averageOrdersPaymentOn14Mar2021 = orderRepo.findAll()
                .stream()
                .filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .average()
                .getAsDouble();
        System.out.println();
        System.out.println("media de los productos de los pedidos cuya fecha es el 15/03/2021");
        System.out.println(averageOrdersPaymentOn14Mar2021 + "€");
        System.out.println();
    }
}















    /*@Test
    public void filterByRangeDates() {
        LocalDate startingDate = LocalDate.of(2021, 2, 1);
        LocalDate endingDate = LocalDate.of(2021, 3, 1);
        ByRangeDatesOrderPredicate rangeDatesOrderPredicate = new ByRangeDatesOrderPredicate(startingDate, endingDate);

        List<Order> rangeDatesOrderList = new ArrayList<>();
        for (Order order : orderRepo.findAll()) {
            if (rangeDatesOrderPredicate.test(order)) {
                rangeDatesOrderList.add(order);
            }
        }
        System.out.println();
        System.out.println("Lista de pedidos entre las fechas: " + startingDate + " y " + endingDate);
        System.out.println(rangeDatesOrderList.size());
        rangeDatesOrderList.forEach(System.out::println);

    }
 */
