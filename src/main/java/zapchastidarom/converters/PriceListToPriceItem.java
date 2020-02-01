package zapchastidarom.converters;

import zapchastidarom.PriceItem;

public interface PriceListToPriceItem {

  PriceItem convertToPriceItem(String[] strings);
}
