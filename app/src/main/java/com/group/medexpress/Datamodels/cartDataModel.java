package com.group.medexpress.Datamodels;

public class cartDataModel {


    private String ProductDocID;
    private String productID;
    private String productName;
    private String productPrice;
    private String quantityInStock;
    private String productImage;
    private String productDesc;
    private String productQty;



    public cartDataModel(String productDocID, String productID, String productName,
                             String productPrice, String quantityInStock, String productImage,
                             String productDesc, String productQty) {
        ProductDocID = productDocID;
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantityInStock = quantityInStock;
        this.productImage = productImage;
        this.productDesc = productDesc;
        this.productQty = productQty;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(String quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDocID() {
        return ProductDocID;
    }

    public void setProductDocID(String productDocID) {
        ProductDocID = productDocID;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductQty() {return productQty;}

    public  void setProductQty(String productQty) {this.productQty = productQty;}

}
