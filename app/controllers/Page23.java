package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page23 extends Controller {

    private static final Long PAGE_ID = 23l;

    public static void savePage(Long listingId, String p23q1, String p23q2, String p23q3) {

        Listing listing = Listing.getByListingId(listingId);
        
        // Validate all fields on this page requiring validation
        validation.required(p23q1).message("p23q1:null");
        if(!listing.lot.equals("SaaS")){
            validation.required(p23q2).message("p23q2:null");
            validation.required(p23q3).message("p23q3:null");
        }
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p23q1", p23q1);
        page.responses.put("p23q2", p23q2);
        page.responses.put("p23q3", p23q3);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

    public static void savePage(Long listingId, String p23q1) {
        savePage(listingId, p23q1, null, null);
    }
}
