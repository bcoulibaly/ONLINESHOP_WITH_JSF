package de.hsb.app.controller;

import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.primefaces.event.FlowEvent;

import de.hsb.app.Model.Anrede;
import de.hsb.app.Model.Kunde;
import de.hsb.app.Model.Rolle;

@ManagedBean(name = "kundenHandler")
@SessionScoped
public class KundenHandler {

	@PersistenceContext
	private EntityManager entityManager;
	@Resource
	private UserTransaction userTransaction;
	
	private DataModel<Kunde> kundenListe;
	private Kunde merkeKunde;
	private Kunde selectedUser;
	private boolean skip;
	String lastPage;

	public KundenHandler() {
	}

	@PostConstruct
	public void init() {

		try {
			userTransaction.begin();

			entityManager.persist(new Kunde("Ben", "Coulibaly", new GregorianCalendar(1997, 4, 3).getTime(),
					"bcoulibaly", "beniboy",Rolle.ADMIN, Anrede.HERR));
			entityManager.persist(new Kunde("Lionel", "Ngoubayou", new GregorianCalendar(1990, 9, 15).getTime(),
					"lngoubayou", "lgoubayou", Rolle.KUNDE, Anrede.HERR));
			entityManager.persist(new Kunde("Amadou", "Sow", new GregorianCalendar(1994, 5, 21).getTime(), "asow",
					"asow", Rolle.KUNDE, Anrede.HERR));
			kundenListe = new ListDataModel<Kunde>();
			kundenListe.setWrappedData(entityManager.createNamedQuery("SelectKunden").getResultList());

			userTransaction.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			
			e.printStackTrace();
		}
	}
	
	public String neu() {
		merkeKunde = new Kunde();
		
		return "neuerKunde";
	}
	
	public String kundeBearbeiten(Kunde kunde) {
		merkeKunde = kunde;
		return "Registrieren";
	}

	public String speichern() {
		try {
			System.out.println("Speichern wurde aufgerufen");
			userTransaction.begin();
			
			merkeKunde = entityManager.merge(merkeKunde);
			entityManager.persist(merkeKunde);
			kundenListe.setWrappedData(entityManager.createNamedQuery("SelectKunden").getResultList());
			
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return "homePageAdmin";
	}
	
	
	public boolean isSkip() {
        return skip;
    }
 
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
     
    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }
	
	public Rolle[] getRolleValues() {
		return Rolle.values();
	}

	public DataModel<Kunde> getKundenListe() {
		return kundenListe;
	}

	public void setKundenListe(DataModel<Kunde> kundenListe) {
		this.kundenListe = kundenListe;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public UserTransaction getUserTransaction() {
		return userTransaction;
	}

	public void setUserTransaction(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}

	public Kunde getMerkeKunde() {
		return merkeKunde;
	}

	public void setMerkeKunde(Kunde merkeKunde) {
		this.merkeKunde = merkeKunde;
	}

	public Kunde getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(Kunde actuelUser) {
		this.selectedUser = actuelUser;
	}

}
