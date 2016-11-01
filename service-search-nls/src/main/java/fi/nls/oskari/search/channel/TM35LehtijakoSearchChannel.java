package fi.nls.oskari.search.channel;

import fi.mml.portti.service.search.ChannelSearchResult;
import fi.mml.portti.service.search.IllegalSearchCriteriaException;
import fi.mml.portti.service.search.SearchCriteria;
import fi.mml.portti.service.search.SearchResultItem;
import fi.nls.aluejako.karttalehtijako.utm_karttalehti;
import fi.nls.oskari.annotation.Oskari;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;

@Oskari("TM35LEHTIJAKO_CHANNEL")
public class TM35LehtijakoSearchChannel extends SearchChannel {

    private Logger log = LogFactory.getLogger(this.getClass());

    @Override
    public void init() {
        
    }

    public Capabilities getCapabilities() {
        return Capabilities.BOTH;
    }
    
    /**
     * Find centroid for a grid square
     * 
     * @param searchCriteria
     * @return 
     */
    @Override
    public ChannelSearchResult doSearch(SearchCriteria searchCriteria) {
        log.debug("lehtijako");

        String lehti = searchCriteria.getSearchString();
        
        utm_karttalehti l = new utm_karttalehti(lehti);
        l = l.lehti_numerolla(lehti);
        
        double[] sijainti = l.sijainti();   // suorakaide pisteet
                
//        for (double d : sijainti) {
//            log.debug("d = " + d);
//        }

        double[] keskipiste = laskeKeskipiste(sijainti);

        ChannelSearchResult result = new ChannelSearchResult();
        SearchResultItem item = new SearchResultItem();
        item.setLat(keskipiste[0]);
        item.setLon(keskipiste[1]);
        result.addItem(item);
         
       return result;
    }
    
    /**
     * Find grid square for coordinates
     * 
     * @param searchCriteria
     * @return
     * @throws IllegalSearchCriteriaException 
     */
    public ChannelSearchResult reverseGeocode(SearchCriteria searchCriteria) throws IllegalSearchCriteriaException {
        log.debug("lehtijako");
        
        double x = searchCriteria.getLat();
        double y = searchCriteria.getLon();
        
        int scale = Integer.parseInt((String) searchCriteria.getParam("scale")); // pitää olla jokin näistä: 100000,50000,25000,20000,10000,5000
        double[] pt = new double[]{x, y}; // E, N (EPSG:3067)

        utm_karttalehti lehti = new utm_karttalehti();
        lehti = lehti.pisteessa(pt, scale);

        ChannelSearchResult result = new ChannelSearchResult();
        SearchResultItem item = new SearchResultItem();
        item.setType("tm35lehtijako");
        item.setTitle(lehti.lehtinumero());
        item.setDescription(lehti.lehtinumero());
        item.setLang("en");
        
        item.setLat(x);
        item.setLon(y);
        
//        item.addValue("lehti", lehti.lehtinumero());
        result.addItem(item);
        
        
              
        return result;
    }    
    
    private double[] laskeKeskipiste(double[] pisteet) {
        double x = ((pisteet[4] - pisteet[0]) / 2) + pisteet[0];
        double y = ((pisteet[3] - pisteet[1]) / 2) + pisteet[1];
        return new double[]{x, y};
    }
}
