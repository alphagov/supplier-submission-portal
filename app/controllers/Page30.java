package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.List;
import java.util.Map;

public class Page30 extends AuthenticatingController {

    private static final Long PAGE_ID = 30l;

    public static void savePage(Long listingId, String p30q1, String p30q2, String p30q3, String p30q4, String p30q5
                                        , String p30q1assurance, String p30q2assurance, String p30q3assurance, String p30q4assurance, String p30q5assurance, Boolean return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        // ALL LOTS
        validation.required(p30q1).key("p30q1");
        validation.maxSize(p30q1, 10);
        validation.required(p30q1assurance).key("p30q1");
        validation.maxSize(p30q1assurance, 50);

        if (!listing.lot.equals("SCS")) {
            // SaaS, PaaS, IaaS
            validation.required(p30q2).key("p30q2");
            validation.maxSize(p30q2, 10);
            validation.required(p30q3).key("p30q3");
            validation.maxSize(p30q3, 10);

            validation.required(p30q2assurance).key("p30q2");
            validation.maxSize(p30q2assurance, 50);
            validation.required(p30q3assurance).key("p30q3");
            validation.maxSize(p30q3assurance, 50);
        }

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            if (request.params.get("return_to_summary").equals("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
              redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }


        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p30q1", p30q1);
        page.responses.put("p30q2", p30q2);
        page.responses.put("p30q3", p30q3);
        page.responses.put("p30q1assurance", p30q1assurance);
        page.responses.put("p30q2assurance", p30q2assurance);
        page.responses.put("p30q3assurance", p30q3assurance);
        page.insert();
        listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
        if (request.params.get("return_to_summary").equals("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }

}
