package zapchastidarom;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import zapchastidarom.converters.PriceListToPriceItemDVS;
import zapchastidarom.mail.MailService;

//TODO: rewrite as Spring web-app
//TODO: replace sout with logger
public class App {

  private static PriceItemRepository priceItemRepository = new PriceItemRepository();
  private static PriceListService priceListService = new PriceListService();

  private static String[] providers = {"1. OOO Dostavim v srock", "2. ..."};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    System.out.println("Enter username and password");
    String username = sc.nextLine();
    String password = sc.nextLine();

    MailService mailService = new MailService(MailService.GMAIL_HOST, MailService.DEF_PORT, username, password);

    System.out.println("Looking for the latest attachment...");
    String fileName = mailService.findLatestAttachment();

    if (!fileName.isEmpty()) {

      System.out.println("Choose provider for " + fileName);
      for (String p : providers) {
        System.out.println(p);
      }

      int provider = sc.nextInt();

      List<PriceItem> items = processFileByProvider(fileName, provider);
      priceItemRepository.createMany(items);

      System.out.println(items.size() + " was added");
    }
    priceItemRepository.close();
  }

  private static List<PriceItem> processFileByProvider(String fileName, int provider) {
    List<PriceItem> items = new ArrayList<>();

    if (provider == 1) {
      items = priceListService.processPriceListWithCsvReader(fileName, new PriceListToPriceItemDVS());

    }
    else {
      System.out.println("Unknown Provider. Please, fill the new price list form");
    }
    return items;
  }
}
