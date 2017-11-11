/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.AuctionEntityControllerLocal;
import ejb.session.stateless.BidEntityControllerLocal;
import ejb.session.stateless.CustomerEntityControllerLocal;
import entity.AuctionEntity;
import entity.BidEntity;
import entity.CustomerEntity;
import entity.ProxyBiddingEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CustomerTypeEnum;
import util.exception.AuctionNotFoundException;
import util.exception.BidAlreadyExistException;
import util.exception.CustomerAlreadyPremiumException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerNotPremiumException;
import util.exception.DuplicateException;
import util.exception.GeneralException;
import util.exception.IncorrectPasswordException;

/**
 *
 * @author alina
 */
@WebService(serviceName = "ProxyWebService")
@Stateless()
public class ProxyWebService {

    @EJB
    private BidEntityControllerLocal bidEntityController;

    @EJB
    private AuctionEntityControllerLocal auctionEntityController;
    @PersistenceContext(unitName = "OnlineAuctionSystem-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerEntityControllerLocal customerEntityController;
    
    
    
    @WebMethod(operationName = "registration")
    public CustomerEntity registration(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws CustomerNotFoundException, IncorrectPasswordException, CustomerAlreadyPremiumException{
        System.out.println("******* [OAS Web Service] Registration");
        CustomerEntity c =  customerEntityController.customerLogin(username, password);
        if(c.getCustomerTypeEnum().equals(CustomerTypeEnum.PREMIUM))
            throw new CustomerAlreadyPremiumException("Customer "+username+" is already a premium customer!");
        c.setCustomerTypeEnum(CustomerTypeEnum.PREMIUM);
        
        return c;
    }
    
    /**
     *
     * @param username
     * @param password
     * @return
     * @throws CustomerNotFoundException
     * @throws IncorrectPasswordException
     * @throws DuplicateException
     */
    @WebMethod(operationName = "customerLogin")
    public CustomerEntity customerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws CustomerNotFoundException, IncorrectPasswordException, CustomerNotPremiumException{
        System.out.println("******* [OAS Web Service] Remote Login");
        
        CustomerEntity c = customerEntityController.customerLogin(username, password);
        detachBidAddress(c.getBidEntities());
        
        if(c.getCustomerTypeEnum() == CustomerTypeEnum.NORMAL)
            throw new CustomerNotPremiumException("Customer "+username+" is not a premium customer!");
        return c;
    }
    
    private void detachBidAddress(List<BidEntity> bids){
        for(BidEntity b: bids){
            em.detach(b);
            b.setAddressEntity(null);
        }
   
    }
    
    @WebMethod(operationName = "viewAuctionListDetails")
    public AuctionEntity viewAuctionListDetails(@WebParam(name = "id") Long id) throws AuctionNotFoundException {
        return auctionEntityController.retrieveAuctionById(id);
    }
    
    @WebMethod(operationName = "viewAuctionListByName")
    public List<AuctionEntity> viewAuctionListByName(@WebParam(name = "productName") String productName) throws AuctionNotFoundException{
        return auctionEntityController.retrieveAuctionByProductName(productName);
    }
    
    @WebMethod(operationName = "viewCreditBalance")
    public BigDecimal viewCreditBalance(@WebParam(name = "id") Long id)  throws CustomerNotFoundException, GeneralException{
        System.out.println("******* [OAS Web Service] View Credit Balance");
        return customerEntityController.retrieveCustomerById(id).getCreditBalance();
    }
    
    @WebMethod(operationName = "viewAllAuctionListings")
    public List<AuctionEntity> viewAllAuctionListings() throws GeneralException{
        System.out.println("******* [OAS Web Service] View All Auction Listings");
        return auctionEntityController.viewAllAuction();
    }
    
    @WebMethod(operationName = "viewWonAuctionListings")
    public List<AuctionEntity> viewWonAuctionListings(@WebParam(name = "id") Long cid) throws GeneralException{
        System.out.println("******* [OAS Web Service] View Won Auction Listings");
        return auctionEntityController.viewWonAuction(cid);
    }
    

    @WebMethod(operationName = "createSnippingBid")
    public void createSnippingBid(@WebParam(name="bid")BidEntity bid, @WebParam(name="maxPrice") BigDecimal maxPrice, @WebParam(name="timeDuration" )int timeDuration, @WebParam(name="aid") Long aid, @WebParam(name="cid") Long cid) throws CustomerNotFoundException, AuctionNotFoundException, BidAlreadyExistException, GeneralException{
        bidEntityController.createSnipingBid(timeDuration, bid, cid, aid, maxPrice); 
    }

    @WebMethod(operationName = "createProxyBid")
    public void createProxyBid(@WebParam(name="bid")ProxyBiddingEntity bid, @WebParam(name="maxPrice") BigDecimal maxPrice, @WebParam(name="aid") Long aid, @WebParam(name="cid") Long cid) {
        
    }
}
