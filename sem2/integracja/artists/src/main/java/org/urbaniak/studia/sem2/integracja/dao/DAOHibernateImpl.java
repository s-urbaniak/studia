package org.urbaniak.studia.sem2.integracja.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.urbaniak.studia.sem2.integracja.entity.Artist;

public class DAOHibernateImpl extends HibernateDaoSupport {
    private Integer batchSize = 0;

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public void saveOrUpdateBatch(List list) {
        if (batchSize == null)
            throw new UnsupportedOperationException(
                    "batchSize not set, cannot perform batch updates.");

        int batchIterations = list.size() / getBatchSize();

        for (int i = 0; i < batchIterations; i++) {
            int toSize = (i + 1) * getBatchSize();
            if (toSize > list.size())
                toSize = list.size();
            List subList = list.subList(i * getBatchSize(), toSize);

            getHibernateTemplate().saveOrUpdateAll(subList);
            getHibernateTemplate().flush();
            getHibernateTemplate().clear();
        }
    }
}