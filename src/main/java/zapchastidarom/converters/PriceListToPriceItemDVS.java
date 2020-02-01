package zapchastidarom.converters;

import java.util.HashMap;
import java.util.Map;
import zapchastidarom.PriceItem;

public class PriceListToPriceItemDVS implements PriceListToPriceItem {

  private Map dvs;

  public PriceListToPriceItemDVS() {
  }

  public PriceListToPriceItemDVS(String[] str) {
    Map <String, Object> map = new HashMap();
    map.put("number", str[10]);
    map.put("vendor", str[1]);
    map.put("description", str[3]);
    map.put("number", str[10]);
  }

  @Override
  public PriceItem convertToPriceItem(String[] strings) {
    String symbols = "[^a-zA-Z0-9]";

    String number = strings[10];
    String searchNumber = number
        .replaceAll(symbols, "")
        .toUpperCase();

    String vendor = strings[1];
    String searchVendor = vendor
        .replaceAll(symbols, "")
        .toUpperCase();

    int lim = 512;
    String description = strings[3].length() < lim ? strings[3] : strings[3].substring(0, lim);

    double price = 0.0;
    if (!strings[6].isEmpty()) {
      String strPrice = strings[6].replace(',', '.');
      price = Double.parseDouble(strPrice);
    }

    int count = 0;
    if (!strings[8].isEmpty()) {
      String strCount = strings[8].replaceAll("[^0-9]", "#");

      int st = strCount.lastIndexOf('#');
      count = Integer.parseInt(strCount.substring(++st));
    }

    return new PriceItem(searchNumber, vendor, number, searchVendor, description, price, count);
  }
}
