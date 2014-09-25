package controllers;

import models.Listing;
import models.Page;

import java.util.Arrays;
import play.mvc.Controller;


public class Page1 extends Controller {

    private static final Long PAGE_ID = 1l;

    public static void savePage(Long listingId, String[] p1q1) {
        
        Listing listing = Listing.getByListingId(listingId);
        // Validate all fields on this page requiring validation
        validation.required(p1q1).message("p1q1 : null");
        
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p1q1", Arrays.asList(p1q1).toString());
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
