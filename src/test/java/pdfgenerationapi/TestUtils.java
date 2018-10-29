package pdfgenerationapi;

import com.google.common.collect.Lists;
import pdfgenerationapi.models.Extents;
import pdfgenerationapi.models.Feature;
import pdfgenerationapi.models.Geometry;
import pdfgenerationapi.models.Llc1PdfRequest;
import pdfgenerator.models.Address;
import pdfgenerator.models.ChargeAddress;
import pdfgenerator.models.DocumentItem;
import pdfgenerator.models.Author;
import pdfgenerator.models.Item;
import pdfgenerator.models.LocalLandCharge;
import pdfgenerator.models.StructurePositionDimension;

import java.lang.reflect.Field;
import java.util.*;

public class TestUtils {

    /**
     * This sets values into the system environment.
     * It is a massive hack so should only be used when testing.
     *
     * @param newenv A map containing the key/value env var pairs
     * @throws Exception When environment not available
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void setEnv(Map<String, String> newenv) throws Exception {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        }
        catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for(Class cl : classes) {
                if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newenv);
                }
            }
        }
    }

    public static void SetupEnvironment() throws Exception {
        Map<String, String> environment = buildInitialVariables();
        addGoodCerts(environment);

        setEnv(environment);
    }

    public static void SetupEnvironmentBadCert() throws Exception {
        Map<String, String> environment = buildInitialVariables();
        addBadCerts(environment);
        setEnv(environment);
    }

    private static Map<String, String> buildInitialVariables()
    {
        Map<String, String> environment = new HashMap<>(7);
        environment.put("APP_NAME", "pdf-generation-api");
        environment.put("COMMIT", "LOCAL");
        environment.put("MAX_HEALTH_CASCADE", "2");
        environment.put("PORT", "8080");
        environment.put("SEARCH_API", "localhost:9898");
        environment.put("STORAGE_API", "localhost:9008");
        environment.put("WMS_SERVER_URL", "http://www.google.com/");
        environment.put("WMS_LAYER_NAME", "layer");
        environment.put("SIGNATURE_REASON", "testReason");
        environment.put("SIGNATURE_LOCATION", "testLocation");
        environment.put("AUTHENTICATION_API", "localhost:8003");
        environment.put("AUTHENTICATION_API_BASE", "localhost:8003");
        environment.put("WATERMARK", "SAMPLE");
        environment.put("GEOSERVER_URL", "http://www.google.co.uk/");
        environment.put("GEOSERVER_BOUNDARY_LAYER_NAME", "boundary layer");
        environment.put("GEOSERVER_NON_MIGRATED_STYLE_NAME", "style name");
        environment.put("IMAGE_GEN_THREAD_COUNT", "10");

        return environment;
    }

    private static void addGoodCerts(Map<String, String> environment) {
        environment.put("CERTIFICATE_PATH", "certs/dev_signature.crt");
        environment.put("PRIVATE_KEY_PATH", "certs/dev_signature.key");
    }

    private static void addBadCerts(Map<String, String> environment) {
        environment.put("CERTIFICATE_PATH", "certs/abc.crt");
        environment.put("PRIVATE_KEY_PATH", "certs/abc.key");
    }

    public static Llc1PdfRequest buildLlcRequest() {
        Geometry geometry = new Geometry();
        geometry.setType("Polygon");
        geometry.setCoordinates(buildCoords());

        Feature feature = new Feature();
        feature.setType("Feature");
        feature.setProperties("property");
        feature.setGeometry(geometry);

        Extents extents = new Extents();
        extents.setType("abc");
        extents.setFeatures(new ArrayList<Feature>() {{
            add(feature);
        }});

        Llc1PdfRequest llc1PdfRequest = new Llc1PdfRequest();
        llc1PdfRequest.setDescription("abc");
        llc1PdfRequest.setExtents(extents);
        llc1PdfRequest.setReferenceNumber(12);

        return llc1PdfRequest;
    }

    public static LocalLandCharge buildLocalLandCharge() {
        Geometry apigeometry = new Geometry();
        apigeometry.setType("Polygon");
        apigeometry.setCoordinates(buildCoords());

        Feature feature = new Feature();
        feature.setType("Feature");
        feature.setProperties("property");
        feature.setGeometry(apigeometry);

        List<Feature> features = new ArrayList<>();
        features.add(feature);

        pdfgenerator.models.Geometry geometry = new pdfgenerator.models.Geometry();
        geometry.setType("abc");
        geometry.setFeatures(features);

        Item item = new Item();
        Author author = new Author();
        Date date = new Date();

        item.setAuthor(author);
        item.setChargeCreationDate(date);
        item.setChargeGeographicDescription("charge Geo Description");
        item.setChargeType("charge type");
        item.setSupplementaryInformation("supplementary information");
        item.setFurtherInformationLocation("further information location");
        item.setFurtherInformationReference("further information ref");
        item.setGeometry(geometry);
        item.setInstrument("instrument");
        item.setLocalLandCharge("local land charge");
        item.setOriginatingAuthority("originating authority");
        item.setRegistrationDate(date);
        item.setStartDate(date);
        item.setStatutoryProvision("Statutory provision");

        LocalLandCharge localLandCharge = new LocalLandCharge();

        localLandCharge.setCancelled(false);
        localLandCharge.setDisplayId("abc");
        localLandCharge.setId(1);
        localLandCharge.setType("type");
        localLandCharge.setGeometry(geometry);
        localLandCharge.setItem(item);

        return localLandCharge;
    }

    public static LocalLandCharge buildLONCharge() {
        Geometry apigeometry = new Geometry();
        apigeometry.setType("Polygon");
        apigeometry.setCoordinates(buildCoords());

        Feature feature = new Feature();
        feature.setType("Feature");
        feature.setProperties("property");
        feature.setGeometry(apigeometry);

        List<Feature> features = new ArrayList<>();
        features.add(feature);

        pdfgenerator.models.Geometry geometry = new pdfgenerator.models.Geometry();
        geometry.setType("abc");
        geometry.setFeatures(features);

        Item item = new Item();
        Date date = new Date();
        Address address = new Address();
        address.setLine1("Street");
        address.setLine2("Town");
        address.setPostcode("Postcode");

        item.setChargeCreationDate(date);

        ChargeAddress chargeAddress = new ChargeAddress();
        chargeAddress.setLine1("street");
        chargeAddress.setLine2("town");
        chargeAddress.setPostcode("postcode");
        item.setChargeAddress(chargeAddress);

        StructurePositionDimension position = new StructurePositionDimension();
        position.setHeight("Unlimited height");
        position.setExtentCovered("All of the extent");
        item.setStructurePositionAndDimension(position);

        item.setChargeType("Light obstruction notice");
        item.setFurtherInformationLocation("further information location");
        item.setFurtherInformationReference("further information ref");
        item.setGeometry(geometry);
        item.setInstrument("instrument");
        item.setLocalLandCharge("local land charge");
        item.setOriginatingAuthority("originating authority");
        item.setRegistrationDate(date);
        item.setStartDate(date);
        item.setStatutoryProvision("Statutory provision");
        item.setExpiryDate(date);
        item.setTribunalTemporaryCertificateDate(date);
        item.setTribunalTemporaryCertificateExpiryDate(date);
        item.setTribunalDefinitiveCertificateDate(date);
        item.setApplicantName("Applicant Name");
        item.setApplicantAddress(address);
        item.setServientLandInterestDescription("Owner");

        LocalLandCharge localLandCharge = new LocalLandCharge();

        localLandCharge.setCancelled(false);
        localLandCharge.setDisplayId("abc");
        localLandCharge.setId(1);
        localLandCharge.setType("Light obstruction notice");
        localLandCharge.setGeometry(geometry);
        localLandCharge.setItem(item);
        
        Map<String, DocumentItem[]> docs = new HashMap<String, DocumentItem[]>();
        DocumentItem[] aDocArr = new DocumentItem[1];
        aDocArr[0] = new DocumentItem();
        aDocArr[0].setBucket("A Bucket");
        aDocArr[0].setFileId("A File ID");
        aDocArr[0].setReference("A Reference");
        aDocArr[0].setSubdirectory("A Subdirectory");
        docs.put("form-a", aDocArr);
        docs.put("form-b", aDocArr);
        docs.put("temporary-certificate", aDocArr);
        docs.put("definitive-certificate", aDocArr);
        docs.put("court-order", aDocArr);
        localLandCharge.getItem().setDocumentsFiled(docs);
        		
        return localLandCharge;
    }

    private static List<Object> buildCoords() {
        Collection<Double> coords = Lists.newArrayList(111.111, 111.111);
        Collection<Object> geometry = Lists.newArrayList(coords, coords, coords);
        return Lists.newArrayList(geometry);
    }
}
