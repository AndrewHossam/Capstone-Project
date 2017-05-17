package andrewhossam.se3reldollar.Objects;

import java.util.Comparator;

public class BuyComparator implements Comparator<CurrencyObject> {

    @Override
    public int compare(CurrencyObject o1, CurrencyObject o2) {
        return Double.compare(o1.buy, o2.buy);
    }
}
