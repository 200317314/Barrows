package core.ge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GrandExchancePrice {

    private static final String BASE_URL = "http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=";
    private static final int MILLION = 1000000;
    private static final int THOUSAND = 1000;

    // get price from GE
    public static int getPrice(final int id) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(BASE_URL + id).openStream()))) {
            final String raw = reader.readLine().replace(",", "").replace("\"", "").split("price:")[1].split("}")[0];
            return raw.endsWith("m") || raw.endsWith("k") ? (int) (Double.parseDouble(raw.substring(0, raw.length() - 1))
                    * (raw.endsWith("m") ? MILLION : THOUSAND)) : Integer.parseInt(raw);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
