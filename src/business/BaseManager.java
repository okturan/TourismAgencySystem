package business;

import java.util.ArrayList;

import dao.BaseDao;
import entity.BaseEntity;

public abstract class BaseManager<E extends BaseEntity> {

    final BaseDao<E> dao;

    public BaseManager(BaseDao<E> dao) {
        this.dao = dao;
    }

    public BaseDao<E> getDao() {
        return dao;
    }

    public boolean save(E entity) {
        if (getId(entity) != 0) {
            return false;
        }
        return dao.save(entity);
    }

    public boolean update(E entity) {
        if (dao.findById(getId(entity)) == null) {
            return false;
        }
        return dao.update(entity);
    }

    public boolean delete(int id) {
        if (dao.findById(id) == null) {
            return false;
        }
        return dao.delete(id);
    }

    public abstract ArrayList<Object[]> formatDataForTable(ArrayList<E> entities);

    public E getById(int id) {
        return dao.findById(id);
    }

    public ArrayList<E> findAll() {
        return dao.findAll();
    }

    public int getId(E entity) {
        return entity.getId();
    }
}
