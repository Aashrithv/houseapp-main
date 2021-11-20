package com.example.improvedhouse.models;

/**
 * Created by Joanna Ruth on 04-Jul-18.
 */

public class HouseProperties {
    String housename;
    String housetype;
    String houseaddress;
    String rentamount;
    String lease_rent_sale;
    String youremail;
    String image;
    private String mkey;

    public HouseProperties(){ }

    public HouseProperties(String apartmentname, String typeofhouse, String address, String rentamount, String leaserentsale, String youremail, String image) {
        this.housename=apartmentname;
        this.housetype=typeofhouse;
        this.houseaddress = address;
        this.rentamount = rentamount;
        this.lease_rent_sale=leaserentsale;
        this.youremail=youremail;
        this.image=image;

    }

    public String getHousename() {
        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename;
    }

    public String getHousetype() {
        return housetype;
    }

    public void setHousetype(String housetype) {
        this.housetype = housetype;
    }

    public String getHouseaddress() {
        return houseaddress;
    }

    public void setHouseaddress(String houseaddress) {
        this.houseaddress = houseaddress;
    }

    public String getRentamount() {
        return rentamount;
    }

    public void setRentamount(String rentamount) {
        this.rentamount = rentamount;
    }

    public String getLease_rent_sale() {
        return lease_rent_sale;
    }

    public void setLease_rent_sale(String lease_rent_sale) {
        this.lease_rent_sale = lease_rent_sale;
    }

    public String getYouremail() {
        return youremail;
    }

    public void setYouremail(String youremail) {
        this.youremail = youremail;
    }




    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getkey() {
        return mkey;
    }

    public void setkey(String mkey) {
        this.mkey = mkey;
    }

}
