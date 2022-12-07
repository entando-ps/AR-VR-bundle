package it.iedx.login.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"token\"")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Token {

    @Id
    @Column(name = "\"current\"", length = 256)
    private String current;

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }
}
