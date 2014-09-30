package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

<<<<<<< HEAD
import play.data.validation.*;
import play.data.validation.Error;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class Page35 extends Controller {
=======
public class Page35 extends AuthenticatingController {
>>>>>>> d339c59... Added cookie authentication to all controllers + check supplier ids match owner

    private static final Long PAGE_ID = 35l;

    public static void savePage(Long listingId, String p35q1) {

        Listing listing = Listing.getByListingId(listingId);

        // TODO: Validate all fields on this page requiring validation
        validation.required(p35q1).key("p35q1");

        if(validation.hasErrors()) {
            //flash.error("%s", validation.errors());

            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p35q1", p35q1);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
