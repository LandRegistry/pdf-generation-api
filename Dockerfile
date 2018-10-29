FROM hmlandregistry/dev_base_java:3

ENV APP_NAME=pdf-generation-api \
    MAX_HEALTH_CASCADE=6 \
    PORT=8080 \
    SEARCH_API="http://dev-search-service-search-api:8080/v2.0/search/local_land_charges" \
    STORAGE_API="http://storage-api:8080/v1.0/storage" \
    CERTIFICATE_PATH="certs/dev_signature.crt" \
    PRIVATE_KEY_PATH="certs/dev_signature.key" \
    SIGNATURE_REASON="Official document generated" \
    SIGNATURE_LOCATION="Plymouth" \
    WMS_SERVER_URL="http://mapproxy-api:8080/service" \
    WMS_LAYER_NAME="os_hybrid_bng" \
    AUTHENTICATION_API="http://dev-search-authentication-api:8080/v2.0/authentication/validate"  \
    AUTHENTICATION_API_BASE="http://dev-search-authentication-api:8080" \
    WATERMARK="SAMPLE" \
    GEOSERVER_URL="http://geoserver:8080/geoserver/wms" \
    GEOSERVER_BOUNDARY_LAYER_NAME="llc:boundaries_organisation_combined" \
    GEOSERVER_NON_MIGRATED_STYLE_NAME="authority_boundary_search" \
    IMAGE_GEN_THREAD_COUNT=10