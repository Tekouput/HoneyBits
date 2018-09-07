package com.teko.honeybits.honeybits.models;

public class Picture {

    public static class SizeSources {
        private String big;
        private String medium;
        private String small;

        public SizeSources(String big, String medium, String small) {
            this.big = big;
            this.medium = medium;
            this.small = small;
        }

        public String getBig() {
            return big;
        }

        public void setBig(String big) {
            this.big = big;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }
    }

    private String id;
    private String productID;
    private SizeSources urls;

    public Picture(String id, String productID, SizeSources urls) {
        this.id = id;
        this.productID = productID;
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public SizeSources getUrls() {
        return urls;
    }

    public void setUrls(SizeSources urls) {
        this.urls = urls;
    }
}
