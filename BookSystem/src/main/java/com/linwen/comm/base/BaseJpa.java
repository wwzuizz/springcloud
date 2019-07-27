package com.linwen.comm.base;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by linwen on 18-12-7.
 */
@NoRepositoryBean
public interface BaseJpa<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
