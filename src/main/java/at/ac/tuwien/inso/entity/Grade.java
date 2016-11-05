package at.ac.tuwien.inso.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Grade {

    @Id
    @GeneratedValue
    private Long id;

}
