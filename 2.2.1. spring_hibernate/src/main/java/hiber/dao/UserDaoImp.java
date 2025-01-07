package hiber.dao;

import hiber.model.User;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImp implements UserDao {

   private final SessionFactory sessionFactory;

   @Autowired
   public UserDaoImp(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query= (TypedQuery<User>) sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public Optional<User> getUserByCarModelAndSeries(String model, int series) {
      String hql = "FROM User u "
              + "JOIN FETCH u.car c "
              + "WHERE c.model = :model AND c.series = :series";

      Session session = sessionFactory.getCurrentSession();
      Query query = session.createQuery(hql, User.class)
              .setParameter("model", model)
              .setParameter("series", series);

      List<User> resultList = query.getResultList();

      if (!resultList.isEmpty()) {
         return Optional.of(resultList.get(0));
      }
      return Optional.empty();
   }

}
