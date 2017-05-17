package andrewhossam.se3reldollar.Objects;

import java.util.Comparator;

/**
 * Created by Andrew on 1/1/2017.
 */

public class BankNameComparator implements Comparator<CurrencyObject> {
    @Override
    public int compare(CurrencyObject o1, CurrencyObject o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
