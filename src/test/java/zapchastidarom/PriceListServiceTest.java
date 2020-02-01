package zapchastidarom;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import zapchastidarom.converters.PriceListToPriceItem;

public class PriceListServiceTest {

  @Mock
  PriceItem item;

  @Mock
  private PriceListToPriceItem converter;

  @Mock
  private InputStreamReader reader;

  @Spy
  private PriceListService service;

  String file = "sth.csv";
  String string = "content";
  String[] expectedRow = {string, string};
  Iterable<String[]> expectedIterableRows;
  List<PriceItem> expectedItems;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    expectedIterableRows = Collections.singletonList(expectedRow);
    expectedItems = Collections.singletonList(item);
  }

  @Test
  public void processPriceList() throws FileNotFoundException, UnsupportedEncodingException {
    doReturn(reader).when(service).readFile(file);
    doReturn(expectedIterableRows).when(service).getRows(reader);
    doReturn(true).when(service).isNotEmptyRow(expectedRow);
    when(converter.convertToPriceItem(expectedRow)).thenReturn(item);

    List<PriceItem> items = service.processPriceListWithCsvReader(file, converter);

    verify(service).readFile(file);
    verify(converter).convertToPriceItem(expectedRow);
    assertEquals(expectedItems, items);

  }

  @Test
  public void processPriceListFailed() throws FileNotFoundException, UnsupportedEncodingException {
    doThrow(FileNotFoundException.class).when(service).readFile("");

    List<PriceItem> items = service.processPriceListWithCsvReader("", converter);

    assertEquals(Collections.emptyList(), items);
  }

  @Test
  public void isNotEmptyRow() {
    assertTrue(service.isNotEmptyRow(expectedRow));
  }

  @Test
  public void isEmptyRow() {
    String[] emptyRow = {"", ""};
    assertFalse(service.isNotEmptyRow(emptyRow));
  }

}