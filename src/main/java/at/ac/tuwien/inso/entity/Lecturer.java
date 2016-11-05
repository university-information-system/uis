package at.ac.tuwien.inso.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Lecturer {

    @Id
    @GeneratedValue
    private Long id;

}
