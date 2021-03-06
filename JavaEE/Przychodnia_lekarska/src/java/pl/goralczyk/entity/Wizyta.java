/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.goralczyk.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Artur
 */
@Entity
@Table(name = "wizyta", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"lekarz", "data"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Wizyta.findAll", query = "SELECT w FROM Wizyta w")
    , @NamedQuery(name = "Wizyta.findByLekarz", query = "SELECT w FROM Wizyta w WHERE w.wizytaPK.lekarz = :lekarz")
    , @NamedQuery(name = "Wizyta.findByPacjent", query = "SELECT w FROM Wizyta w WHERE w.wizytaPK.pacjent = :pacjent")
    , @NamedQuery(name = "Wizyta.findByData", query = "SELECT w FROM Wizyta w WHERE w.wizytaPK.data = :data")})
public class Wizyta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WizytaPK wizytaPK;
    @JoinColumn(name = "lekarz", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Lekarz lekarz1;

    public Wizyta() {
    }

    public Wizyta(WizytaPK wizytaPK) {
        this.wizytaPK = wizytaPK;
    }

    public Wizyta(int lekarz, int pacjent, Date data) {
        this.wizytaPK = new WizytaPK(lekarz, pacjent, data);
    }

    public WizytaPK getWizytaPK() {
        return wizytaPK;
    }

    public void setWizytaPK(WizytaPK wizytaPK) {
        this.wizytaPK = wizytaPK;
    }

    public Lekarz getLekarz1() {
        return lekarz1;
    }

    public void setLekarz1(Lekarz lekarz1) {
        this.lekarz1 = lekarz1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wizytaPK != null ? wizytaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Wizyta)) {
            return false;
        }
        Wizyta other = (Wizyta) object;
        if ((this.wizytaPK == null && other.wizytaPK != null) || (this.wizytaPK != null && !this.wizytaPK.equals(other.wizytaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.goralczyk.entity.Wizyta[ wizytaPK=" + wizytaPK + " ]";
    }

    public String getWizytaPKAsString() {
        WizytaPK klucz = this.getWizytaPK();
        return klucz.getLekarz() + ";" + klucz.getPacjent() + ";" + klucz.getData().getTime();
    }

    public static WizytaPK convertStringAsWizytaPK(String s) {
        String[] czesciKlucza = s.split(";");
        int lekarzID = Integer.parseInt(czesciKlucza[0]);
        int pacjentID = Integer.parseInt(czesciKlucza[1]);
        Date data = new Date(Long.parseLong(czesciKlucza[2]));
        WizytaPK klucz = new WizytaPK(lekarzID, pacjentID, data);
        return klucz;
    }
}
