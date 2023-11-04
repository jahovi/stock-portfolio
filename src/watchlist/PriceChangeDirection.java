package watchlist;

/**
 * The constants in this enumeration serve to define whether a stock is falling, rising or staying
 * constant in price. This in turn decides how the stocks price cell is colored in the watchlist.
 */
public enum PriceChangeDirection {
    CONSTANT, RISING, FALLING;
}
