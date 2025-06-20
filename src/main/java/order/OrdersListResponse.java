package order;


import java.util.List;

public class OrdersListResponse {
    private List<Order> orders;
    private PageInfo pageInfo;
    private List<AvailableStation> availableStations;


    public List<Order> getOrders() { return orders; }

    public static class PageInfo {
        private int total;
        private int limit;
        private int page;

        public int getTotal() {
            return total;
        }

        public int getLimit() {
            return limit;
        }

        public int getPage() {
            return page;
        }

    }

    public static class AvailableStation {
        private String name;
        private int number;
        private String color;

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }

        public String getColor() {
            return color;
        }
    }
}