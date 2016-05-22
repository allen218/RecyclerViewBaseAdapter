package com.example.mac.recyclerbaseadapter.info;

/**
 * Created by allen on 16/5/22.
 */
public class DataInfo {
    private String headerImgUrl;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataInfo dataInfo = (DataInfo) o;

        if (headerImgUrl != null ? !headerImgUrl.equals(dataInfo.headerImgUrl) : dataInfo.headerImgUrl != null)
            return false;
        if (name != null ? !name.equals(dataInfo.name) : dataInfo.name != null) return false;
        return email != null ? email.equals(dataInfo.email) : dataInfo.email == null;

    }

    @Override
    public int hashCode() {
        int result = headerImgUrl != null ? headerImgUrl.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    private String email;

    public String getHeaderImgUrl() {
        return headerImgUrl;
    }

    public void setHeaderImgUrl(String headerImgUrl) {
        this.headerImgUrl = headerImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
