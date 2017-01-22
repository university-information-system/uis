package at.ac.tuwien.inso.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EctsDistribution {

    @Column(nullable = false)
    private BigDecimal mandatory;

    @Column(nullable = false)
    private BigDecimal optional;

    @Column(nullable = false)
    private BigDecimal freeChoice;

    protected EctsDistribution() {
    }

    public EctsDistribution(BigDecimal mandatory, BigDecimal optional, BigDecimal freeChoice) {
        this.mandatory = mandatory;
        this.optional = optional;
        this.freeChoice = freeChoice;
    }

    public BigDecimal getMandatory() {
        return mandatory;
    }

    public BigDecimal getOptional() {
        return optional;
    }

    public BigDecimal getFreeChoice() {
        return freeChoice;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EctsDistribution that = (EctsDistribution) o;

        if (mandatory != null ? !mandatory.equals(that.mandatory) : that.mandatory != null) return false;
        if (optional != null ? !optional.equals(that.optional) : that.optional != null) return false;
        return freeChoice != null ? freeChoice.equals(that.freeChoice) : that.freeChoice == null;

    }

    @Override
    public int hashCode() {
        int result = mandatory != null ? mandatory.hashCode() : 0;
        result = 31 * result + (optional != null ? optional.hashCode() : 0);
        result = 31 * result + (freeChoice != null ? freeChoice.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EctsDistribution{" +
                "mandatory=" + mandatory +
                ", optional=" + optional +
                ", freeChoice=" + freeChoice +
                '}';
    }
}
