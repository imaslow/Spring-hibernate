package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void addUser(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> showUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public User findCar(String model, int series) {
      TypedQuery<Car> carQuery = sessionFactory.getCurrentSession().createQuery("from Car where model = :model and series = :series")
              .setParameter("model", model)
              .setParameter("series", series);
      List<Car> carList = carQuery.getResultList();
      if (!carList.isEmpty()) {
         Car car = carList.get(0);
         List<User> listUser = showUsers();
         User user = listUser.stream()
                 .filter(u -> u.getCar().equals(car))
                 .findAny()
                 .orElse(null);
         return user;
      }
      return null;
   }

}
