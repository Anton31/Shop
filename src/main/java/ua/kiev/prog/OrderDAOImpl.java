package ua.kiev.prog;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderDAOImpl implements OrderDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(Order order) {
        entityManager.merge(order);
    }

    @Override
    public List<Order> list() {
        Query query = entityManager.createQuery("select  o from Order o", Order.class);
        return (List<Order>) query.getResultList();
    }

}
