/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AuctionEntityControllerLocal;
import ejb.session.stateless.BidEntityControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.DuplicateException;
import util.exception.GeneralException;
import util.exception.StaffAlreadyExistException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author alina
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private BidEntityControllerLocal bidEntityController;

    @EJB
    private AuctionEntityControllerLocal auctionEntityController;

    @EJB
    private StaffEntityControllerLocal staffEntityController;

    @PersistenceContext(unitName = "OnlineAuctionSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        try {
            staffEntityController.retrieveStaffByIdentificationNumber("123");
        } catch (StaffNotFoundException| DuplicateException ex) {
            initializeDataStaff1();
        }
    }
    
    

    private void initializeDataStaff1() {
        try {
            staffEntityController.createNewStaffEntity(new StaffEntity("Anthony", "Young", "123", "manager", "password", EmployeeAccessRightEnum.MANAGER));
        } catch (StaffAlreadyExistException | GeneralException ex) {
            System.out.println("Error in Singleton");
        }
    }

}

//private void initializeDataAuction() {
//  AuctionEntity auction = new AuctionEntity(Long id, Date startingTime, Date endingTime, Boolean status, BigDecimal reservePrice, BigDecimal winningBid, String productName, String productDescription, Long winningCustomerId, List<BidEntity> bidEntities);
    //}
