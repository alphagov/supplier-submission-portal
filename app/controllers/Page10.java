package controllers;

import com.google.gson.Gson;
import models.Listing;

import play.Logger;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;
import uk.gov.gds.dm.Fixtures;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page10 extends AuthenticatingQuestionPage {


    private static final Long PAGE_ID = 10l;

    public static void savePage(Long listingId, String[] p10q1, String p10q2, String p10q3, String p10q4, String p10q5, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        notFoundIfNull(listing);

        if(!listing.supplierId.equals(getSupplierId())) {
            Logger.error("Supplier id of listing did not match the logged in supplier. Expected: " + listing.supplierId + ", Found: " + getSupplierId());
            notFound();
        }

        if (listing.serviceSubmitted) {
          Logger.info("Trying to edit a submitted service; redirect to summary page.");
          redirect(listing.summaryPageUrl());
        }

        validation.required(p10q1).key("p10q1");
        validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p10q1, 20)).key("p10q1").message("Invalid values");

        validation.required(p10q2).key("p10q2");
        validation.maxSize(p10q2, 10);

        validation.required(p10q3).key("p10q3");
        validation.isTrue(ValidationUtils.isWordCountLessThan(p10q3, 20)).key("p10q3").message("Your answer must be less than 20 words.");
        validation.maxSize(p10q3, 200).message("Your answer must be less than 200 characters in length.");
        validation.isTrue(!p10q3.startsWith("[")).key("p10q3").message("The character '[' is not allowed here.");

        validation.required(p10q4).key("p10q4");
        validation.isTrue(ValidationUtils.isWordCountLessThan(p10q4, 20)).key("p10q4").message("Your answer must be less than 20 words.");
        validation.maxSize(p10q4, 200).message("Your answer must be less than 200 characters in length.");
        validation.isTrue(!p10q4.startsWith("[")).key("p10q4").message("The character '[' is not allowed here.");

        validation.required(p10q5).key("p10q5");
        validation.maxSize(p10q5, 10);

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, Fixtures.getErrorMessage(key, value));
            }
            Logger.info(String.format("Validation errors: %s; reloading page.", validation.errorsMap().toString()));
            if (return_to_summary.contains("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
                redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }

        //Save the form data as a Page into the correct page index
        Map<String, String> pageResponses = new HashMap<String, String>();
        Gson gson = new Gson();
        pageResponses.put("p10q1", gson.toJson(p10q1));
        pageResponses.put("p10q2", p10q2);
        pageResponses.put("p10q3", p10q3);
        pageResponses.put("p10q4", p10q4);
        pageResponses.put("p10q5", p10q5);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
   }
}
