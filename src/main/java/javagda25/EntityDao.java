package javagda25;

import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class EntityDao {


    public <T extends IBaseEntity> void saveOrUpdate (T entity) {

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            session.saveOrUpdate(entity);
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }



    public <T extends IBaseEntity> List<T> getAll(Class<T> classT) {
        List<T> list = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {

            // Narzędzie do kreowania zapytania, do tworzenia query i budowania klauzuli 'where'

            CriteriaBuilder cb = session.getCriteriaBuilder();

            // Obiekt reprezentujący zapytanie o <typ generyczny>

            CriteriaQuery<T> criteriaQuery = cb.createQuery(classT);

            // reprezentuje tabelę 'Student' i tworzymy tą instancję żeby powiedzieć
            // "do jakiej tabeli chcemy wykonać zapytanie"

            Root<T> rootTable = criteriaQuery.from(classT);

            // wykonanie select'a z tabeli

            criteriaQuery.select(rootTable);

            // wywołujemy zapytanie, wyniki zbieramy do listy

            list.addAll(session.createQuery(criteriaQuery).list());

            //Query<Student> query = session.createQuery("from Student",Student.class); //HQL
            //return query.list();

        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }

    public <T extends IBaseEntity> Optional<T> getById(Class<T> classT, Long id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            T entity = session.get(classT, id);
            return Optional.ofNullable(entity);
        }
    }

    public <T extends IBaseEntity> void delite(Class<T> classT, Long id) {

        Optional<T> entityOptional = getById(classT,id);

        if(entityOptional.isPresent()){
            delite(entityOptional.get());
        }else {
            System.err.println("Nie udało się odnaleźć instancji");
        }
    }

    public <T extends IBaseEntity> void delite(T entity) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        }
    }

}
