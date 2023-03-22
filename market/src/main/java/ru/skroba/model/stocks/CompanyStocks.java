package ru.skroba.model.stocks;

public record CompanyStocks(String companyName, double price, long count) {
    public CompanyStocks sellStocks(long count) {
        return new CompanyStocks(companyName, price * PriceGenerator.sell(count), count + count());
    }
    
    public CompanyStocks buyStocks(long count) {
        return new CompanyStocks(companyName, price * PriceGenerator.bought(count), count() - count);
    }
    
    public CompanyStocks randomUpdatePrice() {
        return new CompanyStocks(companyName, price * PriceGenerator.randomUpdate(), count);
    }
    
    private static class PriceGenerator {
        static double sell(long count) {
            return Math.random() * 0.25 * Math.pow(0.5, count) + 0.75;
        }
        
        static double bought(long count) {
            return Math.random() * 0.5 * Math.pow(1.115, count) + 1;
        }
        
        static double randomUpdate() {
            return Math.random() * 0.4 + 0.75;
        }
    }
}
