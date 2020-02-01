package zapchastidarom;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class PriceItemRepository {

  private EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistence-unit");
  private EntityManager entityManager;

  public PriceItemRepository() {
    entityManager = factory.createEntityManager();
  }

  public PriceItem get(PriceItem item) {
    return entityManager.find(PriceItem.class, item.getId());
  }

  public List getAll() {
    Query query = entityManager.createQuery("SELECT e FROM PriceItem e");
    return query.getResultList();
  }

  public PriceItem create(PriceItem item) {
    entityManager.getTransaction().begin();
    entityManager.persist(item);
    entityManager.getTransaction().commit();
    return item;
  }

  public void createMany(List<PriceItem> list) {
    list.forEach(this::create);
  }

  public PriceItem update(PriceItem item) {
    entityManager.getTransaction().begin();
    PriceItem updatedItem = entityManager.merge(item);
    entityManager.getTransaction().commit();
    return updatedItem;
  }

  public void delete(PriceItem item) {
    entityManager.getTransaction().begin();
    entityManager.remove(item);
    entityManager.getTransaction().commit();
  }

  public void close() {
    entityManager.close();
  }
}
