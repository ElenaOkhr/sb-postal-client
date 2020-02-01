package zapchastidarom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PriceItem {

  @Id
  @GeneratedValue
  private long id;

  @Column(length = 64)
  private String searchNumber;

  @Column(length = 64)
  private String vendor;

  @Column(length = 64)
  private String number;

  @Column(length = 64)
  private String searchVendor;

  @Column(length = 512)
  private String description;

  @Column(columnDefinition="Decimal(18,2)")
  private double price;

  private int count;

  public PriceItem() {
  }

  public PriceItem(String searchNumber, String vendor, String number, String searchVendor, String description,
      double price, int count) {
    this.searchNumber = searchNumber;
    this.vendor = vendor;
    this.number = number;
    this.searchVendor = searchVendor;
    this.description = description;
    this.price = price;
    this.count = count;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getSearchNumber() {
    return searchNumber;
  }

  public void setSearchNumber(String searchNumber) {
    this.searchNumber = searchNumber;
  }

  public String getVendor() {
    return vendor;
  }

  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getSearchVendor() {
    return searchVendor;
  }

  public void setSearchVendor(String searchVendor) {
    this.searchVendor = searchVendor;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  @Override
  public String toString() {
    return "PriceItem{" +
        "searchNumber='" + searchNumber + '\'' +
        ", vendor='" + vendor + '\'' +
        ", number='" + number + '\'' +
        ", searchVendor='" + searchVendor + '\'' +
        ", description='" + description + '\'' +
        ", price=" + price +
        ", count=" + count +
        '}';
  }
}
