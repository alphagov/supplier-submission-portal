package controllers;

import models.Listing;
import models.Page;


public class QuestionPageDisplay extends AuthenticatingController {

    public static void showPage(Long pageId, Long listingId) {

        Listing listing = Listing.getByListingId(listingId);

        notFoundIfNull(listing);

        if(!listing.pageSequence.contains(pageId)){
            notFound();
        }

        // Check listing belongs to authenticated user
        if(!listing.supplierId.equals(supplierDetailsFromCookie.get("supplierId")) ){
            notFound();
        }

        int index = listing.pageSequence.indexOf(pageId);
        renderArgs.put("lot", listing.lot);
        renderArgs.put("listingID", listing.id);
        renderArgs.put("content", Fixtures.getContentProperties());
        renderArgs.put("pageNum", Integer.toString(index+1));
        renderArgs.put("pageTotal", Integer.toString(listing.pageSequence.size()));
        renderArgs.put("listingId", listingId);

        Page page = listing.completedPages.get(index);
        if (page!= null) {
            renderArgs.put("oldValues", page.responses);
        }
        renderArgs.put("serviceName", listing.title);
        renderTemplate(String.format("QuestionPages/%d.html", pageId));
    }
}
