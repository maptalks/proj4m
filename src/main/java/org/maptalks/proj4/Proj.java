package org.maptalks.proj4;

public class Proj {

    private String projName;
    private String datumCode;

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getDatumCode() {
        return datumCode;
    }

    public void setDatumCode(String datumCode) {
        this.datumCode = datumCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Proj)) {
            return false;
        }
        Proj o = ((Proj) obj);
        return eql(this.projName, o.projName) && eql(this.datumCode, o.datumCode);
    }

    private boolean eql(Object a, Object b) {
        if (a != null) {
            if (b == null) {
                return false;
            }
            return a.equals(b);
        } else {
            if (b == null) {
                return true;
            }
            return false;
        }
    }
}
