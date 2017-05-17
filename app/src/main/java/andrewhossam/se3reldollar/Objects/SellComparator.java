package andrewhossam.se3reldollar.Objects;

import java.util.Comparator;

public class SellComparator implements Comparator<CurrencyObject> {

    @Override
    public int compare(CurrencyObject o1, CurrencyObject o2) {
        return Double.compare(o1.sell, o2.sell);
    }
}
