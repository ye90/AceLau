package generatormybatis.com.mybatis.entity;

public class BbsProductWithBLOBs extends BbsProduct {
    private String description;

    private String packageList;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getPackageList() {
        return packageList;
    }

    public void setPackageList(String packageList) {
        this.packageList = packageList == null ? null : packageList.trim();
    }
}