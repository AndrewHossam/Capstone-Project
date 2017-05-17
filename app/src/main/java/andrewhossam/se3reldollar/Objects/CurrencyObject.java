package andrewhossam.se3reldollar.Objects;

/**
 * Created by Andrew on 12/31/2016.
 */

public class CurrencyObject {
    String name;
    double sell;
    double buy;
    String lastUpDate;


    public CurrencyObject(String name, double sell, double buy, String lastUpDate) {
        this.name = name;
        this.sell = sell;
        this.buy = buy;
        this.lastUpDate = lastUpDate;
    }

    public String getName() {
        return name;
    }

    public String getSell() {
        return String.valueOf(sell);
    }

    public String getBuy() {
        return String.valueOf(buy);
    }

    public String getLastUpDate() {
        return lastUpDate;
    }
}

