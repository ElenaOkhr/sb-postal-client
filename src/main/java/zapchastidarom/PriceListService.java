package zapchastidarom;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import zapchastidarom.converters.PriceListToPriceItem;

//TODO: try other methods of parsing (mapToEntity, Spring-magic-ways and other...)
public class PriceListService {

  public List<PriceItem> processPriceListWithCsvReader(String file, PriceListToPriceItem converter) {
    try (InputStreamReader reader = readFile(file)) {

      Iterable<String[]> rows = getRows(reader);

      return StreamSupport.stream(rows.spliterator(), false)
          .filter(this::isNotEmptyRow)
          .map(converter::convertToPriceItem)
          .collect(Collectors.toList());

    } catch (UnsupportedEncodingException e) {
      System.out.println("Default encoding is windows-1251 " + e.toString());

    } catch (FileNotFoundException e) {
      System.out.println("File is missing or damaged " + e.toString());

    } catch (IOException e) {
      System.out.println(e.toString());
    }
    return Collections.emptyList();
  }

  Iterable<String[]> getRows(InputStreamReader reader) {
    CSVParser parser = new CSVParserBuilder()
        .withSeparator(';')
        .withIgnoreLeadingWhiteSpace(true)
        .build();

    return () -> new CSVReaderBuilder(reader)
        .withCSVParser(parser)
        .withSkipLines(1)
        .build()
        .iterator();
  }

  boolean isNotEmptyRow(String[] strings) {
    for (String s : strings) {
      if (!s.isEmpty()) {
        return true;
      }
    }
    return false;
  }

  InputStreamReader readFile(String file) throws FileNotFoundException, UnsupportedEncodingException {
    return new InputStreamReader(new FileInputStream(file), "windows-1251");
  }
}
