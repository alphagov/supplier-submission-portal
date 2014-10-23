package controllers;

import com.google.gson.Gson;
import models.Listing;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page36 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 36l;

    public static void savePage(Long listingId, String[] p36q1, String p36q1assurance, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p36q1).key("p36q1");
        validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p36q1, 100)).key("p36q1").message("Invalid values");
        validation.required(p36q1assurance).key("p36q1");
        validation.maxSize(p36q1assurance, 50);

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            if (return_to_summary.contains("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
              redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }

        Map<String, String> pageResponses = new HashMap<String, String>();
        Gson gson = new Gson();
        pageResponses.put("p36q1", gson.toJson(p36q1));
        pageResponses.put("p36q1assurance", p36q1assurance);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
