package pl.goralczyk.controllers;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.goralczyk.config.DBManager;
import pl.goralczyk.config.DataConnect;
import pl.goralczyk.config.SessionUtils;
import pl.goralczyk.entity.Pacjent;

@SessionScoped
public class PacjentBean {

    private Pacjent pacjent = new Pacjent();
    private String username;
    private String password;
    private String imie;
    private String nazwisko;
    private String pesel;
    private int przychodniaID;
    private long ID;
    private int pacjentId;

    public int getPacjentId() {
        return pacjentId;
    }

    public void setPacjentId(int pacjentId) {
        this.pacjentId = pacjentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public int getPrzychodniaID() {
        return przychodniaID;
    }

    public void setPrzychodniaID(int przychodniaID) {
        this.przychodniaID = przychodniaID;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public Pacjent getPacjent() {
        return pacjent;
    }

    public void setPacjent(Pacjent pacjent) {
        this.pacjent = pacjent;
    }

    public String logout() {
        ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
        HttpSession session = (HttpSession) ectx.getSession(false);
        session.invalidate();
        return "/loginPage.xhtml";
    }

    public String validateUsernamePassword() throws SQLException {
        boolean valid = PacjentBean.validate(username, password);
        HttpSession session = SessionUtils.getSession();
        if (valid) {
            if (username.equals("admin") && password.equals("admin")) {
                EntityManager em = DBManager.getManager().createEntityManager();
                pacjent = (Pacjent) em.createQuery("SELECT p FROM Pacjent p WHERE p.username = :username").setParameter("username", username).getSingleResult();

                session.setAttribute("username", username);
                session.setAttribute("haslo", password);
                em.close();
                return "/adminPage.xhtml";
            } else {
                FacesContext fc = FacesContext.getCurrentInstance();
                EntityManager em = DBManager.getManager().createEntityManager();
                pacjent = (Pacjent) em.createQuery("SELECT p FROM Pacjent p WHERE p.username = :username").setParameter("username", username).getSingleResult();

                session.setAttribute("username", username);
                session.setAttribute("haslo", password);
                em.close();
                return "/patientPage.xhtml";

            }
        } else {
            return "/loginPageWarning.xhtml";
        }
    }

    public static boolean validate(String user, String password) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DataConnect.getConnection();
            ps = (PreparedStatement) con.prepareStatement("Select * from pacjent where username = ? and haslo = ?");
            ps.setString(1, user);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            return false;
        } finally {
            DataConnect.close(con);
        }
        return false;
    }

    public String dodaj() {
        EntityManager em = DBManager.getManager().createEntityManager();
        em.getTransaction().begin();
        pacjent.setId(null);
        em.persist(pacjent);
        em.getTransaction().commit();
        em.close();
        this.pacjent = new Pacjent();
        return null;
    }

    public void pacjentListener() {
        String ids = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pacjentID");//.toString()
        int id = Integer.parseInt(ids);
        this.pacjent.setId(id);
    }

    public String zaladujDoEdycji() {
        EntityManager em = DBManager.getManager().createEntityManager();
        this.pacjent = em.find(Pacjent.class,
                pacjent.getId());
        em.close();
        return "/editPatient.xhtml";
    }

    public String usun() {
        EntityManager em = DBManager.getManager().createEntityManager();
        em.getTransaction().begin();

        this.pacjent = em.find(Pacjent.class,
                pacjent.getId());
        em.remove(this.pacjent);
        this.pacjent = new Pacjent();
        em.getTransaction().commit();
        em.close();
        return null;
    }

    public List<Pacjent> getLista() {
        EntityManager em = DBManager.getManager().createEntityManager();
        List list = em.createNamedQuery("Pacjent.findAll").getResultList();
        em.close();
        return list;
    }

    public String edytuj() {
        EntityManager em = DBManager.getManager().createEntityManager();
        em.getTransaction().begin();
        em.merge(this.pacjent);
        em.getTransaction().commit();
        em.close();
        this.pacjent = new Pacjent();
        return "/editPatientSuccess.xhtml";
    }

}
